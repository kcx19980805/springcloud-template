## 项目简介

一个企业级的分布式项目后端框架，整个项目和配置都是完整的，业务只有简单的示例，可以直接在上面搭建自己的业务

## 项目技术栈

springcloud + springcloudalibaba + swagger +mybatisplus + mysql8.0 +xxl-job

## 项目整体架构图

计划设计如下，目前暂时只搭建了后端

整个分布式框架协调调用，动态数据源，分布式事务等基本实现。

![image-20230503235802185]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230503235802185.png)





## 项目启动

先将5个数据库导入mysql8.0中

![image-20230504113139727]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230504113139727.png)

配置nacos数据库连接

![image-20230504105340452]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230504105340452.png)

配置seata数据库连接

![image-20230504105520261]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230504105520261.png)

配置seata连接nacos地址

![image-20230504105618236]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230504105618236.png)

然后在“组件”目录下cmd，回车打开命令行

![image-20230504105737031]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230504105737031.png)

开多个窗口，依次执行启动nacos，zipkin,sentinel,seata服务命令

![image-20230504105807862]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230504105807862.png)

![image-20230504105919765]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230504105919765.png)

除了xxl-job-admin是从xxl-job官网拉下来的项目，不用修改，其它项目都是业务项目，xxl-job-admin是服务端，具体调用的分布式任务在job中

另外common包中提供了支付，视频流，excel，文件等处理工具类

![image-20230504110522560]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230504110522560.png)



然后自己修改除common,xxl-job-admin以外的8个项目的dev,prod,test配置文件，包括mysql，nacos，sentinel，redis，zipkin等

![image-20230504110722400]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230504110722400.png)



然后注意修改xxl-job-admin的数据库连接

![image-20230504112935330]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230504112935330.png)

## 分布式调用示例

### 微服务网关gateway

通过网关访问api-admin的swagger成功http://localhost:8897/api-admin/

![image-20230504111543675]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230504111543675.png)

### 服务调用openfeign

代码中，接口http://localhost:8897/api-admin/user/login实现了feign

![image-20230504111857359]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230504111857359.png)

### 注册中心nacos

启动所有项目，注册中心显示如下：

![image-20230504111054527]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230504111054527.png)

### 链路追踪sleuth+zipkin

启动所有项目，访问swagger，此接口发送一个请求从：

```
api-admin项目的/user/login调用api-user项目的dict/list

api-user项目的dict/list调用job项目的job/getUserInfo

job项目的job/getUserInfo调用service-middleware项目的 /middleware/updateNickName

service-middleware项目的/middleware/updateNickName 调用service-third项目的third/updateNickName

service-third项目的third/updateNickName调用task-producer的taskProducer/updateNickName

task-producer的taskProducer/updateNickName调用task-consumer的taskConsumer/updateNickName
```

![image-20230503145923171]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230503145923171.png)



在链路追踪中可以看见

![image-20230503150029285]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230503150029285.png)

如果发生异常

```
    @Override
    public ResPageDataVO<ResSysDictDataListVO> list(ReqSysDictDataListVO req) {
        int a = 10/0;
        ReqClientUserVO reqClientUserVO = new ReqClientUserVO();
        reqClientUserVO.setUserId("1");
        Result<ResClientUserInfoVO> userInfo = jobFeign.getUserInfo(reqClientUserVO);
        log.info("远程调用job结果："+userInfo);
        return PageUtils.autoPageData(()->baseMapper.listTotal(req),()->baseMapper.list(req),req);
    }
```

调用链路为

![image-20230503151926873]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230503151926873.png)

通过网关请求此接口效果一样，网关不能加zipkin，所以没有显示 http://localhost:8897/api-admin/user/login

点进详情可以看见具体的某个请求执行时间

![image-20230503152936995]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230503152936995.png)

在sentinel中可以看见实时监控的请求

![image-20230503153645937]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230503153645937.png)

### 服务限流sentinel

为此接口添加限流

![image-20230503155102921]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230503155102921.png)

通过网关快速访问此接口后，即1秒内多次请求超过1qps,则直接报错

![image-20230503155132955]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230503155132955.png)



限流持久化，自己配置保存在nacos数据库中

![image-20230504091649468]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230504091649468.png)

项目中配置读取

![image-20230504091725310]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230504091725310.png)

### 服务熔断sentinel

为此接口添加熔断

![image-20230503160337197]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230503160337197.png)

此时如果在5秒中内10个请求有1个的响应时间超过了50ms，则熔断5秒，5秒内该接口都报如下错误

![image-20230503160524268]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230503160524268.png)

添加指定注解@SentinelResource，可以将限流返回结果自定义

```
    @PostMapping("user/login")
    @ApiOperation(value = "管理员登录")
    @SentinelResource
    public Result userLogin(@RequestBody @Validated ReqUserLoginVO req) {
        return Result.success(sysUserService.userLogin(req));
    }
```

由于在全局异常拦截处理了所以限流友好提示如下

![image-20230503181521340]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230503181521340.png)

### 分布式事务Seata

在示例http://localhost:8893/middleware/updateNickName接口中，测试分布式事务，方法中在当前项目修改了一次，任何调用第二个项目修改，当正常时日子显示PhaseTwo_Committed，注意@GlobalTransactional(rollbackFor = Exception.class)只能在发生异常时回滚所有当前和远程执行的sql，如果封装了异常返回结果，要手动抛出异常

![image-20230503235415954]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230503235415954.png)当发生异常时能正常回滚，注意在多层调用中发生异常，也只用在第一层添加注解

![image-20230503235127271]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230503235127271.png)

### 分布式任务调度xxl-job

访问管理端网址，贰胖子执行器和任务

![image-20230504112654519]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230504112654519.png)

示例代码执行

![image-20230504113435133]( https://kcxbucket.oss-cn-shenzhen.aliyuncs.com/springcloud-template/images/image-20230504113435133.png)

