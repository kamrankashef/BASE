package base.lang;

public interface LanguageSyntaxI {

    /**
     * Is word reserved in the language
     * @param word
     * @return
     */
    public boolean isReservedWord(final String word);
}
