package base.model.methodgenerators;

import base.files.FileI;
import base.gen.ModelGen;
import base.gen.SourceBuilder;
import base.model.AbstractModel;
import base.model.PrimitiveField;
import base.model.methodgenerators.partials.AugmentedPartials;

import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

public class FromTagAttrsMethodGenerator implements ModelGen.ModelMethodGenerator {

    final Function<String, String> elemSelector;

    public static FromTagAttrsMethodGenerator getSelectDirectChildrenOnlyInstance() {
        return new FromTagAttrsMethodGenerator(s -> "> " + s);
    }

    public FromTagAttrsMethodGenerator(final Function<String, String> elemSelector) {
        this.elemSelector = elemSelector;
    }

    @Deprecated 
    // Either make direct children the default or remove this,
    // or refactor it into a factor method
    public FromTagAttrsMethodGenerator() {
        this(s -> s);
    }

    @Override
    public String genMethod(final AbstractModel model, final String parentPackage) {
        final SourceBuilder bldr = new SourceBuilder();

        bldr.append(1, "public static ")
                .append(model.getJavaClassName())
                .append(" fromElem(final Element elem) throws Exception {\n\n");

        CommonCodeBlocks.initUUIDField(bldr, 2, model).append("\n\n");

        for (final PrimitiveField field : model.getPrimitiveFields()) {

            final String callGetElem =
                    "ElemUtil.getFirstSelectedText(elem,"
                    + " \"" 
                    + elemSelector.apply(field.thirdPartyIdentifier).replace("\"", "\\\"")
                    + "\")";

            bldr.indent(2)
                    .append(field.toJavaDeclaration())
                    .append(" = TypeExtract.")
                    .append(field.getPrimitiveType().extractMethod)
                    .append("(")
                    .append(callGetElem)
                    .append(");\n");
            bldr.append(2, "if (")
                    .append(field.toJavaVariableName())
                    .append(" == null && ").append("" + !field.nullable()).append(") {\n").append(3, "throw new java.lang.RuntimeException(\"Got null value for ")
                    .append(field.toJavaVariableName())
                    .append("\");\n").append(2, "}\n\n");

        }

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
        imports.add(FileI.COMMON_PKG + ".ServiceUtil");
        imports.add(FileI.COMMON_PKG + ".ElemUtil");

        return imports;
    }

}
