package base.model.methodgenerators;

import base.files.FileI;
import base.gen.ModelGen;
import base.gen.SourceBuilder;
import base.model.ForiegnKeyField;
import base.model.AbstractModel;
import base.model.PrimitiveField;
import base.model.methodgenerators.partials.AugmentedPartials;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class FromObjectMapGenerator implements ModelGen.ModelMethodGenerator {

    final boolean strictAttributeChecks;
    final boolean softError;

    /**
     * Force constructor that takes boolean or default this to check = true
     *
     * @deprecated
     */
    @Deprecated
    public FromObjectMapGenerator() {
        this(false);
    }

    public FromObjectMapGenerator(final boolean checkUnknownAttributes) {
        this(checkUnknownAttributes, false);
    }

    private FromObjectMapGenerator(final boolean checkUnknownAttributes, final boolean softError) {
        this.strictAttributeChecks = checkUnknownAttributes;
        this.softError = softError;
    }

    public static FromObjectMapGenerator softErrorChecker() {
        return new FromObjectMapGenerator(true, true);
    }

    private static String getSourceName(final AbstractModel model, final PrimitiveField field) {
        if (model.getThirdPartyMapping().containsKey(field.getName())) {
            return model.getThirdPartyMapping().get(field.getName());
        }
        return field.thirdPartyIdentifier;
    }

    @Override
    public String genMethod(final AbstractModel model, final String parentPackage) {
        final SourceBuilder bldr = new SourceBuilder();

        if (softError) {
            bldr.appendlnln(1, "private static Set<String> DELTAS = new java.util.HashSet<>();");
        }

        bldr.append(1, "public static ")
                .append(model.getJavaClassName())
                .append(" fromObjectMap(final Map<String, Object> map");
        for (final ForiegnKeyField field : model.getFkFields()) {
            bldr.append(",\n").append(3, field.toJavaDeclaration());
        }
        bldr.append(") {\n\n");

        if (this.strictAttributeChecks) {
            bldr.appendln(2, "final Set<String> keySet = new HashSet<>(map.keySet());")
                    .appendln();
            for (final PrimitiveField field : model.getPrimitiveFields()) {

                bldr.append(2, "if (!keySet.remove(\"")
                        .append(getSourceName(model, field))
                        .appendln("\")) {");

                // TODO apply softErroring to this
                bldr.append(3, "throw new RuntimeException(")
                        .append("\"Missing attribute: '")
                        .append(getSourceName(model, field))
                        .appendln("'\");");
                bldr.appendln(2, "}");

            }
            bldr.appendln()
                    .appendln(2, "if (!keySet.isEmpty()) {");
            if (!softError) {
                bldr.append(3, "throw new RuntimeException(\"Unaccounted for attributes \" + keySet").appendln(");");
            } else {
                bldr.appendln(3, "for (final String attribute : keySet) {")
                        .appendln(4, "if (DELTAS.add(attribute)) {")
                        .appendln(5, "System.err.println(\"Unaccounted for attribute " + model.getJavaClassName() + ".\" + " + "attribute);")
                        .appendln(4, "}");
                bldr.appendln(3, "}");
            }
            bldr.appendln(2, "}")
                    .appendln();

        }
        CommonCodeBlocks.initUUIDField(bldr, 2, model).append("\n\n");

        for (final PrimitiveField field : model.getPrimitiveFields()) {

            bldr.append(2, field.toJavaDeclaration())
                    .append(" = TypeExtract." + field.getPrimitiveType().extractMethod + "FromObj(map.get(\"")
                    .append(getSourceName(model, field))
                    .append("\"));\n");
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
        imports.add(Map.class.getCanonicalName());
        imports.add(Set.class.getCanonicalName());
        imports.add(HashSet.class.getCanonicalName());

        return imports;
    }

}
