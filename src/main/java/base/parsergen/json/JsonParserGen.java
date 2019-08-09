package base.parsergen.json;

import base.application.ApplicationBuilder;
import base.dl.methodgenerators.FromRs;
import base.dl.methodgenerators.GetByGuid;
import base.dl.methodgenerators.HardDeleteByGuid;
import base.dl.methodgenerators.InsertObject;
import base.dl.methodgenerators.InsertRaw;
import base.gen.DLGen;
import base.gen.ModelGen;
import base.model.AbstractModel;
import base.model.Constraint;
import base.model.Model;
import base.model.PrimitiveField;
import base.model.PrimitiveType;
import base.model.methodgenerators.ConstructorGenerator;
import base.model.methodgenerators.DerivedModelConstructorGenerator;
import base.model.methodgenerators.FromObjectMapGenerator;
import base.model.methodgenerators.ToStringGenerator;
import base.parsergen.rules.ModelAugmenterI;
import base.parsergen.rules.ModelTransformerI;
import com.google.gson.Gson;
import kamserverutils.common.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonParserGen {

    private static final String SCHEMA = "nhl_web";

    final static String ORG = "com.msg.nhl";

    // Loop over exported JSON files and attempt to build model
    private static final String FILES_HOME = "/tmp/nhl_json";

    final static ModelAugmenterI MODEL_AUGMENTER = new ModelAugmenterI() {
        @Override
        public Map<PrimitiveField, String> getAugmentedFields(final AbstractModel model) {
            final Map<PrimitiveField, String> addedFields = new HashMap<>();
            if (model.getJavaClassName().endsWith("GameElem")) {
                final PrimitiveField _gameId = new PrimitiveField("_internalGameId", Constraint.NOT_NULL, PrimitiveType.LONG);
                addedFields.put(_gameId, "common.NHLUtils.normalizedGameId(gameId)");
                final PrimitiveField _season = new PrimitiveField("_season", Constraint.NOT_NULL, PrimitiveType.TINY_TEXT);
                addedFields.put(_season, "common.NHLUtils.normalizedSeasonId(gameId)");
            }
            return addedFields;
        }
    };

    public static void main(final String... args) throws IOException {

        final Map<String, AbstractModel> modelMap = new HashMap<>();

        for (final File file : new File(FILES_HOME).listFiles()) {
            final String modelName = SCHEMA + "." + file.getName().replace(".json", "") + "Elem";

            if (modelName.contains("Franchise")) {
//                System.out.println("Skipping " + modelName);
                continue;
            }
            System.out.println("Building model " + modelName);
            final String fileAsStr
                    = FileUtil.fileToString(file.getAbsolutePath());
            final Map<String, Object> asMap
                    = new Gson().fromJson(fileAsStr, Map.class);

            final List<Map<String, Object>> data = (List<Map<String, Object>>) asMap.get("data");

            final Map<String, FieldDescription> fields = new HashMap<>();

            for (final Map<String, Object> record : data) {

                // For each record if mark any items not previously seen as nullable
                for (final Map.Entry<String, FieldDescription> field : fields.entrySet()) {
                    if (record.get(field.getKey()) == null) {
                        field.getValue().setNullable();
                    }
                }

                for (final Map.Entry<String, Object> field : record.entrySet()) {
                    final String fieldName = field.getKey();
                    final boolean seenPreviously = fields.containsKey(fieldName);
                    final boolean previouslyMissed = !seenPreviously && !fields.isEmpty();
                    final FieldDescription entry;
                    if (!seenPreviously) {
                        entry = new FieldDescription(fieldName);
                        fields.put(fieldName, entry);
                        if (previouslyMissed) {
                            entry.setNullable();
                        }
                    } else {
                        entry = fields.get(fieldName);
                    }
                    entry.applyValue(field.getValue());
                }
            }

            final PrimitiveField[] primitiveFields = new PrimitiveField[fields.size()];

            int i = 0;

            for (final FieldDescription entry : fields.values()) {
                final PrimitiveField pf = new PrimitiveField(entry.name, entry.type);
                pf.setNullable(entry.nullable);

                if (entry.name.equals("seasonId")
                        || entry.name.equals("gameId")
                        || entry.name.equals("playerId")
                        || entry.name.equals("franchiseId")
                        || entry.name.equals("teamId")) {
                    pf.setNullable(false);
                }
                primitiveFields[i++] = pf;
            }

            final AbstractModel model = new Model(modelName, ORG, primitiveFields);
            model.addModelMethodGenerator(new ConstructorGenerator());
            model.addModelMethodGenerator(new FromObjectMapGenerator());
            model.addModelMethodGenerator(new ToStringGenerator());
            model.applyAugmented(MODEL_AUGMENTER);
            modelMap.put(modelName, model);
        }

        final Map<String, String> additionalJavaFiles = Collections.EMPTY_MAP;
        final ModelTransformerI modelTransformer = ModelTransformerI.getSimplePassThroughElemTransformer(modelMap.keySet(),
                new PrimitiveField("_query_year", Constraint.NOT_NULL, PrimitiveType.INT),
                new PrimitiveField("_game_type", Constraint.NOT_NULL, PrimitiveType.INT),
                new PrimitiveField("_game_type_name", Constraint.NOT_NULL, PrimitiveType.TINY_TEXT));
        final Set<ModelGen.ModelMethodGenerator> mergedModelMethods = new HashSet<>();
        mergedModelMethods.add(new DerivedModelConstructorGenerator());
        mergedModelMethods.add(new ConstructorGenerator());
        mergedModelMethods.add(new ToStringGenerator());
        final Set<DLGen.DLMethodGenerator> mergedDLMethods = new LinkedHashSet<>();

        mergedDLMethods.add(new InsertObject());
        mergedDLMethods.add(new FromRs());
        mergedDLMethods.add(new GetByGuid());
        mergedDLMethods.add(new HardDeleteByGuid()); // Need to change to delete by special keys
        mergedDLMethods.add(new InsertRaw());
        // (team_id, season_id)
        // (team_id, game_id)
        // (player_id, season_id)
        // (player_id, game_id)
        final String mainsBuildXML = "";
        final String exportDir = "/tmp";

        ApplicationBuilder.buildElementParserAndLayerModels(ORG,
                modelMap,
                additionalJavaFiles,
                MODEL_AUGMENTER,
                modelTransformer,
                mergedModelMethods,
                mergedDLMethods,
                mainsBuildXML,
                exportDir);
    }
}
