/**
 * PROPRIETARY/CONFIDENTIAL
 * Copyright (c) 2012 GrowYa, Inc.
 * All Rights Reserved.
 */
package com.projectstalker.util;

import java.util.*;

/**
 * String utilities
 */
public class StringUtils {
    private static final Random random = new Random();

    private StringUtils() {
    }

    public static char randomLetter() {
        return (char)(random.nextInt('z' - 'a' + 1) + 'a');
    }

    public static String randomLetters(int count) {
        StringBuilder randomLetters = new StringBuilder();
        for (int i = 0; i < count; ++i)
            randomLetters.append(randomLetter());
        return randomLetters.toString();
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * Get unique set of letters used in string s.
     * <p/>
     * e.g. uniqueLetters("aabbcde") == "abcde"
     *
     * @param s the string to get unique letters from.
     * @return sorted string with unique letters used.
     */
    public static String uniqueChars(String s) {
        Set<Character> uniq = new HashSet<Character>();
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (!uniq.contains(c))
                sb.append(c);
            uniq.add(c);
        }

        return sortString(sb.toString());
    }

    /**
     * Sorts a string alphabetically.
     * <p/>
     * e.g. sortString("chris") == "chirs"
     *
     * @param s the string to sort.
     * @return sorted string.
     */
    public static String sortString(String s) {
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    /**
     * Does a word have only letters?
     *
     * @param word the word to check.
     * @return whether or not the word contains only letters.
     */
    public static boolean hasOnlyLetters(String word) {
        if (word == null)
            return false;

        for (char letter : word.toLowerCase(Locale.ENGLISH).toCharArray()) {
            if (!Character.isLetter(letter))
                return false;
        }

        return true;
    }

    /**
     * Filters everything out except letters in given string.
     *
     * @param s the string to filter.
     * @return the string containing only letters.
     */
    public static String filterLetters(String s) {
        if (s == null)
            return null;

        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isLetter(c))
                sb.append(c);
        }

        return sb.toString();
    }

    public static int[] countLetters(String s) {
        s = s.toLowerCase(Locale.ENGLISH);
        int[] ret = new int[26];
        for (int i = 0; i < s.length(); ++i) {
            int c = s.charAt(i) - 96 - 1;
            if (c > -1 && c < 26)
                ++ret[c];
        }
        return ret;
    }

    public static String left(String s, int index) {
        if (s == null)
            return null;

        return s.substring(0, index);
    }

    public static String right(String s, int index) {
        if (s == null)
            return null;

        return s.substring(s.length() - index);
    }

    /**
     * Converts a binary blob to a hexadecimal string.
     *
     * @param binaryData binary blob to convert to hex.
     * @return lower case hexadecimal string.
     */
    public static String hexString(byte[] binaryData) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0, l = binaryData.length; i < l; i++) {
            int intRep = binaryData[i] & 0xFF;
            if (intRep < 0x10)
                hexString.append('\u0030');
            hexString.append(Integer.toHexString(intRep));
        }
        return hexString.toString();
    }
}
