package com.base.parsergen.rules;

public interface TypeRenamerI {

    public String rename(final String str);

    public static final TypeRenamerI DEFAULT_RENAMER = (String str) -> {
        return str;
    };

    static String underScoreConvert(final String name) {
        String preUnstructuredName = name.charAt(0) + "";
        {
            boolean prevIsUnderscore = false;
            for (int i = 1; i < name.length(); i++) {
                final String curChar = name.charAt(i) + "";
                if (curChar.equals("_")) {
                    prevIsUnderscore = true;
                } else {
                    preUnstructuredName += prevIsUnderscore ? curChar.toUpperCase() : curChar;
                    prevIsUnderscore = false;
                }
            }
        }
        return preUnstructuredName.replace("+", "Plus");
    }

    public static final TypeRenamerI MISC_CASE_UNDERSCORE_RENAMER = (name) -> {
        boolean prevIsLower = false;
        boolean prevIsUnderScore = false;

        String retString = "";
        for (char ch : name.toCharArray()) {
            final String chAsStr = ch + "";
            if (ch == '_') {
                prevIsUnderScore = true;
                prevIsLower = true;
                continue;
            }

            if (prevIsUnderScore) {
                retString += chAsStr.toUpperCase();
                prevIsUnderScore = false;
            } else if (prevIsLower) {
                // Allow preservation of existing camel case
                retString += chAsStr;
            } else {
                retString += chAsStr.toLowerCase();
            }
            prevIsLower = chAsStr.equals(chAsStr.toLowerCase());
        }
        return retString;
    };

    public static final TypeRenamerI UNDERSCORE_RENAMER = (String name) -> {
        return underScoreConvert(name);
    };

    public static final TypeRenamerI SPACE_RENAMER = (String name) -> {
        return underScoreConvert(name.replaceAll(" ", "_").replaceAll("%", "P"));
    };

}
