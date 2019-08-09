package com.base.application.gen2;

import com.google.gson.annotations.SerializedName;
import com.kamserverutils.common.util.StringUtil;
import java.io.File;

public class ApplicationSettings {

    @SerializedName("deployment_parent_directory")
    final private String deploymentParentDirectory;
    @SerializedName("name")
    final private String name;
    @SerializedName("org")
    final private String org;

    public ApplicationSettings(
            final String name,
            final String org,
            final String deploymentParentDirectory
    ) {

        if (!isValidApplicationDirectory(deploymentParentDirectory)) {
            throw new IllegalArgumentException("Illegal directory name " + deploymentParentDirectory);
        }
        if (!isVaildName(name)) {
            throw new IllegalArgumentException("Illegal project name " + name);
        }
        if (null != getOrgError(org)) {
            throw new IllegalArgumentException("Illegal org: " + getOrgError(org));
        }
        this.deploymentParentDirectory = deploymentParentDirectory;
        this.name = name.trim();
        this.org = org;
    }

    public File applicationDirectory() {
        return new File(deploymentParentDirectory + "/" + name);
    }

    public File applicationParentDirectory() {
        return new File(deploymentParentDirectory);
    }

    public static boolean isValidApplicationDirectory(final String deploymentParentDirectory) {
        return new File(deploymentParentDirectory).exists();
    }

    public String getName() {
        return this.name;
    }

    public String getOrg() {
        return this.org;
    }

    public static boolean isVaildName(final String name) {
        return !StringUtil.isNullWhiteSpace(name);
    }

    public static String getOrgError(final String org) {
        if (org == null || org.length() == 0) {
            return "cannot be empty;";
        }

        if (!org.toLowerCase().equals(org)) {
            return "must be lower case";
        }

        if ("org".contains("..")) {
            return "cannot contain \"..\"";
        }

        if (!org.matches("^([\\p{L}_$][\\p{L}\\p{N}_$]*\\.)*[\\p{L}_$][\\p{L}\\p{N}_$]*$")) {
            return "must be of the form \"org.identified.example\"";
        }

        try {
            // TODO Have a runtime check
            Class.forName(org);
            return "indentified is taken";
        } catch (ClassNotFoundException ex) {

        }

            for (final String part : org.split("\\.")) {
                if (isReservedWord(part)) {
                    return "cannot have the reserved word '" + part + "'";
                }
        }
        return null;
    }

    public static boolean isReservedWord(final String checkStr) {
        for (final String word : RESERVER_WORDS) {
            if (word.equals(checkStr)) {
                return true;
            }
        }
        return false;
    }
    
    final static String[] RESERVER_WORDS
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
}
