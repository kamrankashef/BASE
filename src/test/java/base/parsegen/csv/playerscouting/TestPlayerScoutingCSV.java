package base.parsegen.csv.playerscouting;

import base.dl.methodgenerators.*;
import base.gen.DLGen;
import base.gen.ModelGen;
import base.model.AbstractModel;
import base.model.ModelUtil;
import base.model.PrimitiveField;
import base.model.PrimitiveType;
import base.model.methodgenerators.ConstructorGenerator;
import base.model.methodgenerators.DerivedModelConstructorGenerator;
import base.model.methodgenerators.FromCSVGenerator;
import base.parsergen.rules.ModelAugmenterI;
import base.parsergen.rules.TypeRenamerI;
import base.util.AdjoinModelUtil;
import base.v3.AbstractApplicationBuilder;
import fullsuite.CSVGenTest;

import java.util.*;

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
    public String getBaselineDir() {
        return "expected_out/player_scouting";
    }

    @Override
    protected void applyOverrides(final AbstractApplicationBuilder abstractApplicationBuilder) {

        // Set ModelAugmenterI
        abstractApplicationBuilder.setModelAugmenterI((model) -> {
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
        });


        // Set TypeRenamerI
        abstractApplicationBuilder.setTypeRenamerI(TypeRenamerI.SPACE_RENAMER);

        // Method generators
        abstractApplicationBuilder.clearMethodGenerators();
        abstractApplicationBuilder.addMergedDLModelMethod(new InsertRaw())
                .addMergedDLModelMethod(new TruncateTable())
                .addMergedDLModelMethod(new InsertObject())
                .addMergedDLModelMethod(new InsertObjectDBUtil())
                .addMergedDLModelMethod(new InsertObjectBatch());
        abstractApplicationBuilder.addElemModelMethods(new FromCSVGenerator())
                .addElemModelMethods(new ConstructorGenerator());
        abstractApplicationBuilder.addMergedModelMethod(new ConstructorGenerator())
                .addMergedModelMethod(new DerivedModelConstructorGenerator());

        // Set ModelTransformerI
        abstractApplicationBuilder.setModelTransformerI((final String pkg,
                                                         final Map<String, AbstractModel> models,
                                                         final ModelAugmenterI modelAugmenter,
                                                         final Set<ModelGen.ModelMethodGenerator> mergedModelMethods,
                                                         final Set<DLGen.DLMethodGenerator> mergedDLMethods) -> {
            final List<AbstractModel> derivedModels = new LinkedList<>();

            ModelUtil.fieldLookup(models.get("PlayerScoutingFeed"), "offense").setPrimitiveType(PrimitiveType.MEDIUM_TEXT);
            ModelUtil.fieldLookup(models.get("PlayerScoutingFeed"), "defense").setPrimitiveType(PrimitiveType.MEDIUM_TEXT);
            ModelUtil.fieldLookup(models.get("PlayerScoutingFeed"), "knicksFit").setPrimitiveType(PrimitiveType.MEDIUM_TEXT);
            ModelUtil.fieldLookup(models.get("PlayerScoutingFeed"), "reportDate").setNullable(true);
            final AdjoinModelUtil adjoinModelUtil = new AdjoinModelUtil(pkg, true);

            derivedModels.add(adjoinModelUtil.adjoinFields("PlayerScoutingFeed", "PlayerScouting", models));
            return derivedModels;
        });

    }

}

