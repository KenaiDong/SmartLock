package com.xuzd.commons.utils;

import com.google.common.base.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static boolean isEmpty(String str) {
        return (str == null || "".equals(str));
    }

    public static boolean isNotEmpty(String str) {
        return (str != null && !"".equals(str));
    }

    public static boolean isStrictEmpty(String str) {
        return (str == null || "".equals(str.trim()));
    }

    public static boolean isStrictNotEmpty(String str) {
        return (str != null && !"".equals(str.trim()));
    }

    /**
     * @param strings
     * @return 返回第一个不为空的字符串，如果都为空，则返回null
     */
    public static String nullOr(String... strings){
        for (String str : strings) {
            if(!Strings.isNullOrEmpty(str)){
                return str;
            }
        }
        return null;
    }


    /**
     * 判断 sources 中是否包含 target
     * @param target
     * @param sources 不能为null或者“”,否则无论如何都返回true
     * @return
     */
    public static boolean contains(String target, String sources) {
        if (isStrictEmpty(sources)) {
            return true;
        }
        String[] array = sources.split(",");
        for (int i = 0; i < array.length; i++) {
            if (target.equals(array[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 在特殊字符前加"/"
     * @param origin
     * @return
     */
    public static String repSpeCharacter(String origin) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(origin);
        if (!m.find()) {
            return origin;
        }
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < origin.length(); i++) {
            String str = origin.substring(i, i + 1);
            m = p.matcher(str);
            if (m.find()) {
                str = "\\" + str;
            }
            result.append(str);
        }
        return result.toString();
    }

    /**
     * 删除字符串最后一位
     * @return
     */
    public static String delLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    /**
     * <p>判断字符串是否为整数</p>
     * <pre>
     *     isInteger(null) = false
     *     isInteger("") = false
     * </pre>
     *
     * @param str
     * @return 是整数--true; 不是整数--false
     */
    public static boolean isInteger(String str){
        if (str == null || str.length() == 0) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    private static boolean endsWith(CharSequence str, CharSequence suffix, boolean ignoreCase) {
        if (str != null && suffix != null) {
            if (suffix.length() > str.length()) {
                return false;
            } else {
                int strOffset = str.length() - suffix.length();
                return StringUtils.regionMatches(str, ignoreCase, strOffset, suffix, 0, suffix.length());
            }
        } else {
            return str == null && suffix == null;
        }
    }

    public static boolean regionMatches(CharSequence cs, boolean ignoreCase, int thisStart, CharSequence substring, int start, int length) {
        if (cs instanceof String && substring instanceof String) {
            return ((String)cs).regionMatches(ignoreCase, thisStart, (String)substring, start, length);
        } else {
            int index1 = thisStart;
            int index2 = start;
            int tmpLen = length;
            int srcLen = cs.length() - thisStart;
            int otherLen = substring.length() - start;
            if (thisStart >= 0 && start >= 0 && length >= 0) {
                if (srcLen >= length && otherLen >= length) {
                    while(tmpLen-- > 0) {
                        char c1 = cs.charAt(index1++);
                        char c2 = substring.charAt(index2++);
                        if (c1 != c2) {
                            if (!ignoreCase) {
                                return false;
                            }

                            if (Character.toUpperCase(c1) != Character.toUpperCase(c2) && Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                                return false;
                            }
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public static boolean endsWith(CharSequence str, CharSequence suffix) {
        return endsWith(str, suffix, false);
    }

    public static boolean endsWithIgnoreCase(CharSequence str, CharSequence suffix) {
        return endsWith(str, suffix, true);
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }


}
