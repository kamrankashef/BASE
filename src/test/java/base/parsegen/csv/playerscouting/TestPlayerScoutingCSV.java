package base.parsegen.csv.playerscouting;

import base.gen.DLGen;
import base.gen.ModelGen;
import base.model.AbstractModel;
import base.model.ModelUtil;
import base.model.PrimitiveField;
import base.model.PrimitiveType;
import base.parsergen.rules.ModelAugmenterI;
import base.parsergen.rules.ModelTransformerI;
import base.parsergen.rules.TypeRenamerI;
import base.parsergen.rules.TypeSetsI;
import base.parsergen.rules.impl.StatefulTypeSetGuesser;
import base.util.AdjoinModelUtil;
import fullsuite.CSVGenTest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestPlayerScoutingCSV extends CSVGenTest {

    @Override
    public String getOrg() {
        return "playerscouting";
    }

    @Override
    public String getYAMLSource() {
        return "base/parsegen/csv/playerscouting/playerscouting.yml";
    }

    @Override
    public String getBaselineDir(){
        return "expected_out/player_scouting";
    }

    @Override
    public ModelAugmenterI getModelAugmenterI() {
        return (model) -> {
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
    }

    @Override
    public TypeSetsI getTypeSetsI() {
        return new StatefulTypeSetGuesser();
    }

    @Override
    public ModelTransformerI getModelTransformerI() {

        return (final String pkg,
                final Map<String, AbstractModel> models,
                final ModelAugmenterI modelAugmenter,
                final Set<ModelGen.ModelMethodGenerator> mergedModelMethods,
                final Set<DLGen.DLMethodGenerator> mergedDLMethods) -> {
            final List<AbstractModel> derivedModels = new LinkedList<>();

            ModelUtil.fieldLookup(models.get("PlayerScoutingFeed"), "offense").setPrimitiveType(PrimitiveType.MEDIUM_TEXT);
            ModelUtil.fieldLookup(models.get("PlayerScoutingFeed"), "defense").setPrimitiveType(PrimitiveType.MEDIUM_TEXT);
            ModelUtil.fieldLookup(models.get("PlayerScoutingFeed"), "knicksFit").setPrimitiveType(PrimitiveType.MEDIUM_TEXT);
            ModelUtil.fieldLookup(models.get("PlayerScoutingFeed"), "reportDate").setNullable(true);
            final AdjoinModelUtil adjoinModelUtil = new AdjoinModelUtil(pkg, modelAugmenter, mergedModelMethods, mergedDLMethods, true);

            derivedModels.add(adjoinModelUtil.adjoinFields("PlayerScoutingFeed", "PlayerScouting", models));
            return derivedModels;
        };
    }

    @Override
    public TypeRenamerI getTypeRenamerI() {
        return TypeRenamerI.SPACE_RENAMER;
    }

}
