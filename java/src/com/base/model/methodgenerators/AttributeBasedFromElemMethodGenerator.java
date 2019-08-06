package com.base.model.methodgenerators;

import com.base.files.FileI;
import com.base.gen.ModelGen;
import com.base.gen.SourceBuilder;
import com.base.model.AbstractModel;
import com.base.model.PrimitiveField;
import com.base.model.methodgenerators.partials.AugmentedPartials;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ModelGen.ModelMethodGenerator.class)
public class AttributeBasedFromElemMethodGenerator implements ModelGen.ModelMethodGenerator {

    @Override
    public String genMethod(final AbstractModel model, final String parentPackage) {
        final SourceBuilder bldr = new SourceBuilder();

        bldr.append(1, "public static ")
                .append(model.getJavaClassName())
                .append(" fromElem(final Element elem) throws Exception {\n\n");

        bldr.append(2, "final Set<String> attributeTracker = ServiceUtil.extractAttributeName(elem.attributes());\n");

        CommonCodeBlocks.initUUIDField(bldr, 2, model).append("\n\n");

        for (final PrimitiveField field : model.getPrimitiveFields()) {

            final String callGetElem = "elem.attr(\"" + field.thirdPartyIdentifier + "\")";

            bldr.indent(2)
                    .append(field.toJavaDeclaration())
                    .append(" = TypeExtract.")
                    .append(field.getPrimitiveType().extractMethod)
                    .append("((String) ")
                    .append(callGetElem)
                    .append(");\n");
            bldr.append(2, "if (")
                    .append(field.toJavaVariableName())
                    .append(" == null" + (field.nullable() ? " && " + !field.nullable() : "") + ") {\n").append(3, "throw new java.lang.RuntimeException(\"Got null value for ")
                    .append(field.toJavaVariableName())
                    .append("\");\n").append(2, "}\n");

            bldr.append(2, "attributeTracker.remove(\"").append(field.thirdPartyIdentifier).append("\");\n\n");

        }

        // Check if there are unaccounted for attributes
        bldr.append(2, "if (!attributeTracker.isEmpty()) {\n");
        bldr.append(3, "throw new java.lang.RuntimeException(\"Unaccounted for attributes: \" + attributeTracker);\n");
        bldr.append(2, "}\n\n");

        AugmentedPartials.genAugmented(2, bldr, model);

        bldr.append(2, "return ").append(model.calledConstructor(3)).append(";\n\n");

        bldr.append(1, "}");

        return bldr.toString();
    }

    @Override
    public Set<String> requiredImports() {
        final Set<String> imports = new TreeSet<>();
        imports.add(org.jsoup.nodes.Element.class.getCanonicalName());
        imports.add(FileI.COMMON_PKG + ".TypeExtract");
        imports.add(Set.class.getCanonicalName());
        imports.add(FileI.COMMON_PKG + ".ServiceUtil");

        return imports;
    }

}
