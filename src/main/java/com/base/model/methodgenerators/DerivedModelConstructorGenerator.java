package com.base.model.methodgenerators;

import com.base.gen.SourceBuilder;
import com.base.gen.ModelGen;
import com.base.model.AbstractField;
import com.base.model.AbstractModel;
import com.base.model.Model;
import com.base.model.PrimitiveField;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class DerivedModelConstructorGenerator implements ModelGen.ModelMethodGenerator {

    final String getUUID;

    public DerivedModelConstructorGenerator() {
        this("common.ServiceUtil.getUUID()");
    }

    public DerivedModelConstructorGenerator(final String getUUID) {
        this.getUUID = getUUID;
    }

    @Override
    public String genMethod(final AbstractModel abstractModel, final String parentPackage) {

        final Model model = (Model) abstractModel; // TODO This is poor design
        final SourceBuilder bldr = new SourceBuilder();

        if (model.getConstituentModelsPlusLinked().isEmpty()) {
            bldr.append(1, "// No models to link");
            return bldr.toString();
        }
        // Constructor
        bldr.append(1, "public ").append(model.getJavaClassName()).append("(");

//        bldr.append(3, model.getGuidField().toJavaDeclaration());
        boolean isFirst = true;
        // Parameters
        for (final AbstractField field : model.getLinkedFields()) {
            bldr.appendln(isFirst ? "" : ",");
            bldr.append(3, field.toJavaDeclaration());
            isFirst = false;
        }

        for (final AbstractField field : model.getAdjoinedFields()) {
            bldr.appendln(isFirst ? "" : ",");
            bldr.append(3, field.toJavaDeclaration());
            isFirst = false;
        }

        for (final Map.Entry<String, AbstractModel> entry : model.getConstituentModelsPlusLinked().entrySet()) {
            final AbstractModel constituent = entry.getValue();
            final String prefix = entry.getKey();
            bldr.appendln(isFirst ? "" : ",");
            bldr.append(3, constituent.toFullyQualifiedJavaDeclaration(parentPackage));
            isFirst = false;
        }

        bldr.append(") {\n");

        bldr.append(2, "this.").append(model.getGuidField().toJavaVariableName()).append(" = ")
                .append(getUUID).appendln(";");

        // Assignments
        for (final AbstractField field : model.getLinkedFields()) {
            bldr.append(2, "this.").append(field.toJavaVariableName())
                    .append(" = ")
                    .append(field.toJavaVariableName())
                    .append(";\n");
        }

        for (final AbstractField field : model.getAdjoinedFields()) {
            bldr.append(2, "this.").append(field.toJavaVariableName())
                    .append(" = ")
                    .append(field.toJavaVariableName())
                    .append(";\n");
        }

        for (final Map.Entry<String, AbstractModel> entry : model.getConstituentModelsPlusLinked().entrySet()) {
            final AbstractModel constituent = entry.getValue();

            // Need to go from constituent variable name to model variable name 
            for (final PrimitiveField f : constituent.getPrimitiveFieldsWithLinked()) {
                final PrimitiveField localField = model.getLocalField(constituent, f);
                bldr.append(2, "this.").append(localField.toJavaVariableName())
                        .append(" = ")
                        .append(constituent.toJavaVariableName())
                        .append(".")
                        .append(f.toJavaVariableName())
                        .append(";\n");
            }
            for (final PrimitiveField f : constituent.getAugmentedFields().keySet()) {
                final PrimitiveField localField = model.getLocalField(constituent, f);
                bldr.append(2, "this.").append(localField.toJavaVariableName())
                        .append(" = ")
                        .append(constituent.toJavaVariableName())
                        .append(".")
                        .append(f.toJavaVariableName())
                        .append(";\n");
            }

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
