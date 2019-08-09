package base.application;

import base.dl.methodgenerators.FromRs;
import base.dl.methodgenerators.GetByGuid;
import base.gen.DLGen;
import base.model.AbstractModel;
import base.model.Model;
import base.model.PrimitiveField;
import base.model.PrimitiveType;
import base.model.methodgenerators.ConstructorGenerator;
import base.parsergen.rules.ModelAugmenterI;
import base.parsergen.rules.ModelTransformerI;
import kamserverutils.common.util.FileUtil;
import kamserverutils.common.util.StringUtil;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Using a normalized table definition, create an application to be export as
 * json.
 *
 * @author anon
 */
public class ApplicationFromTableDef {

    public static void main(final String... args) throws IOException {

        final String name = "Unnamed";
        final String org = "org.unnamed";
        final String sqlPath = "/Users/anon/Desktop/create.sql";// args[0];
        final String asStr = FileUtil.fileToString(sqlPath);

        final String[] tables = asStr.split(";");

        final Map<String, AbstractModel> models = new HashMap<>();

        for (final String table : tables) {
            if (StringUtil.isNullWhiteSpace(table)) {
                System.out.println("Skipping");

            } else {
                System.out.println("Got one");
                System.out.println(table);
                final AbstractModel model = tableDefToModel(table, org);
                models.put(model.getJavaClassName(), model);
            }
        }

        // TODO Have option to preseve original table and columns names
        // possible use a renamer interface to map names like in model
        final ApplicationDescription app = new ApplicationDescription(name, org, new LinkedList(models.values()));

//        final AbstractBuilderFromSource builder
//                = getBuilder();

        final Set<DLGen.DLMethodGenerator> dlMethods = new HashSet<>();
        dlMethods.add(new GetByGuid());
        dlMethods.add(new FromRs());
        ApplicationBuilder.buildElementParserAndLayerModels(org,
                models,
                Collections.EMPTY_MAP,
                ModelAugmenterI.EMPTY_AUGMENTER,
                ModelTransformerI.getSimplePassThroughElemTransformer(models.keySet()),
                Collections.singleton(new ConstructorGenerator()),
                dlMethods,
                "",
                "/tmp");

        System.out.println(app.toJson());

    }

    private static final String TYPE = "[\\(\\)a-zA-Z0-9_-]+";
    private static final String NAME = "[a-zA-Z0-9_-]+";
    private final static String TABLE_EXPR = ""
            + "CREATE TABLE (\"?(" + NAME + "+)\"?\\.)?"
            + "(\"?(" + NAME + "+)\"?)\\s*\\((.*)\\)";

    private final static Pattern TABLE_PATTERN = Pattern.compile(TABLE_EXPR, Pattern.DOTALL);
    private final static String COL_EXPR = "\"?(" + NAME + ")\"?\\s+(" + TYPE + ")( NULL)?";
    private final static Pattern COL_PATTERN = Pattern.compile(COL_EXPR);

    public static AbstractModel tableDefToModel(final String table, final String org) {

        final Matcher m = TABLE_PATTERN.matcher(table.trim());

        if (!m.matches()) {
            throw new RuntimeException();
        }
        final String schema = m.group(2);
        final String tableName = m.group(4);
        final String[] columns = m.group(5).split(",");

        final String unstructuredName;

        if (StringUtil.isNullOrEmptyStr(schema)) {
            unstructuredName = table;
        } else {
            unstructuredName = schema + "." + tableName;
        }

        final List<PrimitiveField> fields = new LinkedList<>();

        for (final String colStr : columns) {
            final Matcher colMatcher = COL_PATTERN.matcher(colStr.trim());
            if (!colMatcher.matches()) {
                throw new RuntimeException();
            }
            final String name = colMatcher.group(1);
            final String typeStr = colMatcher.group(2);
            final PrimitiveType type = PrimitiveType.fromString(typeStr);
            final PrimitiveField field = new PrimitiveField(name, type);
            fields.add(field);
        }

        final Model model = new Model(unstructuredName, org);
        model.addPrimitiveFields(new HashSet<>(fields));
        return model;
    }
}
