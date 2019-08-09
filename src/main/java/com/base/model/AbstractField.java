package com.base.model;

import com.base.application.gen2.ApplicationSettings;
import com.base.util.CaseConversion;
import com.google.gson.annotations.SerializedName;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractField
        implements
        MetaObject,
        Comparable<AbstractField> {

    @SerializedName("contraints")
    public final Set<Constraint> contraints;
//    public boolean nullable = false;
    @SerializedName("shared_with_client")
    public boolean shareWithClient = true;
    @SerializedName("user_generated")
    public boolean userGenerated = true;
    @SerializedName("uuid")
    private final String uuid;

    public AbstractField(
            final Set<Constraint> contraints) {
        this.contraints = new HashSet<>(contraints);
        uuid = UUID.randomUUID().toString();
//        this.nullable = !contraints.contains(Constraint.NOT_NULL);
//        System.out.println(nullable);
    }

    public final void setNullable(final boolean canBeNull) {
        if (canBeNull) {
            this.contraints.remove(Constraint.NOT_NULL);
        } else {
            this.contraints.add(Constraint.NOT_NULL);
        }
    }

    @Override
    final public String uuid() {
        return this.uuid;
    }

    public final boolean nullable() {
        return !contraints.contains(Constraint.NOT_NULL);
    }

    public void setDontShareWithClient() {
        this.shareWithClient = false;
    }

    public abstract String getName();

    @Override
    public int hashCode() {
        return (this.getName() + this.toJavaType()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractField other = (AbstractField) obj;
        if (!this.getName().equals(other.getName())) {
            return false;
        }
        if (!this.toJavaType().equals(other.toJavaType())) {
            return false;
        }
        return true;
    }

    public void setNotUserGenerate() {
        this.userGenerated = false;
    }

    public boolean isUserGenerated() {
        return this.userGenerated;
    }

    final static Set<String> RESERVED_WORDS = new HashSet<>();

    static {
        RESERVED_WORDS.add("top");
        RESERVED_WORDS.add("select");
        RESERVED_WORDS.add("double");
        RESERVED_WORDS.add("date");
        RESERVED_WORDS.add("group");
        RESERVED_WORDS.add("rule");
        RESERVED_WORDS.add("end");
        RESERVED_WORDS.add("index");
    }

    public static boolean isSQLReservedWord(final String str) {
        return RESERVED_WORDS.contains(str.toLowerCase());
    }

    public String toDBName() {

        final String dbName = CaseConversion.toDBName(getName());

        if (isSQLReservedWord(dbName)) {
            return dbName + "_";
        }

        final String quote = dbName.matches("^\\d+.*$") ? "\"" : "";

        return quote + dbName + quote;
    }

    public String toJavaVariableName(final String prefix) {
        final String javaVarName;
        if ("".equals(prefix)) {
            javaVarName = CaseConversion.toJavaVariableName(this.getName());
        } else {
            javaVarName = prefix + CaseConversion.toJavaClassName(this.getName());
        }
        if (javaVarName.matches("^\\d+.*$") || ApplicationSettings.isReservedWord(javaVarName)) {
            return "_" + javaVarName;
        }
        return javaVarName;
    }

    public String toJavaVariableName() {
        final String javaVarName = CaseConversion.toJavaVariableName(this.getName());
        if (javaVarName.matches("^\\d+.*$") || ApplicationSettings.isReservedWord(javaVarName)) {
            return "_" + javaVarName;
        }
        return javaVarName;
    }

    @Override
    final public String getJavaClassName() {
        return CaseConversion.toJavaClassName(this.getName());
    }

    public abstract String toJavaType();

    public abstract String toDBRow();

    public String toJavaDeclaration(final String prefix) {
        return "final " + this.toJavaType() + " " + toJavaVariableName(prefix);
    }

    public String toJavaDeclaration() {
        return toJavaDeclaration("");
    }

    public String toEnglish() {
        return CaseConversion.toEnglish(getName());
    }

    abstract public String getNullableMethod();

    abstract public String setNullableMethod();

    @Override
    final public int compareTo(final AbstractField o) {
        if (!this.toJavaVariableName().equals(o.toJavaVariableName())) {
            return this.toJavaVariableName().compareTo(o.toJavaVariableName());
        }
        return (this.toJavaVariableName() + this.toJavaType()).compareTo(o.toJavaVariableName() + o.toJavaType());
    }
}
