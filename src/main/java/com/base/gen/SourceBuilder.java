package com.base.gen;

public class SourceBuilder {

    private static String INDENT = "    ";

    final StringBuilder bldr;

    public SourceBuilder() {
        bldr = new StringBuilder();
    }

    public SourceBuilder(final String str) {
        bldr = new StringBuilder(str);
    }

    @Override
    public String toString() {
        return bldr.toString();
    }

    public SourceBuilder appendln() {
        bldr.append("\n");
        return this;
    }

    public SourceBuilder appendln(final Object str) {
        bldr.append(str);
        bldr.append("\n");
        return this;
    }

    public SourceBuilder appendln(final String str) {
        bldr.append(str);
        bldr.append("\n");
        return this;
    }

    public SourceBuilder appendEscapeQuotes(final String str) {
        bldr.append(str.replaceAll("\"", "\\\\\""));
        return this;
    }

    public SourceBuilder append(final Object str) {
        bldr.append(str);
        return this;
    }

    public SourceBuilder append(final String str) {
        bldr.append(str);
        return this;
    }

    public SourceBuilder indent(final int count) {
        this.append(count, "");
        return this;
    }

    public SourceBuilder append(final int indentCount, final String str) {
        bldr.append(SourceBuilder.getIndents(indentCount));
        bldr.append(str);
        return this;
    }

    public SourceBuilder appendln(final int indentCount, final Object str) {
        bldr.append(SourceBuilder.getIndents(indentCount));
        bldr.append(str);
        bldr.append("\n");

        return this;
    }

    public SourceBuilder appendlnln(final int indentCount, final String str) {
        this.appendln(indentCount, str);
        bldr.append("\n");

        return this;
    }

    public SourceBuilder appendlnln(final String str) {
        return appendlnln(0, str);
    }

    public SourceBuilder appendlnln() {
        return appendlnln("");
    }

    public SourceBuilder appendln(final int indentCount, final String str) {
        bldr.append(SourceBuilder.getIndents(indentCount));
        bldr.append(str);
        bldr.append("\n");

        return this;
    }

    public static String getIndents(final int count) {
        final StringBuilder bldr = new StringBuilder();

        for (int i = 0; i < count; i++) {
            bldr.append(INDENT);
        }

        return bldr.toString();
    }
}
