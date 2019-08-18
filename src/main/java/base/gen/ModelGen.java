package base.gen;

import base.model.AbstractField;
import base.model.AbstractModel;
import base.model.PrimitiveField;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ModelGen {

    public static interface ModelMethodGenerator {

        public String genMethod(AbstractModel model, final String parentPackage);

        public Set<String> requiredImports();
    }

    public static String toModelClassGen2(
            final String parentPackage,
            final AbstractModel model,
            final List<ModelGen.ModelMethodGenerator> modelGenerators) {
        final String _package = model.getExpanededModelPackage(parentPackage);
        final Set<String> imported = new HashSet<>();
        final SourceBuilder bldr = new SourceBuilder();
        bldr.append("package ").append(_package).append(";\n\n");
        final Set<String> imports = new TreeSet<>();

        for (final PrimitiveField field : model.getPrimitiveFieldsWithLinked()) {
            for (final String pkg : field.getType().requiredImports()) {
                imports.add(pkg);
            }
        }

        for (final PrimitiveField field : model.getAugmentedFields().keySet()) {
            for (final String pkg : field.getType().requiredImports()) {
                imports.add(pkg);
            }
        }

        for (final ModelMethodGenerator modGen : modelGenerators) {
            imports.addAll(modGen.requiredImports());
        }

        for (final PrimitiveField field : model.getPrimitiveFieldsWithLinked()) {
            imports.addAll(Arrays.asList(field.getPrimitiveType().requiredImports()));
        }

        for (final String pkg : imports) {
            bldr.append("import ").append(pkg).appendln(";");
        }

        bldr.appendln();

        bldr.append("final public class ")
                .append(model.getJavaClassName())
                .appendln(" {")
                .appendln();

        // Member var declaration
        for (final AbstractField field : model.allOriginalFields()) {
            bldr.append(1, "public ").append(field.toJavaDeclaration()).appendln(";");
        }

        bldr.appendln();
        for (final ModelMethodGenerator modelMethodGenerator : modelGenerators) {
            bldr.append(1, "// ").appendln(modelMethodGenerator.getClass().getSimpleName())
                    .append(modelMethodGenerator.genMethod(model, parentPackage))
                    .appendlnln();
        }

        bldr.append("\n");
        bldr.append("}\n");
        return bldr.toString();
    }

    public static String toModelClass(final AbstractModel model) throws IOException {

        return toModelClassGen2("",
                model,
                model.getModelGenerators());

    }

}
