package base.dl.methodgenerators;

import base.files.FileI;
import base.gen.DLGen.DLMethodGenerator;
import base.gen.SourceBuilder;
import base.model.AbstractField;
import base.model.AbstractModel;
import java.util.Set;
import java.util.TreeSet;

public class InsertObjectDBUtil implements DLMethodGenerator {

    @Override
    public String genMethod(final AbstractModel m) {
        final SourceBuilder bldr = new SourceBuilder();

        // Insert with fields
        bldr.append(1, "private void addBatch(")
                .append("final DBUtil dBUtil")
                .append(", ")
                .append(m.toJavaDeclaration());
        bldr.appendln(") throws SQLException {")
                .appendln();
        for (final AbstractField field : m.allOriginalFields()) {
            bldr.append(2, "dBUtil.").append(field.setNullableMethod())
                    .append("(")
                    .append(m.toJavaVariableName() + "." + field.toJavaVariableName())
                    .append(");\n");
        }
        bldr.appendln(2, "dBUtil.setNowTimestamp();");
        bldr.appendln(2, "dBUtil.setNowTimestamp();");
        bldr.appendln(2, "dBUtil.setNullableTimestamp(null);");
        bldr.appendln(2, "dBUtil.addBatch();");
        bldr.appendln(1, "}");

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
