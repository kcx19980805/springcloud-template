package com.kcx.common.utils.captcha;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

/**
 * 验证码工具类
 */
public class CaptchaUtils {

    private static int width = 100;// 定义图片的width
    private static int height = 40;// 定义图片的height
    private static int codeCount = 4;// 定义图片上显示验证码的个数
    private static int codeX = 18;
    private static  int codeY = 27;
    private static char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R','T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '6', '7', '8', '9' };
    private static String[] fontNames  = {"宋体", "华文楷体", "黑体", "微软雅黑", "楷体_GB2312"};

    /**
     * 生成验证码图片输出到浏览器
     * @param response 浏览器响应对象
     * @param callback 执行的具体业务，入参是验证码，返回是业务执行结果
     */
    public static void generateCodeAndPicToWeb(HttpServletResponse response, Function<String,Boolean> callback){
        try {
            Map<String,Object> map = generateCodeAndPic();
            String code = map.get("code").toString();
            //先将验证码传给具体业务执行，如保存到数据库或缓存，业务执行成功再继续
            boolean businessResult = callback.apply(code);
            if(businessResult){
                response.setContentType("text/html;charset=utf-8");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Cache-Control", "no-cache");
                response.setDateHeader("Expires", -1);
                response.setContentType("image/jpeg");
                ImageIO.write((RenderedImage) map.get("codePic"), "jpeg", response.getOutputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 生成验证码图片保存在本地文件
     * @param pathName 文件全路径
     * @return 验证码
     */
    public static String generateCodeAndPicToFile(String pathName){
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(pathName));
            Map<String,Object> map = generateCodeAndPic();
            ImageIO.write((RenderedImage) map.get("codePic"), "jpeg", bufferedOutputStream);
            return map.get("code").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != bufferedOutputStream){
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    //忽略
                }
            }
        }
        return null;
    }

    /**
     * 生成验证码和图片
     * @return code验证码 codePic验证码图片
     */
    public static Map<String,Object> generateCodeAndPic() {
        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // Graphics2D gd = buffImg.createGraphics();
        // Graphics2D gd = (Graphics2D) buffImg.getGraphics();
        Graphics gd = buffImg.getGraphics();
        // 创建一个随机数生成器类
        Random random = new Random();
        // 将图像填充为白色
        gd.setColor(Color.WHITE);
        gd.fillRect(0, 0, width, height);
        // 设置字体。
        gd.setFont(randomFont());
        // 画边框。
        gd.setColor(Color.BLACK);
        gd.drawRect(0, 0, width - 1, height - 1);
        // 随机产生40条干扰线，使图象中的认证码不易被其它程序探测到。
        gd.setColor(Color.BLACK);
        for (int i = 0; i < 30; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            gd.drawLine(x, y, x + xl, y + yl);
        }
        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();
        int red = 0, green = 0, blue = 0;
        // 随机产生codeCount数字的验证码。
        for (int i = 0; i < codeCount; i++) {
            // 得到随机产生的验证码数字。
            String code = String.valueOf(codeSequence[random.nextInt(29)]);
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            // 用随机产生的颜色将验证码绘制到图像中。
            gd.setColor(new Color(red, green, blue));
            gd.drawString(code, (i + 1) * codeX, codeY);
            // 将产生的四个随机数组合在一起。
            randomCode.append(code);
        }
        Map<String,Object> map  =new HashMap<String,Object>();
        //存放验证码
        map.put("code", randomCode);
        //存放生成的验证码BufferedImage对象
        map.put("codePic", buffImg);
        return map;
    }

    /**
     * 生成随机字体
     * @return
     */
    private static Font randomFont() {
        Random r = new Random();
        int index = r.nextInt(fontNames.length);
        String fontName = fontNames[index];
        int style = r.nextInt(4);  //生成随机样式，0:无样式，1:粗体，2:斜体，3:粗体+斜体
        int size = r.nextInt(5) + 24; //生成随机字号
        return new Font(fontName, style, size);
    }

    /**
     * 要测试先注释掉包含HttpServletResponse导出的方法
     */
    public static void main(String[] args) {
        System.out.println(generateCodeAndPicToFile("D:\\验证码.jpg"));
    }
}
