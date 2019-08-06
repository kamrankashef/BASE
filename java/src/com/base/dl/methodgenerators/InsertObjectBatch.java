package com.base.dl.methodgenerators;

import com.base.files.FileI;
import com.base.gen.DLGen.DLMethodGenerator;
import com.base.gen.SourceBuilder;
import com.base.model.AbstractModel;
import java.util.Set;
import java.util.TreeSet;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = DLMethodGenerator.class)
public class InsertObjectBatch implements DLMethodGenerator {

    private static final int BATCH_SIZE = 5000;

    @Override
    public String genMethod(final AbstractModel m) {
        final SourceBuilder bldr = new SourceBuilder();

        // Insert with object 
        bldr.appendln(1, "public void insertBatch(")
                .append(3, "final Connection conn, final Collection<")
                .append(m.getJavaClassName())
                .append("> objects")
                .appendln(") throws SQLException {")
                .appendln();

        bldr.append(2, "final int batchSize = ").append(BATCH_SIZE).appendln(";");
        bldr.appendln(2, "if (objects.isEmpty()) {")
                .appendln(3, "return;")
                .appendln(2, "}");

        CommonSnipits.makeInsertString(bldr, m);

        bldr.appendln(2, "int count = 0;");
        bldr.appendln(2, "try (final PreparedStatement ps = conn.prepareStatement(INSERT)) {")
                .appendln();
        bldr.appendln(3, "final DBUtil dBUtil = new DBUtil(ps);")
                .appendln();
        bldr.append(3, "for (").append(m.toJavaDeclaration())
                .appendln(" : objects) {")
                .appendln(4, "count++;")
                .append(4, "addBatch( dBUtil, ").append(m.toJavaVariableName()).appendln(");")
                .appendln(4, "if(count % batchSize == 0) {")
                .appendln(5, "ps.executeBatch();")
                .appendln(4, "}")
                .appendln(3, "}");
        bldr.appendln(3, "if(count % batchSize != 0) {");
        bldr.appendln(4, "ps.executeBatch();");
        bldr.appendln(3, "}");
        bldr.appendln(2, "}");

        bldr.append(1, "}");

        return bldr.toString();
    }

    @Override
    public Set< String> requiredImports(final AbstractModel m, final String parentPackage) {
        final Set<String> imports = new TreeSet<>();
        imports.add("java.sql.Connection");
        imports.add("java.sql.SQLException");
        imports.add("java.util.Collection");

        imports.add(m.getCanonicalName(parentPackage));

        imports.add(FileI.COMMON_PKG + ".ExecutionResult");

        return imports;
    }

}
