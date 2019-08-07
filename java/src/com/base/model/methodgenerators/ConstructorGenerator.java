package com.base.model.methodgenerators;

import com.base.gen.SourceBuilder;
import com.base.gen.ModelGen;
import com.base.model.AbstractField;
import com.base.model.AbstractModel;
import java.util.Collections;
import java.util.Set;

public class ConstructorGenerator implements ModelGen.ModelMethodGenerator {

    @Override
    public String genMethod(final AbstractModel model, final String parentPackage) {
        final SourceBuilder bldr = new SourceBuilder();

        // Constructor
        bldr.append(1, "public ").append(model.getJavaClassName()).append("(\n");

        boolean first = true;
        for (final AbstractField field : model.allOriginalFields()) {
            bldr.append(first ? "" : ",\n");
            bldr.append(3, field.toJavaDeclaration());
            first = false;
        }
        bldr.append(") {\n");

        for (final AbstractField field : model.allOriginalFields()) {
            bldr.append(2, "this.").append(field.toJavaVariableName())
                    .append(" = ")
                    .append(field.toJavaVariableName())
                    .append(";\n");
        }

        // END constructor
        bldr.append(1, "}");

        return bldr.toString();
    }

    @Override
    public Set<String> requiredImports() {
        return Collections.EMPTY_SET;
    }

}
