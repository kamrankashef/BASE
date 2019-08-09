package base.dl.methodgenerators;

import base.files.FileI;
import base.gen.DLGen.DLMethodGenerator;
import base.gen.SourceBuilder;
import base.model.AbstractModel;
import java.util.Set;
import java.util.TreeSet;

public class DeleteByGuid implements DLMethodGenerator {

    @Override
    public String genMethod(final AbstractModel m) {

        final String guidVar = m.getGuidField().toJavaVariableName();

        final SourceBuilder bldr = new SourceBuilder();

        // def:DELETE_BY_GUID
        bldr.append(1, "private static final String DELETE_BY_GUID\n").append(2, "= ")
                .append("\"UPDATE \"\n")
                .append(2, "+ TABLE_NAME\n")
                .append(2, "+ \" SET\"\n")
                .append(2, "+ \" deleted_at = ?\"\n")
                .append(2, "+ \" WHERE\"\n")
                .append(2, "+ \" deleted_at IS NULL AND ")
                .append(m.getGuidField().toDBName()).append(" = ?\";\n\n");

        // Delete
        bldr.append(1, "public static ExecutionResult<Void> delete(final Connection conn, final String ")
                .append(guidVar)
                .append(") throws SQLException {\n");
        bldr.append(2, "try(final PreparedStatement ps = conn.prepareStatement(DELETE_BY_GUID)) {\n");
        bldr.append(3, "final DBUtil dBUtil = new DBUtil(ps);\n");
        bldr.append(3, "dBUtil.setNowTimestamp();\n");
        bldr.append(3, "dBUtil.setNullableString(").append(guidVar).append(");\n");
        bldr.append(3, "final int affectedRows = ps.executeUpdate();\n");
        bldr.append(3, "if(affectedRows != 1) {\n");
        bldr.append(4, "return ExecutionResult.errorResult(new ErrorType(\"")
                .append(m.toDBName())
                .append("_not_found\", \"")
                .append(m.toEnglish())
                .append(" not found\"));\n");
        bldr.append(3, "}\n");
        bldr.append(3, "return ExecutionResult.successResult(null);\n");
        bldr.append(2, "}\n");
        bldr.append(1, "}");

        return bldr.toString();
    }

    @Override
    public Set< String> requiredImports(final AbstractModel m, final String parentPackage) {
        final Set<String> imports = new TreeSet<>();
        imports.add("java.sql.Connection");
        imports.add("java.sql.PreparedStatement");
        imports.add("java.sql.ResultSet");
        imports.add("java.sql.SQLException");

        imports.add(FileI.COMMON_PKG + ".DBUtil");
        imports.add(FileI.COMMON_PKG + ".ErrorType");
        imports.add(FileI.COMMON_PKG + ".ExecutionResult");

        return imports;
    }

}
