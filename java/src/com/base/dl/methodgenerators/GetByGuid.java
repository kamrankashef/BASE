package com.base.dl.methodgenerators;

import com.base.files.FileI;
import com.base.gen.DLGen.DLMethodGenerator;
import com.base.gen.SourceBuilder;
import com.base.model.AbstractModel;
import java.util.Set;
import java.util.TreeSet;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = DLMethodGenerator.class)
public class GetByGuid implements DLMethodGenerator {

    @Override
    public String genMethod(final AbstractModel m) {

        final String guidVar = m.getGuidField().toJavaVariableName();

        final SourceBuilder bldr = new SourceBuilder();

        // Get by guid
        bldr.append(1, "public ExecutionResult<")
                .append(m.getJavaClassName())
                .append("> getByGuid(final Connection conn, final String ")
                .append(guidVar)
                .appendln(") throws SQLException {");
        
        // def:GET_BY_GUID
        bldr.appendln(2, "final String GET_BY_GUID")
                .appendln(4, "= \"SELECT \"")
                .appendln(4, "+ FIELDS")
                .appendln(4, "+ \" FROM \"")
                .appendln(4, "+ tableName()")
                .appendln(4, "+ \" WHERE \"")
                .appendln(4, "+ \" deleted_at IS NULL AND \"")
                .append(4, "+ \"").append(m.getGuidField().toDBName()).appendlnln(" = ?\";");

        bldr.append(2, "try(final PreparedStatement ps = conn.prepareStatement(GET_BY_GUID)) {\n");
        bldr.append(3, "final DBUtil dBUtil = new DBUtil(ps);\n");
        bldr.append(3, "dBUtil.setNullableString(").append(guidVar).append(");\n");
        bldr.append(3, "final ResultSet rs = ps.executeQuery();\n");
        bldr.append(3, "if(!rs.next()) {\n");
        bldr.append(4, "return ExecutionResult.errorResult(new ErrorType(\"")
                .append(m.toDBName())
                .append("_not_found\", \"")
                .append(m.toEnglish())
                .append(" not found\"));\n");
        bldr.append(3, "}\n");
        bldr.append(3, "return ExecutionResult.successResult(")
                .append(m.dlName())
                .append(".fromRS(rs));\n");
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

        imports.add(m.getCanonicalName(parentPackage));

        imports.add(FileI.COMMON_PKG + ".DBUtil");
        imports.add(FileI.COMMON_PKG + ".ErrorType");
        imports.add(FileI.COMMON_PKG + ".ExecutionResult");

        return imports;
    }

}
