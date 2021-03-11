package com.cheatbreaker.client.ui.overlay;

public class StringSanitizer {

    public static final char[] lIIIIlIIllIIlIIlIIIlIIllI = new char[]{'/', '\n', '\r', '\t', '\'', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};

    public static boolean isValid(char c) {
        return c != '§' && c >= ' ' && c != '';
    }

    public static String sanitize(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : string.toCharArray()) {
            if (!StringSanitizer.isValid(c)) continue;
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

}
