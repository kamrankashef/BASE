package base.dl.methodgenerators;

import base.files.FileI;
import base.gen.DLGen.DLMethodGenerator;
import base.gen.SourceBuilder;
import base.model.AbstractModel;
import java.util.Set;
import java.util.TreeSet;

public class TruncateTable implements DLMethodGenerator {

    @Override
    public String genMethod(final AbstractModel m) {
        final SourceBuilder bldr = new SourceBuilder();

        // By fields
        bldr.append(1, "public void truncate(")
                .append("final Connection conn")
                .appendln(") throws SQLException {");
        bldr.appendln(2, "final String TRUNCATE")
                .appendln(4, "= \"TRUNCATE TABLE \"")
                .appendlnln(4, "+ tableName();");

        bldr.appendln(3, "try(final PreparedStatement ps = conn.prepareStatement(TRUNCATE)) {");
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
