package com.base.dl.methodgenerators;

import com.base.files.FileI;
import com.base.gen.DLGen.DLMethodGenerator;
import com.base.gen.SourceBuilder;
import com.base.model.AbstractModel;
import com.base.model.PrimitiveField;
import com.base.util.CaseConversion;
import java.util.Set;
import java.util.TreeSet;

public class GetBy implements DLMethodGenerator {

    private PrimitiveField[] fields;

    public GetBy() {
        // Need to have logic that accounts for this effectively non-funcational
        // case
        this(new PrimitiveField[0]);
    }

    public GetBy(final PrimitiveField... fields) {
        this.fields = fields;
    }

    @Override
    public String genMethod(final AbstractModel m) {

        final SourceBuilder bldr = new SourceBuilder();

        final StringBuffer queryBuffer = new StringBuffer("GET_BY");
        final StringBuffer functionBuffer = new StringBuffer("getBy");
        for (final PrimitiveField field : this.fields) {
            queryBuffer.append("_").append(field.toDBName().toUpperCase());
            functionBuffer.append("_").append(CaseConversion.toJavaClassName(field.toJavaVariableName()));
        }
        final String queryName = queryBuffer.toString();
        final String functionName = functionBuffer.toString();
        int index = 0;

        // def: method
        bldr.append(1, "public Collection<").append(m.getJavaClassName()).append("> ").append(functionName).appendln("(")
                .appendln(3, "final Connection conn,");
        // params
        for (final PrimitiveField field : this.fields) {
            bldr.appendln(3, field.toJavaDeclaration() + (++index == fields.length ? "" : ","));
        }
        bldr.appendln(1, ") throws SQLException {");

        // def:GET_BY_...
        bldr.appendln(2, "final String " + queryName)
                .appendln(4, "= \"SELECT \"")
                .appendln(4, "+ FIELDS")
                .appendln(4, "+\" FROM \"")
                .appendln(4, "+ tableName()")
                .append(4, "+ \" WHERE\" ");
        index = 0;
        for (final PrimitiveField field : this.fields) {

            bldr.appendln().append(4, "+ \"" + ((index++ != 0) ? " AND " : " ") + field.toDBName() + " = ?\"");
        }
        bldr
                .appendlnln(";");

        bldr.append(2, "try (final PreparedStatement ps = conn.prepareStatement(").append(queryName).appendln(")) {");
        bldr.appendln(3, "final DBUtil dBUtil = new DBUtil(ps);");

        for (final PrimitiveField field : this.fields) {

            bldr.append(3, "dBUtil.")
                    .append(field.getPrimitiveType().setNullableMethod)
                    .append("(")
                    .append(field.toJavaVariableName())
                    .append(");")
                    .appendln();
        }

        CommonSnipits.collectResults(bldr, m, index);
        bldr.appendln(2, "}");
        bldr.appendln(1, "}");
        return bldr.toString();
    }

    @Override
    public Set< String> requiredImports(final AbstractModel m, final String parentPackage) {
        final Set<String> imports = new TreeSet<>();
        imports.add("java.sql.Connection");
        imports.add("java.sql.PreparedStatement");
        imports.add("java.sql.SQLException");
        imports.add("java.util.Collection");
        imports.add("java.util.LinkedList");

        imports.add(FileI.COMMON_PKG + ".DBUtil");

        return imports;
    }

}
