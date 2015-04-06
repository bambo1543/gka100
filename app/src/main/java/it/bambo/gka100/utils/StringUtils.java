package it.bambo.gka100.utils;

/**
 * @author <a href="mailto:andreas.bga@gmail.com">Andreas Baumgartner</a> on 03.04.15.
 */
public class StringUtils {

    public static boolean isEmpty(String string) {
        return string == null || string.trim().equals("");
    }

    public static String cutEnd(String string, int count) {
        return string.substring(0, string.length() - count);
    }

}
