package com.kcx.common.utils.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 线程池工具类
 * 利用线程池比单线程更快的完成耗时任务
 */
@Slf4j
public class ThreadPoolUtils {

    /**
     * 使用完整自定义的线程池执行任务,不需要返回值
     * @param taskList 任务列表
     * @param corePoolSize 核心线程数目 (最多保留的线程数)
     * @param maximumPoolSize 最大线程数目
     * @param keepAliveTime 生存时间 - 针对救急线程
     * @param unit 时间单位 - 针对救急线程
     * @param workQueue 阻塞队列，任务队列
     * @param threadFactory 线程工厂 - 可以为线程创建时起个好名字
     * @param handler 拒绝策略
     */
    public static void runThreadPoolExecutorTask(List<Runnable> taskList,
                                              int corePoolSize,
                                              int maximumPoolSize,
                                              long keepAliveTime,
                                              TimeUnit unit,
                                              BlockingQueue<Runnable> workQueue,
                                              ThreadFactory threadFactory,RejectedExecutionHandler handler){
        ExecutorService pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory,handler);
        for (Runnable task : taskList){
            pool.execute(task);
        }
        //不会接收新任务,但已提交任务会执行完,最后关闭
        pool.shutdown();
    }

    /**
     * 使用固定大小的线程池执行任务,不需要返回值
     * 核心线程数 == 最大线程数（没有救急线程被创建），因此也无需超时时间
     * 阻塞队列是无界的，可以放任意数量的任务
     * 适用于任务量已知，相对耗时的任务
     * @param taskList 任务列表
     * @param nThreads 线程数
     * @param threadFactory 线程工厂
     */
    public static void runNewFixedThreadPool(List<Runnable> taskList,int nThreads,ThreadFactory threadFactory){
        ExecutorService pool = Executors.newFixedThreadPool(nThreads,threadFactory);
        for (Runnable task : taskList){
            pool.execute(task);
        }
        //不会接收新任务,但已提交任务会执行完,最后关闭
        pool.shutdown();
    }


    /**
     * 使用缓存线程池执行任务,不需要返回值
     * 核心线程数是 0， 最大线程数是 Integer.MAX_VALUE，救急线程的空闲生存时间是 60s，意味着全部都是救急线程（60s 后可以回收）救急线程可以无限创建
     * 队列采用了 SynchronousQueue 实现特点是，它没有容量，没有线程来取是放不进去的（一手交钱、一手交货）
     * 整个线程池表现为线程数会根据任务量不断增长，没有上限，当任务执行完毕，空闲 1分钟后释放线程。
     * 适合任务数比较密集，但每个任务执行时间较短的情况
     * @param taskList 任务列表
     */
    public static void runNewCachedThreadPool(List<Runnable> taskList){
        ExecutorService pool = Executors.newCachedThreadPool();
        for (Runnable task : taskList){
            pool.execute(task);
        }
        //不会接收新任务,但已提交任务会执行完,最后关闭
        pool.shutdown();
    }

    /**
     * 使用单线程线程池执行任务
     * 希望多个任务排队执行。线程数固定为 1，任务数多于 1 时，会放入无界队列排队。任务执行完毕，这唯一的线程也不会被释放。
     * 区别：自己创建一个单线程串行执行任务，如果任务执行失败而终止那么没有任何补救措施，而线程池还会新建一个线程，保证池的正常工作
     * @param taskList 任务列表
     */
    public static void runNewSingleThreadExecutor(List<Runnable> taskList) {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        for (Runnable task : taskList){
            pool.execute(task);
        }
        //不会接收新任务,但已提交任务会执行完,最后关闭
        pool.shutdown();
    }

    /**
     * 使用Future并行执行多个任务并返回所有任务的结果
     * 例如同时发送多个http请求，一些请求的耗时分别是1秒，4秒，5秒，在线程池数量充足的情况，后台总耗时为5秒+handlerResultTask耗时，主线程阻塞5秒
     * 如果是主线程最后一行代码才执行这里，那么可以使用，否则推荐使用下面parallelFutureTask方法，使用CompletableFuture主线程无需任何等待
     * @param threadPool 线程池
     * @param futureTaskList 任务列表
     * @return 按传入的顺序依次返回每个任务的值
     */
    public static List<String> runFuture(ExecutorService threadPool, List<FutureTask<String>> futureTaskList){
        final long start = System.currentTimeMillis();
        List<String> result = new ArrayList(futureTaskList.size());
        for (FutureTask<String> futureTask : futureTaskList){
            threadPool.submit(futureTask);
        }
        for (FutureTask<String> futureTask : futureTaskList){
            try {
                //get会阻塞主线程等待结果执行完毕
                result.add(futureTask.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        threadPool.shutdown();
        log.info("runFuture耗时:{}秒",(System.currentTimeMillis()-start));
        return result;
    }


    /**
     * 使用CompletableFuture并行执行多个任务并处理所有任务的结果
     * 例如同时发送多个http请求，一些请求的耗时分别是1秒，4秒，5秒，在线程池数量充足的情况，后台总耗时为5秒+handlerResultTask耗时，且主线程无任何阻塞
     * 异步请求后的结果也用了另一个线程单独接收处理，注意这些任务执行顺序是随机的，当其中某个任务执行异常，剩下的任务仍然会执行完
     * 多个任务发送异常时，只会抛出第一个异常，不会打断主线程
     * @param threadPool 执行任务的线程池，一般用newFixedThreadPool,不能为null
     * @param taskList 所有要执行的任务，不能为null
     * @param handlerResultTask 处理所有结果的逻辑，map的key为taskList的索引，value为一一对应的任务结果值，可以为null
     * @param exceptionTask 发生异常后要处理的逻辑，如果执行这个就不会执行handlerResultTask，可以为null
     */
    public static void parallelFutureTask(ExecutorService threadPool,
                                            List<Supplier<String>> taskList,
                                            Consumer<Map<Integer, String>> handlerResultTask,
                                            Consumer<Throwable> exceptionTask){
        final long start = System.currentTimeMillis();
        ConcurrentHashMap<Integer, String> resultMap = new ConcurrentHashMap<>();
        List<CompletableFuture> completableFutureList = new ArrayList<>();
        if(null != threadPool && null != taskList){
            //对每个任务都单独分配线程执行
            for (int i = 0; i < taskList.size(); i++) {
                int finalI = i;
                completableFutureList.add(CompletableFuture.runAsync(() ->{
                    String res = taskList.get(finalI).get();
                    resultMap.put(finalI,res);
                },threadPool));
            }
            //单独分配线程等待所有任务完成
            CompletableFuture.runAsync(()->{
                //等待所有任务执行完成
                completableFutureList.forEach(item->{
                    item.join();
                });
            },threadPool).whenComplete((value,exception) -> {
                //即使发生异常也会走到这里
                //执行处理任务结果的逻辑
                if (exception == null && null != handlerResultTask) {
                    handlerResultTask.accept(resultMap);
                }
                log.info("parallelFutureTask耗时:{}秒",(System.currentTimeMillis()-start));
                threadPool.shutdown();
            }).exceptionally(exception -> {
                //发生异常，执行处理异常的逻辑
                if(null != exceptionTask){
                    exceptionTask.accept(exception);
                }else {
                    exception.printStackTrace();
                }
                //这个值是返回到当前completableFuture的值,通过completableFuture.get()阻塞主线程获得
                return null;
            });
        }
    }


    /**
     * 使用CompletableFuture串行执行某个大任务并处理所有步骤执行完的结果
     * 例如处理上下结果关联的复杂逻辑，某些步骤耗时1秒，4秒，5秒，后台总耗时为10秒+handlerResultTask耗时，但主线程无任何阻塞
     * 执行每一个步骤都是从线程池随机拿的，但由于需要等待上一步结果，所以耗时是累加的，前面的步骤发生异常时，后面的步骤不会被执行，
     * 发生异常不会打断主线程，相比直接在主线程串行执行逻辑，这里不会占用主线程时间，跟单独开一个线程其实差不多
     * 尽量不用使用new初始化CompletableFuture，因为官方说明new会创建一个不完美，没完成的CompletableFuture
     * 如果不指定线程池，默认使用ForkJoinPool.commonPool，但这是守护线程，如果主线程结束，则默认线程池自动关闭，此时可能并没有计算完成就会中断
     * @param threadPool 线程池，一般用newSingleThreadExecutor,不能为null
     * @param firstTask 第一个要执行的任务，需要返回值，不能为null
     * @param haveReturnTask 中间任务，每一个任务都接收上一个任务的返回值然后返回下一个值,中间有任务异常之后，剩下任务都不会执行，可以为null
     * @param handlerResultTask 最后一个要执行的任务，如果执行这个就不会执行exceptionTask，可以为null
     * @param exceptionTask 发生异常后最后要处理的任务，如果执行这个就不会执行handlerResultTask，可以为null
     */
    public static void serialFutureTask(ExecutorService threadPool,
                                            Supplier<String> firstTask,
                                            List<Function<String,String>> haveReturnTask,
                                            Consumer<String> handlerResultTask,
                                            Consumer<Throwable> exceptionTask){
        //thenRun执行的下一步不需要上一步的结果，但无返回值,thenAccept执行的下一步需要上一步的结果，但无返回值，thenApply执行的下一步需要上一步的结果，但有返回值
        //thenApply handle都是将异步串行化，即下一步计算需要依赖上一步的结果，但thenApply在其中一步出错时立即停止，handle会继续
        //thenApply调用显示“进行大量计算”的任务，这意味着您没有指定执行程序来运行此任务，并且系统可以自由使用任何执行程序，包括当前主线程，有时会阻塞主线程。
        //如果希望此作业在预定义的执行程序上执行，请使用thenApplyAsync
        final long start = System.currentTimeMillis();
        if(null != threadPool){
            CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> firstTask.get(),threadPool);
            if(haveReturnTask != null && haveReturnTask.size()>0){
                //thenApplyAsync是会阻塞的，串行执行中间任务
                for (Function<String,String> function : haveReturnTask){
                    completableFuture = completableFuture.thenApplyAsync(r-> function.apply(r),threadPool);
                }
            }
            completableFuture.whenCompleteAsync((value,exception) -> {
                //所有任务计算完成，执行最后的任务
                if (exception == null) {
                    handlerResultTask.accept(value);
                }
                log.info("serialFutureTask耗时:{}秒",(System.currentTimeMillis()-start));
                threadPool.shutdown();
            },threadPool).exceptionally(exception -> {
                //发生异常，且执行完成最后的任务后，执行处理异常的任务
                exceptionTask.accept(exception);
                //这个值是返回到当前completableFuture的值,通过completableFuture.get()阻塞主线程获得
                return null;
            });
        }
    }




    public static void main(String[] args) {
/*        List<Runnable> taskList = new ArrayList<>();
        taskList.add(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("111");
        });
        taskList.add(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("333");
        });
        taskList.add(()->{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("222");
        });
        taskList.add(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("555");
        });
        runNewFixedThreadPool(taskList, 5, new ThreadFactory() {
            private AtomicInteger atomicInteger = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"线程"+atomicInteger.getAndIncrement());
            }
        });*/

       // runFuture
/*        List<FutureTask<String>> futureTaskList = new ArrayList<>();
        futureTaskList.add(new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("第一个任务，应该返回1");
                Thread.sleep(1000);
                return "1";
            }
        }));
        futureTaskList.add(new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("第二个任务，应该返回2");
                Thread.sleep(1000);
                return "2";
            }
        }));
        futureTaskList.add(new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("第三个任务，应该返回3");
                Thread.sleep(5000);
                return "3";
            }
        }));
        List<String> res = runFuture(Executors.newFixedThreadPool(8),futureTaskList);
        for (int i = 0; i < res.size(); i++) {
            System.out.println("第"+(i+1)+"个任务返回值为："+res.get(i));
        }*/


        //parallelFutureTask
/*        List<Supplier<String>> haveReturnTask = new ArrayList<>();
        haveReturnTask.add(() -> {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName()+"执行第一个任务");
            int i = 10/0;
            return "res1";
        });
        haveReturnTask.add(() -> {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName()+"执行第二个任务");
            Integer a= null;
            Double.valueOf(a);
            return "res2";
        });
        haveReturnTask.add(() -> {
            try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName()+"执行第三个任务");
            return "res3";
        });

        Consumer<Map<Integer, String>> lastTask = new Consumer<Map<Integer, String>>() {
            @Override
            public void accept(Map<Integer, String> integerStringMap) {
                System.out.println("所有任务执行完成");
                for (int i = 0; i < integerStringMap.size(); i++) {
                    System.out.println("执行结果依次为："+integerStringMap.get(i));
                }
            }
        };
        Consumer<Throwable> exceptionTask = throwable -> {
            System.out.println(Thread.currentThread().getName()+"执行任务失败了");
            throwable.printStackTrace();
        };
        parallelFutureTask(Executors.newFixedThreadPool(5),haveReturnTask,lastTask,exceptionTask);*/

        //serialFutureTask
/*        Supplier<String> firstTask = () -> {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName()+"执行第一个任务");
            return "res1";
        };
        List<Function<String,String>> haveReturnTask = new ArrayList<>();
        haveReturnTask.add(s -> {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName()+"执行第二个任务，第一个任务的值："+s);
            return s+" res2";
        });
        haveReturnTask.add(s -> {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            //int i = 10/0;
            System.out.println(Thread.currentThread().getName()+"执行第三个任务，第二个任务的值："+s);
            return s+" res3";
        });
        Consumer<String> lastTask = s -> {
            System.out.println(Thread.currentThread().getName()+"执行最后的任务写入数据库，之前所有任务执行后的值："+s);
            try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
        };
        Consumer<Throwable> exceptionTask = throwable -> {
            System.out.println(Thread.currentThread().getName()+"执行任务失败了");
            throwable.printStackTrace();
        };
        serialFutureTask(Executors.newSingleThreadExecutor(),firstTask,haveReturnTask,lastTask,exceptionTask);*/


        System.out.println(Thread.currentThread().getName()+"----主线程先去忙其它任务");
        System.out.println(Thread.currentThread().getName()+"----写入数据库");
    }
}
