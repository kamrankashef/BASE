package com.base.model.methodgenerators;

import com.base.gen.SourceBuilder;
import com.base.gen.ModelGen;
import com.base.model.AbstractField;
import com.base.model.AbstractModel;
import java.util.Collections;
import java.util.Set;

public class CopyConstructorGenerator implements ModelGen.ModelMethodGenerator {

    final AbstractField[] fieldsToInline;

    // In reality, could set contr
    public CopyConstructorGenerator() {
        this(new AbstractField[]{});
    }

    public CopyConstructorGenerator(final AbstractField... fieldsToInline) {
        this.fieldsToInline = fieldsToInline;
    }

    @Override
    public String genMethod(final AbstractModel model, final String parentPackage) {
        final SourceBuilder bldr = new SourceBuilder();

        // Constructor
        bldr.append(1, "public ").append(model.getJavaClassName()).appendln("(");
        bldr.append(3, model.toJavaDeclaration());
        for (final AbstractField field : fieldsToInline) {
            bldr.appendln(",");
            bldr.append(3, field.toJavaDeclaration());
        }
        bldr.appendln(") {");

        for (final AbstractField field : model.allOriginalFields()) {

            boolean isInlined = false;
            for (final AbstractField inlinedField : this.fieldsToInline) {
                if (field.getName().equals(inlinedField.getName())) {
                    isInlined = true;
                }
            }

            bldr.append(2, "this.").append(field.toJavaVariableName())
                    .append(" = ")
                    .append(isInlined ? "" : model.toJavaVariableName() + ".").append(field.toJavaVariableName())
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
