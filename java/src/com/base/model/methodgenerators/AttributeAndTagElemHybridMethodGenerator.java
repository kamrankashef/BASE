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
public class AttributeAndTagElemHybridMethodGenerator implements ModelGen.ModelMethodGenerator {

    private final static String TAG_VALUE_PREFIX = "fromtag";
    private final static String ATTR_VALUE_PREFIX = "fromAttr";

    @Override
    public String genMethod(final AbstractModel model, final String parentPackage) {
        final SourceBuilder bldr = new SourceBuilder();

        bldr.append(1, "public static ")
                .append(model.getJavaClassName())
                .appendln(" fromAttrAndElem(final Element elem) throws Exception {\n");

        bldr.appendln(2, "final Set<String> attributeTracker = ServiceUtil.extractAttributeName(elem.attributes());");

        CommonCodeBlocks.initUUIDField(bldr, 2, model).append("\n\n");

        for (final PrimitiveField field : model.getPrimitiveFields()) {

            final String callGetElem = "elem.attr(\"" + field.thirdPartyIdentifier + "\")";

            // Lifted from attribute version
            bldr.indent(2)
                    .append("final " + field.toJavaType() + " ")
                    .append(field.toJavaVariableName(ATTR_VALUE_PREFIX))
                    .append(" = ")
                    .append("TypeExtract.")
                    .append(field.getPrimitiveType().extractMethod)
                    .append("(")
                    .append(callGetElem)
                    .append(")")
                    .appendln(";");

            // Lifted from tag version
            final String callGetSubElem
                    = " = "
                    + "TypeExtract." + field.getPrimitiveType().extractMethod
                    + "(ElemUtil.getFirstSelectedText(elem,"
                    + " \"" + field.thirdPartyIdentifier + "\"))";
            bldr.indent(2)
                    .append("final " + field.toJavaType() + " ")
                    .append(field.toJavaVariableName(TAG_VALUE_PREFIX))
                    .append(callGetSubElem)
                    .appendln(";");

            // Take either one that is not null
            bldr.indent(2)
                    .append(field.toJavaDeclaration()).append(" = ")
                    .append(field.toJavaVariableName(ATTR_VALUE_PREFIX))
                    .append(" == null ? ")
                    .append(field.toJavaVariableName(TAG_VALUE_PREFIX))
                    .append(" : ")
                    .append(field.toJavaVariableName(ATTR_VALUE_PREFIX))
                    .appendln(";");

            bldr.append(2, "if (")
                    .append(field.toJavaVariableName())
                    .appendln(" == null" + (field.nullable() ? " && " + !field.nullable() : "") + ") {")
                    .append(3, "throw new java.lang.RuntimeException(\"Got null value for ")
                    .append(field.toJavaVariableName())
                    .appendln("\");").appendln(2, "}");

            bldr.append(2, "attributeTracker.remove(\"").append(field.thirdPartyIdentifier).appendln("\");\n");

        }

        // Check if there are unaccounted for attributes
        bldr.appendln(2, "if (!attributeTracker.isEmpty()) {");
        bldr.appendln(3, "System.out.println(elem);");
        bldr.appendln(3, "throw new java.lang.RuntimeException(\"Unaccounted for attributes: \" + attributeTracker);");
        bldr.appendln(2, "}\n");

        AugmentedPartials.genAugmented(2, bldr, model);

        bldr.append(2, "return ").append(model.calledConstructor(3)).appendln(";");
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
        imports.add(org.jsoup.nodes.Element.class.getCanonicalName());
        imports.add(FileI.COMMON_PKG + ".ElemUtil");

        return imports;
    }

}
