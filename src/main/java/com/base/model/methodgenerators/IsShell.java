package com.base.model.methodgenerators;

import com.base.gen.ModelGen;
import com.base.gen.SourceBuilder;
import com.base.model.AbstractModel;
import java.util.Collections;
import java.util.Set;

public class IsShell implements ModelGen.ModelMethodGenerator {

    @Override
    public String genMethod(final AbstractModel model, final String parentPackage) {
        final SourceBuilder bldr = new SourceBuilder();

        // Start toString()
        bldr.append(1, "public boolean isShell() {\n\n");
        bldr.append(2, "return ").append("" + model.getPrimitiveFields().isEmpty()).append(";\n");
        bldr.append(1, "}");
        return bldr.toString();
    }

    @Override
    public Set<String> requiredImports() {
        return Collections.EMPTY_SET;
    }

}
