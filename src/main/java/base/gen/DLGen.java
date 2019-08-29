package base.gen;

import base.model.AbstractField;
import base.model.AbstractModel;
import base.model.PrimitiveField;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class DLGen {

    public interface DLMethodGenerator {

        String genMethod(AbstractModel model);

        Collection<String> requiredImports(final AbstractModel m, final String parentPackage);
    }

    public static String toDLClass2(final String parentPackage,
            final AbstractModel m,
            final Collection<DLGen.DLMethodGenerator> dlGenerators) {
        final SourceBuilder bldr = new SourceBuilder();
        bldr.append("package ").append(m.getExpanededDLPackage(parentPackage)).append(";\n\n");

        final Set<String> imports = new TreeSet<>();

        for (final PrimitiveField field : m.getPrimitiveFieldsWithLinked()) {
            Collections.addAll(imports, field.getType().requiredImports());
        }

        for (final PrimitiveField field : m.getAugmentedFields().keySet()) {
            Collections.addAll(imports, field.getType().requiredImports());
        }

        for (final DLGen.DLMethodGenerator dlGen : dlGenerators) {
            imports.addAll(dlGen.requiredImports(m, parentPackage));
        }

        for (final String pkg : imports) {
            bldr.append("import ").append(pkg).append(";\n");
        }

        bldr.append("\n");
        bldr.append("final public class ")
                .append(m.dlName())
                .append(" {\n\n");

        // def:TABLE_NAME
        bldr.appendlnln(1, "private final String tableName;");

        // def:Constructor
        bldr.append(1, "public ").append(m.dlName()).appendln("(final String schema) {")
                .append(2, "tableName = schema + \".").append(m.toDBName()).appendln("\";")
                .appendlnln(1, "}");

        // def:tableName()
        bldr.appendln(1, "public String tableName() {")
                .appendln(2, "return tableName;")
                .appendlnln(1, "}");

        // def:FIELDS
        bldr.append(
                1, "private static final String FIELDS\n");

        {
            boolean first = true;
            for (final AbstractField field : m.allOriginalFields()) {
                bldr.append(3, first ? "= \"" : "+ \", ").appendEscapeQuotes(field.toDBName()).append("\"\n");
                first = false;
            }
            bldr.append(3, "+ \", ").append("created_at").append("\"");
            bldr.append("\n").append(3, "+ \", ").append("modified_at").append("\"");
            bldr.append("\n").append(3, "+ \", ").append("deleted_at").append("\"");

            bldr.append(";\n");
        }

        for (final DLGen.DLMethodGenerator dlMethodGenerator : dlGenerators) {

            bldr.append("\n");
            bldr.append(dlMethodGenerator.genMethod(m));
            bldr.append("\n");
        }

        bldr.append("}\n");
        return bldr.toString();
    }


}
