package com.base.dl.methodgenerators;

import com.base.files.FileI;
import com.base.gen.DLGen.DLMethodGenerator;
import com.base.gen.SourceBuilder;
import com.base.model.AbstractField;
import com.base.model.AbstractModel;
import java.util.Set;
import java.util.TreeSet;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service=DLMethodGenerator.class)
public class UpdateRaw implements DLMethodGenerator {

    @Override
    public String genMethod(final AbstractModel m) {
        final SourceBuilder bldr = new SourceBuilder();

        // def:UPDATE
        {
            // Using fields
            bldr.append(1, "private static final String UPDATE\n")
                    .append(2, "= \"UPDATE \"\n").append(2, "+ TABLE_NAME\n").append(2, "+ \" SET \"\n");

            boolean first = true;
            for (final AbstractField field : m.allNonGuidOriginalFields()) {
                bldr.append(2, "+ ").append("\"").append(first ? " " : ",").append(field.toDBName()).append(" = ?\"\n");
                first = false;
            }
            bldr.append(2, "+ \",modified_at = ?\"\n")
                    .append(2, "+ \" WHERE \"\n")
                    .append(2, "+ \" deleted_at IS NULL AND\"\n")
                    .append(2, "+ \" ").append(m.getGuidField().toDBName()).append(" = ?\";\n\n");

            bldr.append("\n\n");
        }

        // By fields
        bldr.append(1, "public static ExecutionResult<Void> update(\n").append(2, "final Connection conn");
        for (final AbstractField field : m.allOriginalFields()) {
            bldr.append("\n").append(2, ", ").append(field.toJavaDeclaration());
        }
        bldr.append(") throws SQLException {\n");
        bldr.append(2, "try(final PreparedStatement ps = conn.prepareStatement(UPDATE)) {\n");
        bldr.append(3, "final DBUtil dBUtil = new DBUtil(ps);\n\n");
        for (final AbstractField field : m.allNonGuidOriginalFields()) {
            bldr.append(3, "dBUtil.").append(field.setNullableMethod())
                    .append("(")
                    .append(field.toJavaVariableName())
                    .append(");\n");
        }
        bldr.append(3, "dBUtil.setNowTimestamp();\n");
        bldr.append(3, "dBUtil.").append(m.getGuidField().setNullableMethod())
                .append("(")
                .append(m.getGuidField().toJavaVariableName())
                .append(");\n");

        bldr.append(3, "if(1 != ps.executeUpdate()) {\n");
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
