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

public abstract class DelimitedGenerator implements ModelGen.ModelMethodGenerator {

    protected abstract String getEscapedDelimiter();

    protected abstract String getType();

    @Override
    public String genMethod(final AbstractModel model, final String parentPackage) {
        final SourceBuilder bldr = new SourceBuilder();

        bldr.append(1, "public static ")
                .append(model.getJavaClassName())
                .append(" from" + getType() + "Row(final Map<String, Integer> colMapping, final String rowStr) {\n")
                .appendln("");

        CommonCodeBlocks.initUUIDField(bldr, 2, model).append("\n\n");

        bldr.append(2, "final String[] rowData = rowStr.split(\"" + getEscapedDelimiter() + "\", -1);\n")
                .appendln("");
        bldr.append(2, "if(rowData.length != ").append("" + model.getPrimitiveFields().size())
                .append(") {\n")
                .append(3, "throw new RuntimeException(\"Unexpected row width: \" + rowData.length + \" !="
                        + " \" + " + model.getPrimitiveFields().size() + ");\n")
                .append(2, "}\n")
                .appendln("");

        for (final PrimitiveField field : model.getPrimitiveFields()) {

            bldr.append(2, field.toJavaDeclaration())
                    .append(" = TypeExtract." + field.getPrimitiveType().extractMethod)
                    .append("(")
                    .append("rowData[")
                    .append("colMapping.get(")
                    .append("\"")
                    .append(field.thirdPartyIdentifier)
                    .append("\"")
                    .append(")]")
                    .append(");\n");
            // Check for null?
        }

        AugmentedPartials.genAugmented(2, bldr, model);

        bldr.append("\n").append(2, "return ").append(model.calledConstructor(2));
        bldr.append(";\n").append(1, "}");

        return bldr.toString();
    }

    @Override
    public Set<String> requiredImports() {
        final Set<String> imports = new TreeSet<>();
        imports.add(FileI.COMMON_PKG + ".ServiceUtil");
        imports.add(FileI.COMMON_PKG + ".TypeExtract");
        imports.add("com.google.common.base.Splitter");
        imports.add(Map.class.getCanonicalName());

        return imports;
    }

}
