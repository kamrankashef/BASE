package base.model.methodgenerators;

import base.gen.ModelGen;
import base.gen.SourceBuilder;
import base.model.AbstractModel;
import base.model.PrimitiveField;
import base.model.methodgenerators.partials.AugmentedPartials;
import java.util.HashSet;
import java.util.Set;

public class FromCSVGenerator implements ModelGen.ModelMethodGenerator {

    @Override
    public String genMethod(final AbstractModel model, final String parentPackage) {
        final SourceBuilder bldr = new SourceBuilder();

        bldr.append(1, "public static ")
                .append(model.getJavaClassName())
                .appendlnln(" fromRow(final CSVRecord record) throws ParseException {");

        CommonCodeBlocks.initUUIDField(bldr, 2, model).appendlnln();

        for (final PrimitiveField field : model.getPrimitiveFields()) {

            bldr.append(2, field.toJavaDeclaration())
                    .append(" = TypeExtract." + field.getPrimitiveType().extractMethod)
                    .append("(")
                    .append("record.get(")
                    .append("\"")
                    .append(field.thirdPartyIdentifier)
                    .append("\"")
                    .appendln("));");
            // Check for null?
        }
        bldr.appendln();
        AugmentedPartials.genAugmented(2, bldr, model);

        bldr.appendln().append(2, "return ").append(model.calledConstructor(2));
        bldr.appendln(";")
                .append(1, "}");

        return bldr.toString();
    }

    @Override
    public Set<String> requiredImports() {
        final Set<String> imports = new HashSet<>();
        imports.add("common.ServiceUtil");
        imports.add("common.TypeExtract");
        imports.add("java.util.Date");
        imports.add("java.text.ParseException");
        imports.add("org.apache.commons.csv.CSVRecord");
        return imports;
    }

}
