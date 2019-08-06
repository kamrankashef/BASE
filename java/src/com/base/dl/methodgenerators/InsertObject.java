package com.base.dl.methodgenerators;

import com.base.files.FileI;
import com.base.gen.DLGen.DLMethodGenerator;
import com.base.gen.SourceBuilder;
import com.base.model.AbstractField;
import com.base.model.AbstractModel;
import java.util.Set;
import java.util.TreeSet;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = DLMethodGenerator.class)
public class InsertObject implements DLMethodGenerator {

    @Override
    public String genMethod(final AbstractModel m) {
        final SourceBuilder bldr = new SourceBuilder();

        // Insert with object 
        bldr.append(1, "public ExecutionResult<Void> insert(\n")
                .append(3, "final Connection conn,\n")
                .append(3, m.toJavaDeclaration())
                .append(") throws SQLException {\n\n");
        bldr.append(2, "return insert(\n").append(4, "conn");
        for (final AbstractField field : m.allOriginalFields()) {
            bldr.append(",\n")
                    .append(4, m.toJavaVariableName())
                    .append(".")
                    .append(field.toJavaVariableName());
        }
        bldr.append(");\n");
        bldr.append(1, "}");

        return bldr.toString();
    }

    @Override
    public Set< String> requiredImports(final AbstractModel m, final String parentPackage) {
        final Set<String> imports = new TreeSet<>();
        imports.add("java.sql.Connection");
        imports.add("java.sql.SQLException");

        imports.add(m.getCanonicalName(parentPackage));

        imports.add(FileI.COMMON_PKG + ".ExecutionResult");

        return imports;
    }

}
