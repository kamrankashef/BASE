package com.base.files.common;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class ServiceUtil {

    private static final int GUID_LENGTH = 10;

    static private final String[] CHAR_MAPPINGS = {"0", "1", "2", "3", "4", "5", "6",
        "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
        "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
        "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
        "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
        "X", "Y", "Z"};

    static private final int CHAR_SET_SIZE = CHAR_MAPPINGS.length;

    public static String getGuid() {
        return ServiceUtil.getGuid(GUID_LENGTH);
    }

    public static String getGuid(final int length) {

        final Random rand = new Random();
        final StringBuilder builder = new StringBuilder();

        while (builder.length() < length) {
            builder.append(CHAR_MAPPINGS[rand.nextInt(CHAR_SET_SIZE)]);
        }
        return builder.toString();
    }

    public static String getUUID() {

        return java.util.UUID.randomUUID().toString();
    }

    public static Set<String> extractAttributeName(final Attributes attributes) {
        final Set<String> set = new HashSet<>();
        for (Attribute attr : attributes) {
            set.add(attr.getKey());
        }
        return set;
    }

}
