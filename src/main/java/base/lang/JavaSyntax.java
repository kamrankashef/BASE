package base.lang;

public class JavaSyntax implements LanguageSyntaxI {


    private static String[] RESERVED_WORDS
            = {"abstract", "continue", "for", "new", "switch",
            "assert", "default", "goto", "package", "synchronized", "boolean", "do", "if", "private", "this", "break",
            "double", "implements", "protected", "throw", "byte", "else", "import", "public", "throws",
            "case", "enum", "instanceof", "return", "transient",
            "catch", "extends", "int", "short", "try",
            "char", "final", "interface", "static", "void",
            "class", "finally", "long", "strictfp", "volatile",
            "const", "float", "native", "super", "while",
            // From set of genators
            "ps", "rs", "bldr", "int", "long", "e", "ex"
    };


    @Override
    public boolean isReservedWord(final String checkStr) {
        for (final String word : RESERVED_WORDS) {
            if (word.equals(checkStr)) {
                return true;
            }
        }
        return false;
    }

}
