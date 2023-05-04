package com.kcx.common.utils.regular;


import org.apache.commons.lang3.StringUtils;

/**
 * 正则表达式 校验
 */
public class RegUtils {
    /**
     * 正负整数
     */
    public static final String NumberInteger = "^(\\-|\\+)?[1-9]\\d*$";
    /**
     * 正整数
     */
    public static final String NumberPositiveInteger = "^[1-9]\\d*$";
    /**
     * 负整数
     */
    public static final String NumberNegativeInteger = "^\\-[1-9]\\d*$";
    /**
     * 0-9开头的正整数
     */
    public static final String Number09Integer  = "^[0-9]\\d*$";
    /**
     * 正负小数
     */
    public static final String NumberDecimal = "^(\\-|\\+)?\\d+(\\.\\d+)?$";
    /**
     * 正小数
     */
    public static final String NumberPositiveDecimal = "^[1-9]\\d+(\\.\\d+)?$";
    /**
     * 负小数
     */
    public static final String NumberNegativeDecimal = "^\\-[1-9]\\d+(\\.\\d+)?$";
    /**
     * 最多小数点后两位的正小数
     */
    public static final String NumberPositiveDecimal2 = "^([1-9]\\d*)+(\\.[0-9]{1,2})?$";
    /**
     * 手机号 11位
     */
    public static final String Phone ="^1[0-9]{10}$";
    /**
     * 国内电话号码0511-4405224
     */
    public static final String Phone2 ="^\\d{3}-\\d{8}|\\d{4}-\\d{7}$";
    /**
     * 密码，数字，字母，特殊字符，6-18位
     */
    public static final String Password ="^([0-9]|\\w|[\\x21-\\x7e]){6,18}$";
    /**
     * 身份证 15-18位
     */
    public static final String IdCard = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
    /**
     * 邮箱匹配
     */
    public static final String Email = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * 只能为汉字
     */
    public static final String ChineseCharacter = "^[\\u4e00-\\u9fa5]{0,}$";
    /**
     * 域名
     */
    public static final String DomainName = "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?";
    /**
     * 金额 (不能为0) 小数点后两位 0.01 0.1 0.10 1.0 1.01 1
     */
    public static final String Money = "^((0(\\.[0][1-9]))|(0(\\.[1-9][0-9]?))|(([1-9][0-9]*)+(\\.[0-9]{1,2})?))$";
    /**
     * xml文件
     */
    public static final String XmlFile = "^([a-zA-Z]+-?)+[a-zA-Z0-9]+\\.[x|X][m|M][l|L]$";
    /**
     * 输入的是否是一个html标记，<div>404错误!</div>
     */
    public static final String HtmlTag = "^<(\\S*?)[^>]*>.*?|<.*? />$";
    /**
     * 日期格式，2021-01-02
     */
    public static final String Date = "^\\d{4}-\\d{1,2}-\\d{1,2}$";
    /**
     * 日期格式，2021-01-02 13:17:09
     */
    public static final String DateTime = "^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$";
    /**
     * url,https://c.runoob.com/front-end/854/
     */
    public static final String Url = "^(https?|ftp):\\/\\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\\.)*[a-zA-Z0-9-]+\\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\\/($|[a-zA-Z0-9.,?'\\\\+&%$#=~_-]+))*$";

    /**
     * 验证字符串是否符合指定的正则表达式
     * @param str 字符串
     * @param regx 正则表达式
     * @return
     */
    public static boolean verifyRegx(String str,String regx) {
        if (StringUtils.isBlank(str)) {
            return false;
        } else if (!str.matches(regx)) {
            return false;
        }
        return true;
    }

    /**
     * 验证字符串是否以指定的字符串结尾
     * @param str 字符串
     * @param match 匹配的字符串
     * @return
     */
    public static boolean verifyStringEnd(String str,String match) {
        if (StringUtils.isBlank(str)) {
            return false;
        } else if (!str.matches(".*(?:"+match+")$")) {
            return false;
        }
        return true;
    }


    /**
     * 验证字符串是否以指定的字符串开头
     * @param str 字符串
     * @param match 匹配的字符串
     * @return
     */
    public static boolean verifyStringStart(String str,String match) {
        if (StringUtils.isBlank(str)) {
            return false;
        } else if (!str.matches("^(?:"+match+").*")) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(verifyStringEnd("是是是.ts",".ts"));
        System.out.println(verifyStringEnd("是是是.MP4",".ts"));
        System.out.println(verifyStringStart("是是是.ts","是是是"));
        System.out.println(verifyStringStart("aa是是是.ts","是是是"));
        System.out.println(verifyRegx("188aq~\\",RegUtils.Password));
    }
}
