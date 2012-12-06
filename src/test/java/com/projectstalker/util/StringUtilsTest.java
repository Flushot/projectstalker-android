/**
 * PROPRIETARY/CONFIDENTIAL
 * Copyright (c) 2012 GrowYa, Inc.
 * All Rights Reserved.
 */
package com.projectstalker.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringUtilsTest {
    private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    @Test
    public void test_randomLetter() {
        Character letter1 = StringUtils.randomLetter();
        Character letter2 = StringUtils.randomLetter();

        assertNotNull(letter1);
        assertTrue(alphabet.contains(letter1.toString()));
        assertFalse(letter2.equals(letter1));
    }

    @Test
    public void test_randomLetters() {
        String letters1 = StringUtils.randomLetters(5);
        String letters2 = StringUtils.randomLetters(5);
        assertNotNull(letters1);
        assertTrue(letters1.length() == 5);
        assertFalse(letters1.equals(letters2));
    }

    @Test
    public void test_isBlank() {
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertTrue(StringUtils.isBlank("      "));
        assertTrue(StringUtils.isBlank(null));

        assertFalse(StringUtils.isBlank("a"));
        assertFalse(StringUtils.isBlank("abc"));
    }

    @Test
    public void test_uniqueChars() {
        String unique = StringUtils.uniqueChars("gaabbccdef");
        assertEquals("abcdefg", unique);
    }

    @Test
    public void test_sortString() {
        String sorted = StringUtils.sortString("chris");
        assertEquals("chirs", sorted);
    }

    @Test
    public void test_hasOnlyLetters() {
        // Valid
        assertTrue(StringUtils.hasOnlyLetters("abcdefghijklmnopqrstuvwxyz"));
        assertTrue(StringUtils.hasOnlyLetters("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));

        // Invalid
        String invalidChars = " `1234567890-=~!@#$%^&*()_+[]{}\\|;':,.<>/?";
        for (char c : invalidChars.toCharArray())
            assertFalse(StringUtils.hasOnlyLetters(String.format("abc%s", c)));

        assertFalse(StringUtils.hasOnlyLetters(null));
    }

    @Test
    public void test_filterLetters() {
        assertEquals("a", StringUtils.filterLetters("a"));
        assertEquals("", StringUtils.filterLetters("1"));
        assertEquals("abc", StringUtils.filterLetters("abc123"));
        assertEquals("abcd", StringUtils.filterLetters("a1 b2c3d4#"));
        assertEquals("ABCdef", StringUtils.filterLetters("AB#Cdef"));
        assertNull(StringUtils.filterLetters(null));
    }

    @Test
    public void test_countLetters() {
        final String letters = "abbcccddeeeef";
        int[] counts = StringUtils.countLetters(letters);
        assertEquals(1, counts[alphabet.indexOf('a')]);
        assertEquals(2, counts[alphabet.indexOf('b')]);
        assertEquals(3, counts[alphabet.indexOf('c')]);
        assertEquals(2, counts[alphabet.indexOf('d')]);
        assertEquals(4, counts[alphabet.indexOf('e')]);
        assertEquals(1, counts[alphabet.indexOf('f')]);
    }

    @Test
    public void test_left() {
        assertEquals("a", StringUtils.left("abcde", 1));
        assertEquals("ab", StringUtils.left("abcde", 2));
        assertEquals("a", StringUtils.left("a", 1));
        assertNull(StringUtils.left(null, 1));
    }

    @Test
    public void test_right() {
        assertEquals("e", StringUtils.right("abcde", 1));
        assertEquals("de", StringUtils.right("abcde", 2));
        assertEquals("e", StringUtils.right("e", 1));
        assertNull(StringUtils.right(null, 1));
    }
}
