package com.base.model.methodgenerators;

import com.base.gen.ModelGen;
import com.base.gen.ModelGen.ModelMethodGenerator;
import com.base.gen.SourceBuilder;
import com.base.model.AbstractField;
import com.base.model.AbstractModel;
import java.util.Collections;
import java.util.Set;

public class ToStringGenerator implements ModelGen.ModelMethodGenerator {

    @Override
    public String genMethod(final AbstractModel model, final String parentPackage) {
        final SourceBuilder bldr = new SourceBuilder();

        // Start toString()
        bldr.append(1, "@Override\n");
        bldr.append(1, "public String toString() {\n\n");
        bldr.append(2, "final StringBuilder bldr = new StringBuilder();\n\n");
        bldr.append(2, "bldr.append(\"").append(model.getJavaClassName()).append("=[\");\n");
        boolean first = true;
        for (final AbstractField field : model.allOriginalFields()) {
            if (field.shareWithClient) {
                final String prefix = first ? "" : ",";
                bldr.append(2, "bldr.append(\"")
                        .append(prefix).append(field.toDBName())
                        .append("='\").append(").append(field.toJavaVariableName())
                        .append(").append(\"'\");\n");
                first = false;
            }
        }
        bldr.append(2, "bldr.append(\"]\");\n\n");
        bldr.append(2, "return bldr.toString();\n");
        bldr.append(1, "}");
        return bldr.toString();
    }

    @Override
    public Set<String> requiredImports() {
        return Collections.EMPTY_SET;
    }

}
