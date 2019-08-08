package com.base.dl.methodgenerators;

import com.base.files.FileI;
import com.base.gen.DLGen.DLMethodGenerator;
import com.base.gen.SourceBuilder;
import com.base.model.AbstractField;
import com.base.model.AbstractModel;
import java.util.Set;
import java.util.TreeSet;

public class FromRs implements DLMethodGenerator {

    @Override
    public String genMethod(final AbstractModel m) {
        final SourceBuilder bldr = new SourceBuilder();

        bldr.append(1, "private static ")
                .append(m.getJavaClassName())
                .append(" fromRS(final ResultSet rs)\n").append(3, "throws SQLException {\n\n");
        for (final AbstractField field : m.allOriginalFields()) {
            bldr
                    .append(2, field.toJavaDeclaration())
                    .append(" = DBUtil.")
                    .append(field.getNullableMethod())
                    .append("(rs, \"")
                    .append(field.toDBName())
                    .append("\");\n");
        }

        bldr.append("\n").append(2, "return ").append(m.calledConstructor(2));
        bldr.append(";\n");
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
        imports.add(m.getCanonicalName(parentPackage));
        imports.add(FileI.COMMON_PKG + ".DBUtil");

        return imports;
    }

}
