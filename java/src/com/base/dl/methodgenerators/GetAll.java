package com.base.dl.methodgenerators;

import com.base.files.FileI;
import com.base.gen.DLGen.DLMethodGenerator;
import com.base.gen.SourceBuilder;
import com.base.model.AbstractModel;
import java.util.Set;
import java.util.TreeSet;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = DLMethodGenerator.class)
public class GetAll implements DLMethodGenerator {

    @Override
    public String genMethod(final AbstractModel m) {

        final String guidVar = m.getGuidField().toJavaVariableName();

        final SourceBuilder bldr = new SourceBuilder();

        // def:GET_BY_GUID
        bldr.appendln(1, "private static final String GET_ALL")
                .appendln(2, "= \"SELECT \"")
                .appendln(2, "+ FIELDS")
                .appendln(2, "+ \" FROM \"")
                .appendln(2, "+ TABLE_NAME")
                .appendln(2, "+ \" WHERE \"")
                .appendlnln(2, "+ \" deleted_at IS NULL\";");

        // Get by guid
        bldr.append(1, "public static Collection<")
                .append(m.getJavaClassName())
                .appendlnln("> getAll(final Connection conn) throws SQLException {");

        bldr.append(2, "final Collection<").append(m.getJavaClassName())
                .appendln("> objects = new LinkedList<>();");
        bldr.appendln(2, "try(final PreparedStatement ps = conn.prepareStatement(GET_ALL)) {");
        bldr.appendln(3, "final DBUtil dBUtil = new DBUtil(ps);");
        bldr.appendln(3, "try(final ResultSet rs = ps.executeQuery()){");
        bldr.appendln(4, "while(rs.next()) {");
        bldr.append(5, "objects.add(")
                .append(m.dlName())
                .appendln(".fromRS(rs));");

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

        imports.add(m.getCanonicalName(parentPackage));

        imports.add(FileI.COMMON_PKG + ".DBUtil");
        imports.add(FileI.COMMON_PKG + ".ErrorType");
        imports.add(FileI.COMMON_PKG + ".ExecutionResult");

        return imports;
    }

}
