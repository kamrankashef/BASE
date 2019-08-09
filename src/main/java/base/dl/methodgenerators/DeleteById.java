package base.dl.methodgenerators;

import base.files.FileI;
import base.gen.DLGen.DLMethodGenerator;
import base.gen.SourceBuilder;
import base.model.AbstractModel;
import base.model.PrimitiveField;
import base.util.CaseConversion;
import java.util.Set;
import java.util.TreeSet;

public class DeleteById implements DLMethodGenerator {

    private PrimitiveField[] fields;
    private final String queryName;
    private final String functionName;

    public DeleteById() {
        // Need to have logic that accounts for this effectively non-funcational
        // case
        fields = new PrimitiveField[0];
        queryName = "<NULL>";
        functionName = "<NULL>";
    }

    public DeleteById(final PrimitiveField... fields) {
        this.fields = fields;
        final StringBuffer queryBuffer = new StringBuffer("DELETE_BY");
        final StringBuffer functionBuffer = new StringBuffer("deleteBy");
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
                .appendln("\"DELETE FROM \"")
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
        bldr.append(1, "public static void ").append(functionName).appendln("(")
                .appendln(3, "final Connection conn,");
        // params
        index = 0;
        for (final PrimitiveField field : this.fields) {
            bldr.appendln(3, field.toJavaDeclaration() + (++index == fields.length ? "" : ","));
        }

        bldr.appendln(1, ") throws SQLException {");
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

        bldr.appendln(3, "ps.executeUpdate();");

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

        imports.add(FileI.COMMON_PKG + ".DBUtil");

        return imports;
    }

}
