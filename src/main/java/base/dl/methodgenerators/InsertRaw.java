package base.dl.methodgenerators;

import base.files.FileI;
import base.gen.DLGen.DLMethodGenerator;
import base.gen.SourceBuilder;
import base.model.AbstractField;
import base.model.AbstractModel;
import java.util.Set;
import java.util.TreeSet;

public class InsertRaw implements DLMethodGenerator {

    @Override
    public String genMethod(final AbstractModel m) {
        final SourceBuilder bldr = new SourceBuilder();

        // Insert with fields
        bldr.append(1, "private ExecutionResult<Void> insert(\n")
                .append(3, "final Connection conn");
        for (final AbstractField field : m.allOriginalFields()) {
            bldr.append(",\n").indent(3).append(field.toJavaDeclaration());
        }
        bldr.appendlnln(") throws SQLException {");
        // def:INSERT
        CommonSnipits.makeInsertString(bldr, m);

        bldr.append(2, "try (final PreparedStatement ps = conn.prepareStatement(INSERT)) {\n\n");
        bldr.append(3, "final DBUtil dBUtil = new DBUtil(ps);\n\n");
        for (final AbstractField field : m.allOriginalFields()) {
            bldr.append(3, "dBUtil.").append(field.setNullableMethod())
                    .append("(")
                    .append(field.toJavaVariableName())
                    .append(");\n");
        }
        bldr.append(3, "dBUtil.setNowTimestamp();\n");
        bldr.append(3, "dBUtil.setNowTimestamp();\n");
        bldr.append(3, "dBUtil.setNullableTimestamp(null);\n");
        bldr.append(3, "if (1 != ps.executeUpdate()) {\n");
        bldr.append(4, "return ExecutionResult.errorResult();\n");
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
        imports.add("java.sql.SQLException");

        imports.add(FileI.COMMON_PKG + ".DBUtil");
        imports.add(FileI.COMMON_PKG + ".ExecutionResult");

        return imports;
    }

}
