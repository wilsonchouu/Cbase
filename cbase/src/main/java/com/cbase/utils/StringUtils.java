package com.cbase.utils;

import java.math.RoundingMode;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author : zhouyx
 * @date : 2017/9/13
 * @description : 字符串工具
 */
public class StringUtils {

    private static boolean isEmpty(Object obj) {
        if (obj == null || "".equals(obj) || "null".equals(obj)) {
            return true;
        }
        return false;
    }

    /**
     * 转换成字符串
     *
     * @param obj
     * @return
     */
    public static String toString(Object obj) {
        if (isEmpty(obj)) {
            return "";
        } else {
            return obj.toString();
        }
    }

    /**
     * 转换成整型
     *
     * @param obj
     * @return
     */
    public static int toInt(Object obj) {
        if (isEmpty(obj)) {
            return 0;
        } else {
            try {
                return Integer.parseInt(obj.toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    /**
     * 转换成短整型
     *
     * @param obj
     * @return
     */
    public static short toShort(Object obj) {
        if (isEmpty(obj)) {
            return 0;
        } else {
            try {
                return Short.parseShort(obj.toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    /**
     * 转换成浮点型
     *
     * @param obj
     * @return
     */
    public static float toFloat(Object obj) {
        if (isEmpty(obj)) {
            return 0;
        } else {
            try {
                return Float.parseFloat(obj.toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    /**
     * 转换成双精度浮点型
     *
     * @param obj
     * @return
     */
    public static double toDouble(Object obj) {
        if (isEmpty(obj)) {
            return 0;
        } else {
            try {
                return Double.parseDouble(obj.toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    /**
     * 转换成长整型
     *
     * @param obj
     * @return
     */
    public static long toLong(Object obj) {
        if (isEmpty(obj)) {
            return 0;
        } else {
            try {
                return Long.parseLong(obj.toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    /**
     * 转换成布尔类型
     *
     * @param obj
     * @return
     */
    public static Boolean toBoolean(Object obj) {
        if (isEmpty(obj)) {
            return false;
        }
        return Boolean.valueOf(obj.toString());
    }

    /**
     * 将半角字符转换为全角字符
     *
     * @param input
     * @return
     */
    public static String toSBC(String input) {
        char[] chr = input.toCharArray();
        for (int i = 0; i < chr.length; i++) {
            if (chr[i] == ' ') {
                chr[i] = '\u3000';
            } else if (chr[i] < '\177') {
                chr[i] = (char) (chr[i] + 65248);
            }
        }
        return new String(chr);
    }

    /**
     * 转换数字月份转为中文
     *
     * @param monthStr
     * @return
     */
    public static String convertToChineseMonth(String monthStr) {
        int month = toInt(monthStr);
        String[] strArr = {"", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
        if (month > 0 && month < strArr.length) {
            return strArr[month];
        }
        return strArr[0];
    }

    /**
     * 转换数字月份转为英文
     *
     * @param monthStr
     * @return
     */
    public static String convertToEnglishMonth(String monthStr) {
        int month = toInt(monthStr);
        String[] strArr = {"", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        if (month > 0 && month < strArr.length) {
            return strArr[month];
        }
        return strArr[0];
    }

    /**
     * 获取字符的长度（中文算2，英文算1）
     *
     * @param value
     * @return
     */
    public static int getStringLength(String value) {
        int length = 0;
        String chinese = "[\u4e00-\u9fa5]";
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < value.length(); i++) {
            // 获取一个字符
            String temp = value.substring(i, i + 1);
            // 判断是否为中文字符
            if (temp.matches(chinese)) {
                // 中文字符长度为1
                length += 2;
            } else {
                // 其他字符长度为0.5
                length += 1;
            }
        }
        return length;
    }

    /**
     * byte数组转换成十六进制字符串
     *
     * @param bytes
     * @param length
     * @return
     */
    public static String byteToHexString(byte[] bytes, int length) {
        if (bytes == null || length <= 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }

    /**
     * 将手机号中间4位变为****
     * 注：只适用于11位号码
     *
     * @param phone
     * @return
     */
    public static String phoneToUnknown(String phone) {
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length());
    }

    /**
     * 将数据的后length位置為****
     *
     * @param data
     * @param length
     * @return
     */
    public static String lastToUnknown(String data, int length) {
        int count = data.length();
        StringBuilder builder = new StringBuilder();
        if (count > length) {
            for (int i = 0; i < length; i++) {
                builder.append("*");
            }
            return data.substring(0, count - length) + builder.toString();
        } else {
            return data;
        }
    }

    /**
     * 将数据的前length位置为****
     *
     * @param data
     * @param length
     * @return
     */
    public static String firstToUnknow(String data, int length) {
        int count = data.length();
        StringBuilder builder = new StringBuilder();
        if (count > length) {
            for (int i = 0; i < length; i++) {
                builder.append("*");
            }
            return builder.toString() + data.substring(length, count);
        } else {
            return data;
        }
    }

    /**
     * MD5加密
     *
     * @param str
     * @return
     */
    public static String MD5(String str) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] bytes = str.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            messageDigest.update(bytes);
            // 获得密文
            byte[] digest = messageDigest.digest();
            // 把密文转换成十六进制的字符串形式
            int j = digest.length;
            char[] chs = new char[j * 2];
            int k = 0;
            for (byte byte0 : digest) {
                chs[k++] = hexDigits[byte0 >>> 4 & 0xf];
                chs[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(chs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 金钱数据格式化
     *
     * @param amount
     * @return
     */
    public static String formatMoney(double amount) {
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(2);
        format.setGroupingSize(0);
        format.setRoundingMode(RoundingMode.FLOOR);
        // 定义要显示的数字的格式
        String style = "#,##0.00";
        format.applyPattern(style);
        return format.format(amount);
    }

    /**
     * 将double类型的数据改为百分数
     *
     * @param number
     * @return
     */
    public static String formatPercent(double number, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMinimumFractionDigits(scale);
        format.setRoundingMode(RoundingMode.FLOOR);
        return format.format(number);
    }

    /**
     * 将double类型的数据改为百分数，默认显示两位小数
     *
     * @param number
     * @return
     */
    public static String formatPercent(double number) {
        return formatPercent(number, 2);
    }

}
