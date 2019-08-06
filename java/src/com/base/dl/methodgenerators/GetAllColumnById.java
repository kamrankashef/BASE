package com.base.dl.methodgenerators;

import com.base.files.FileI;
import com.base.gen.DLGen.DLMethodGenerator;
import com.base.gen.SourceBuilder;
import com.base.model.AbstractModel;
import com.base.model.PrimitiveField;
import com.base.util.CaseConversion;
import java.util.Set;
import java.util.TreeSet;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = DLMethodGenerator.class)
public class GetAllColumnById implements DLMethodGenerator {

    private PrimitiveField[] fields;
    private final String queryName;
    private final String functionName;
    final PrimitiveField columnToExtract;

    public GetAllColumnById() {
        // Need to have logic that accounts for this effectively non-funcational
        // case
        fields = new PrimitiveField[0];
        columnToExtract = null;
        queryName = "<NULL>";
        functionName = "<NULL>";
    }

    public GetAllColumnById(final PrimitiveField columnToExtract,
            final PrimitiveField... fields) {
        this.columnToExtract = columnToExtract;
        this.fields = fields;
        final StringBuffer queryBuffer = new StringBuffer("GET_" + CaseConversion.toJavaClassName(columnToExtract.toJavaVariableName()) + "_BY");
        final StringBuffer functionBuffer = new StringBuffer("get" + CaseConversion.toJavaClassName(columnToExtract.toJavaVariableName()) + "By");

        for (final PrimitiveField field : this.fields) {
            queryBuffer.append("_").append(field.toDBName().toUpperCase());
            functionBuffer.append("_").append(CaseConversion.toJavaClassName(field.toJavaVariableName()));
        }

        queryName = queryBuffer.toString();
        functionName = functionBuffer.toString();
    }

    @Override
    public String genMethod(final AbstractModel m) {

        final SourceBuilder bldr = new SourceBuilder();

        // def:DELETE_BY_GUID
        bldr.appendln(1, "private static final String " + this.queryName)
                .append(3, "= ")
                .appendln("\"SELECT \"")
                .appendln(3, "+ \"" + columnToExtract.toDBName() + "\"")
                .appendln(3, "+ \" FROM \"")
                .appendln(3, "+ TABLE_NAME")
                .append(3, "+ \" WHERE\" ");
        int index = 0;
        for (final PrimitiveField field : this.fields) {

            bldr.appendln().append(3, "+ \"" + ((index++ != 0) ? " AND " : " ") + field.toDBName() + " = ?\"");
        }
        bldr
                .appendln(";")
                .appendln();

        // Delete
        bldr.append(1, "public static Collection<")
                .append(columnToExtract.getType().javaTypeName)
                .append(">")
                .append(functionName).appendln("(")
                .appendln(3, "final Connection conn,");
        // params
        index = 0;
        for (final PrimitiveField field : this.fields) {
            bldr.appendln(3, field.toJavaDeclaration() + (++index == fields.length ? "" : ","));
        }

        bldr.appendlnln(1, ") throws SQLException {");
        bldr.append(2, "final Collection<").append(columnToExtract.getType().javaTypeName)
                .appendlnln("> objects = new LinkedList<>();");
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

        bldr.appendln(3, "try (final ResultSet rs = ps.executeQuery()) {");
        bldr.appendln(4, "while (rs.next()) {");
        bldr.append(5, "objects.add( DBUtil.")
                .append(columnToExtract.getNullableMethod())
                .append("(rs, \"")
                .append(columnToExtract.toDBName())
                .append("\"))")
                .appendlnln(";");
        bldr.appendln(4, "}");
        bldr.appendln(3, "}");
        bldr.appendln(3, "return objects;");
        bldr.appendln(2, "}");
        bldr.append(1, "}");

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
