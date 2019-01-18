package com.cbase.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : zhouyx
 * @date : 2017/9/12
 * @description : 检查工具
 */
public class CheckUtils {

    /**
     * 判断两个string是否相等
     *
     * @param strObj0
     * @param strObj1
     * @return
     */
    public static boolean checkEquals(Object strObj0, Object strObj1) {
        String str0 = strObj0 + "";
        String str1 = strObj1 + "";
        return str0.equals(str1);
    }

    /**
     * 判断string是否为空
     *
     * @param strObj
     * @return
     */
    public static boolean isNull(Object strObj) {
        String str = strObj + "";
        if (!"".equals(str) && !"null".equals(str)) {
            return false;
        }
        return true;
    }

    /**
     * 检查邮箱
     *
     * @param strObj
     * @return
     */
    public static boolean checkEmail(Object strObj) {
        String str = strObj + "";
        String match = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern pattern = Pattern.compile(match);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 监测是否为正确的手机号码
     *
     * @param mobile
     * @return
     */
    public static boolean isMobileCorrect(String mobile) {
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17([6-8]|0))|(18[0-9]))\\d{8}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    /**
     * 判断是否为min到max位的字母或数字
     *
     * @param str
     * @param min
     * @param max
     * @return
     */
    public static boolean isAlphanumericRange(String str, int min, int max) {
        String regex = "^[a-z0-9A-Z]{" + min + "," + max + "}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 判断是否是字母加数字
     *
     * @param str
     * @return
     */
    public static boolean isAlphanumeric(String str) {
        Pattern p1 = Pattern.compile("[0-9]*$");
        Pattern p2 = Pattern.compile("^[A-Za-z]+$");
        if (p1.matcher(str).matches() || p2.matcher(str).matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否为n为数字的验证码
     *
     * @param str
     * @param n
     * @return
     */
    public static boolean isVerificationCode(String str, int n) {
        String regex = "^[0-9]{" + n + "}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 检查str的长度是否达到要求
     *
     * @param strObj
     * @param length
     * @return
     */
    public static boolean checkLength(Object strObj, int length) {
        String str = strObj + "";
        return str.length() >= length;
    }

    /**
     * 检查字符串的长度
     *
     * @param strObj
     * @param length
     * @return
     */
    public static boolean checkLengthEquals(Object strObj, int length) {
        String str = strObj + "";
        return str.length() == length;
    }

    /**
     * 检查字符串长度范围
     *
     * @param strObj
     * @param start
     * @param end
     * @return
     */
    public static boolean checkLength(Object strObj, int start, int end) {
        String str = strObj + "";
        return str.length() >= start && str.length() <= end;
    }

    /**
     * 检查是否url
     *
     * @param url
     * @return
     */
    public static boolean checkUrl(String url) {
        String regular = "(http|https)://[\\S]*";
        return Pattern.matches(regular, url);
    }

    /**
     * 检查15位身份证
     *
     * @param idCard
     * @return
     */
    public static boolean checkIdCard15(String idCard) {
        String regular = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
        return Pattern.matches(regular, idCard);
    }

    /**
     * 检查18位身份证
     *
     * @param idCard
     * @return
     */
    public static boolean checkIdCard18(String idCard) {
        String regular = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
        return Pattern.matches(regular, idCard);
    }

    /**
     * 字符是否是数字
     *
     * @param chr
     * @return
     */
    public static boolean isNumber(char chr) {
        if (chr < 48 || chr > 57) {
            return false;
        }
        return false;
    }

    /**
     * 字符串是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        String regular = "[0-9]+$";
        return Pattern.matches(regular, str);
    }

    /**
     * 字符串是否是字母
     *
     * @return
     */
    public static boolean isLetter(String str) {
        String regular = "[A-Za-z]+$";
        return Pattern.matches(regular, str);
    }

    /**
     * 检查是否合法金额
     *
     * @param money
     * @return
     */
    public static boolean isMoney(String money) {
        String regular = "^(([1-9]\\d*)|([0]))(\\.(\\d){0,2})?$";
        return Pattern.matches(regular, money);
    }

}
