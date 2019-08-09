package base.util;

import com.google.common.base.CaseFormat;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CaseConversion {

    private final static Set<String> RESERVED_WORDS = new HashSet<>();

    static {
        RESERVED_WORDS.add("order");
        RESERVED_WORDS.add("to");
        RESERVED_WORDS.add("from");
        // RESERVED_WORDS.add("type");
        RESERVED_WORDS.add("sort");
        RESERVED_WORDS.add("by");
        RESERVED_WORDS.add("desc");
    }

    public static boolean isDigit(final String str) {
        for (int i = 0; i < 10; i++) {
            if (("" + i).equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isUpper(final String str) {
        return str.equals(str.toUpperCase()) && !str.toUpperCase().equals(str.toLowerCase());
    }

    private final static String[] javaReservedWord = {"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends", "false", "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void", "volatile", "while"};
    private final static String[] baseVariableName = {"ps", "rs", "string", "double", "int", "integer", "long"};
    private final static Set<String> illegalJavaVarName = new HashSet<>();

    static {
        // this is affecting db names
//        illegalJavaVarName.addAll(Arrays.asList(javaReservedWord));
        illegalJavaVarName.addAll(Arrays.asList(baseVariableName));
    }

    public static String toJavaVariableName(final String name) {

        final String unstructuredName = name.replaceAll(":", "").replaceAll("-", "");
        if (unstructuredName.toUpperCase().equals(unstructuredName)) {
            // Not sure what the logic here is
            final String candidateName = unstructuredName.toLowerCase();
            return unstructuredName.toLowerCase();
        }

        final StringBuilder bldr = new StringBuilder();

        boolean lastIsUpper = true;
        boolean lastSetAsUpper = true;
        for (int i = 0; i < unstructuredName.length(); i++) {

            final String thisChar = unstructuredName.charAt(i) + "";
            boolean currIsUpper = isUpper(thisChar);

            if (i == 0) {
                bldr.append(thisChar.toLowerCase()); // First character always lower
            } else if (!currIsUpper) {
                bldr.append(thisChar); // Lower case stays lower
            } else if (currIsUpper && !lastIsUpper && i == unstructuredName.length() - 1) {
                bldr.append(thisChar.toUpperCase());
            } else if (unstructuredName.length() > i + 1) {
                // Look ahead, 
                final String nextChar = unstructuredName.charAt(i + 1) + "";
                boolean nextIsUpper = isUpper(nextChar) || isDigit(nextChar);

                // TODO, check if all of this code is reachable
                if (!lastIsUpper && !lastSetAsUpper) {
                    // Last was a natual lower
                    bldr.append(thisChar);
                } else if (!lastSetAsUpper && lastIsUpper) {
                    // Upper was suppresed
                    if (nextIsUpper) {
                        // More uppers coming, go to lower
                        bldr.append(thisChar.toLowerCase());
                    } else {
                        // Preceeding lower
                        bldr.append(thisChar.toUpperCase());
                    }
                } else if (nextIsUpper) {
                    bldr.append(thisChar.toLowerCase());
                } else if (!lastIsUpper || !nextIsUpper) {
                    bldr.append(thisChar.toUpperCase());
                } else {
                    bldr.append(thisChar.toLowerCase());
                }
            } else {
                bldr.append(thisChar.toLowerCase());
            }
            lastIsUpper = currIsUpper;
            String lastSetChar = "" + bldr.charAt(bldr.length() - 1);
            lastSetAsUpper = isUpper(lastSetChar);
        }
        final String candidateName = bldr.toString();
        return candidateName;
    }

    final public static String javaClassNameToDBTableName(final String javaClassName) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, javaClassName);
    }

    public final static String toDBName(final String name) {

        // Hack so Names that end in "_<char>" are property converted
        final String tempName = name + "Xyx";

        final String tempRowName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, CaseConversion.toJavaClassName(tempName));
        String rowName = tempRowName.substring(0, tempRowName.length() - 4);

        if (RESERVED_WORDS.contains(rowName.toLowerCase())) {
            rowName = "_" + rowName;
        }
        if (rowName.endsWith("aug") && !rowName.endsWith("_aug")) {
            return rowName.substring(0, rowName.length() - 3) + "_aug";
        }

        return rowName;

    }

    // Take PK_Event to (class: PKEvent, db_name: pk_event, varName: pKEvent);
    public static String bustedUpperCamelUnderScore(final String busted) {
        boolean lastCap = false;
        String name = "";

        for (int i = 0; i < busted.length(); i++) {
            final String c = "" + busted.charAt(i);
            if (lastCap) {
                name += c.toLowerCase();
            } else {
                name += c;
            }
            if (!c.toLowerCase().equals(c)) {
                lastCap = true;
            } else {
                lastCap = false;
            }
        }
        return name.replaceAll("_", ""); //CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name);
    }

    // Used from API Error reporting
    public static String toEnglish(final String name) {
        return Joiner.on(' ').join(Iterables.transform(Splitter.on(' ')
                .omitEmptyStrings()
                .split(name.replaceAll("_", " ")), new Function<String, String>() {
            @Override
            public String apply(String input) {
                return Character.toUpperCase(input.charAt(0)) + input.substring(1);
            }
        }));
    }

    public static String firstLetterUp(final String string) {
        final String firstChar = (string.charAt(0) + "").toUpperCase();
        if (string.length() == 1) {
            return firstChar;
        }
        return firstChar + string.substring(1);
    }

    public static String firstLetterDown(final String string) {
        final String firstChar = (string.charAt(0) + "").toLowerCase();
        if (string.length() == 1) {
            return firstChar;
        }
        return firstChar + string.substring(1);
    }

    public static String toJavaClassName(final String str) {
        return firstLetterUp(toJavaVariableName(str));
    }

    public static String toJavaClassNameUnunderscore(final String str) {
        return firstLetterUp(toJavaVariableName(underscoresToCamel(str)));
    }

    public static String underscoresToCamel(final String underscored) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, underscored);
    }

}
