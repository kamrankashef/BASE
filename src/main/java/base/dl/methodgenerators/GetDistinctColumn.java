package base.dl.methodgenerators;

import base.files.FileI;
import base.gen.DLGen.DLMethodGenerator;
import base.gen.SourceBuilder;
import base.model.AbstractModel;
import base.model.PrimitiveField;
import java.util.Set;
import java.util.TreeSet;

public class GetDistinctColumn implements DLMethodGenerator {

    final PrimitiveField field;

    public GetDistinctColumn() {
        field = null;
    }

    public GetDistinctColumn(final PrimitiveField field) {
        this.field = field;
    }

    @Override
    public String genMethod(final AbstractModel m) {

        final String guidVar = m.getGuidField().toJavaVariableName();

        final SourceBuilder bldr = new SourceBuilder();

        // Get by guid
        bldr.append(1, "public TreeSet<")
                .append(field.getType().toJavaTypeName())
                .append("> getDistinct").append(field.getJavaClassName())
                .append("(final Connection conn) throws SQLException {");

        bldr.append(2, "final TreeSet<").append(field.getType().toJavaTypeName())
                .appendlnln("> items = new TreeSet<>();");
        // def:GET_DISTINCT
        bldr.appendln(2, "final String GET_DISTINCT")
                .appendln(4, "= \"SELECT \"")
                .append(4, "+ \"DISTINCT(").append(field.toDBName()).append(") as ").append(field.toDBName()).appendln("\"")
                .appendln(4, "+ \" FROM \"")
                .appendlnln(4, "+ tableName();");

        bldr.appendln(2, "try(final PreparedStatement ps = conn.prepareStatement(GET_DISTINCT)) {");
        bldr.appendln(3, "try(final ResultSet rs = ps.executeQuery()) {");
        bldr.appendln(4, "while(rs.next()) {");
        bldr.append(5, "items.add(").append("DBUtil.")
                    .append(field.getNullableMethod())
                    .append("(rs, \"")
                    .append(field.toDBName())
                .appendln("\"));");

        bldr.appendln(4, "}");
        bldr.appendln(3, "}");
        bldr.appendln(2, "}");
        bldr.appendln(2, "return items;");
        bldr.appendln(1, "}");

        return bldr.toString();
    }

    @Override
    public Set< String> requiredImports(final AbstractModel m, final String parentPackage) {
        final Set<String> imports = new TreeSet<>();
        imports.add("java.sql.Connection");
        imports.add("java.sql.PreparedStatement");
        imports.add("java.sql.SQLException");
        imports.add("java.util.TreeSet");

        imports.add(m.getCanonicalName(parentPackage));

        imports.add(FileI.COMMON_PKG + ".DBUtil");

        return imports;
    }

}
