package samples.csv;

import base.application.ApplicationBuilder;
import base.dl.methodgenerators.*;
import base.gen.DLGen;
import base.gen.ModelGen;
import base.model.*;
import base.model.methodgenerators.ConstructorGenerator;
import base.model.methodgenerators.FromCSVGenerator;
import base.parsergen.AbstractBuilderFromSource;
import base.parsergen.CSVBuilder;
import base.parsergen.csv.CSVParserGenerator;
import base.parsergen.rules.*;
import base.parsergen.rules.impl.StatefulTypeSetGuesser;
import base.parsergen.rules.training.SourceFilesFromClasspathYaml;
import base.parsergen.rules.training.SourceFilesI;
import base.util.AdjoinModelUtil;

import java.io.IOException;
import java.util.*;

public class CSVExample {

    public static Set<ModelGen.ModelMethodGenerator> getElemModelMethods() {
        final Set<ModelGen.ModelMethodGenerator> elemModelMethods = new LinkedHashSet<>();
        elemModelMethods.add(new FromCSVGenerator());
        elemModelMethods.add(new ConstructorGenerator());
        return elemModelMethods;
    }

    public static Set<DLGen.DLMethodGenerator> getMergedDLMethods() {
        final Set<DLGen.DLMethodGenerator> mergedDLMethods = new LinkedHashSet<>();
        mergedDLMethods.add(new InsertRaw());
        mergedDLMethods.add(new TruncateTable());
        mergedDLMethods.add(new InsertObject());
        mergedDLMethods.add(new InsertObjectDBUtil());
        mergedDLMethods.add(new InsertObjectBatch());
        return mergedDLMethods;
    }

    public static void main(final String... args) throws IOException, InterruptedException {
        final String org = "com.example";

        final String exportDir = "/tmp";

        // TODO Rename class "...mentOr"
        final ModelAugmenterI modelAugmentor = (model) -> {
            final Map<PrimitiveField, String> addedFields = new HashMap<>();
            final PrimitiveField reportDateAug = new PrimitiveField("reportDateAug", PrimitiveType.DATE);
            final PrimitiveField birthDateAug = new PrimitiveField("birthDateAug", PrimitiveType.DATE);
            final PrimitiveField createdOnAug = new PrimitiveField("createdOnAug", PrimitiveType.TIMESTAMP_FRACTION_3);
            final PrimitiveField editedOnAug = new PrimitiveField("editedOnAug", PrimitiveType.TIMESTAMP_FRACTION_3);

            addedFields.put(reportDateAug, "reportDate == null ? null : common.DateUtil.extract(common.DateUtil.SIMPLE_DATE, reportDate, \"EDT\")");
            addedFields.put(birthDateAug, "birthDate == null ? null : common.DateUtil.extract(common.DateUtil.SIMPLE_DATE, birthDate, \"EDT\")");
            addedFields.put(createdOnAug, "createdOn == null ? null : common.DateUtil.extract(common.DateUtil.SIMPLE_DATE, createdOn, \"EDT\")");
            addedFields.put(editedOnAug, "editedOn == null ? null : common.DateUtil.extract(common.DateUtil.SIMPLE_DATE, editedOn, \"EDT\")");

            return addedFields;
        };


        final TypeSetsI typeSets = new StatefulTypeSetGuesser();

        final ModelTransformerI modelTransformer = (final String pkg,
                                                    final Map<String, AbstractModel> models,
                                                    final ModelAugmenterI modelAugmenter,
                                                    final Set<ModelGen.ModelMethodGenerator> mergedModelMethods,
                                                    final Set<DLGen.DLMethodGenerator> mergedDLMethods) -> {
            final List<AbstractModel> derivedModels = new LinkedList<>();

            ModelUtil.fieldLookup(models.get("RealGMFeed"), "offense").setPrimitiveType(PrimitiveType.MEDIUM_TEXT);
            ModelUtil.fieldLookup(models.get("RealGMFeed"), "defense").setPrimitiveType(PrimitiveType.MEDIUM_TEXT);
            ModelUtil.fieldLookup(models.get("RealGMFeed"), "knicksFit").setPrimitiveType(PrimitiveType.MEDIUM_TEXT);
            ModelUtil.fieldLookup(models.get("RealGMFeed"), "reportDate").setNullable(true);
            final AdjoinModelUtil adjoinModelUtil = new AdjoinModelUtil(pkg, modelAugmenter, mergedModelMethods, mergedDLMethods, true);

            derivedModels.add(adjoinModelUtil.adjoinFields("RealGMFeed", "RealgmScouting", models));
            return derivedModels;
        };


        final TypeRenamerI typeRenamer = TypeRenamerI.SPACE_RENAMER;
        final SourceFilesI sourceFiles = SourceFilesFromClasspathYaml.constructFromCode(CSVExample.class, "config.yml");

        final boolean allowMissingAttributes = false;
        final Set<Constraint> constraints = Collections.singleton(Constraint.NOT_NULL);

        final ParseRuleSet parseRuleSet = new ParseRuleSet(org,
                modelAugmentor,
                getElemModelMethods(),
                typeSets,
                typeRenamer,
                sourceFiles,
                allowMissingAttributes,
                constraints);

        final AbstractBuilderFromSource builder
                = AbstractBuilderFromSource.run(new CSVBuilder(parseRuleSet, new CSVParserGenerator()));

        final boolean autoGenTypeSet = true;

        if (autoGenTypeSet) {
            for (final AbstractModel model : builder.getElemModels()) {
                for (final PrimitiveField f : model.getSimplePrimitiveFields()) {
                    final String className = f.getJavaClassName();
                    final String type;
                    if (className.endsWith("Id")) {
                        type = "LONG";
                    } else if (className.endsWith("Score")) {
                        type = "INT";
                    } else {
                        type = "STRING";
                    }
                    System.out.println(type + "_SET.add(\"" + className + "\"); // Nullable: " + f.nullable());
                }
            }

        }



        System.out.println(builder.getElemModelMap());
        ApplicationBuilder.buildElementParserAndLayerModels(org,
                builder.getElemModelMap(),
                builder.getGenedParsers(),
                modelAugmentor,
                modelTransformer,
                getElemModelMethods(),
                getMergedDLMethods(),
                builder.getAntEntries().iterator().next(),
                exportDir);

        ApplicationBuilder.convertToSqlServer(
               exportDir
                        + "/application/sql/schema.sql",
               exportDir
                        + "/application/sql/schema.sql");

    }

}
