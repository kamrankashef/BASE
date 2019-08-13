package base.parsegen.xml.hospitalevents;

import base.model.AbstractModel;
import base.model.PrimitiveField;
import base.model.PrimitiveType;
import base.parsergen.rules.ModelAugmenterI;
import base.parsergen.rules.ModelTransformerI;
import base.parsergen.rules.TypeSetsI;
import base.parsergen.rules.impl.StatefulTypeSetGuesser;
import base.util.AdjoinModelUtil;
import fullsuite.XMLGenTest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TestHospitalEvents extends XMLGenTest {

    @Override
    public String getOrg() {
        return "com.fakehospital";
    }

    @Override
    public String getYAMLSource() {
        return "base/parsegen/xml/hospitallog/hospital-log.yml";
    }

    @Override
    public String getBaselineDir() {
        return "expected_out/hospital_log";
    }


    @Override
    public ModelAugmenterI getModelAugmenterI() {

        // Combine localDate, localTime and localTimeZone to create a proper timestamp
        return model -> {
            final Map<PrimitiveField, String> augmentedFields = new HashMap<>();

            if (model.getJavaClassName().equals("Event")) {
                final String fieldName = "localTimeAug";
                final String def = "common.DateUtil.extract(\"yyyyMMdd hh:mm:ss.S\", localDate + \" \" + localTime, localTimeZone)";

                augmentedFields.put(
                        new PrimitiveField(fieldName, PrimitiveType.TIMESTAMP_FRACTION_3),
                        def);
            }
            return augmentedFields;
        };
    }

    @Override
    public TypeSetsI getTypeSetsI() {
        return new StatefulTypeSetGuesser();
    }

    @Override
    public ModelTransformerI getModelTransformerI() {

        final boolean preserveConstraints = false;

        return (pkg, models, modelAugmenter, mergedModelMethods, mergedDLMethods) -> {
            final PrimitiveField customAttribute = new PrimitiveField("customAttribute", PrimitiveType.TINY_TEXT);

            final PrimitiveField[] players = {
                    new PrimitiveField("_employee1ID", PrimitiveType.INT),
                    new PrimitiveField("_employee2ID", PrimitiveType.INT)};

            final List<AbstractModel> derivedModels = new LinkedList<>();
            final AdjoinModelUtil adjoinModelUtil = new AdjoinModelUtil(pkg, modelAugmenter, mergedModelMethods, mergedDLMethods, preserveConstraints);


            final String meetingName = "ZMeeting";
            final String surgeryName = "ZSurgery";

            final Map<String, AbstractModel> eventSubModels = new HashMap<>();


            {
                final String[] modelNames = {"Meeting", "Employee"};
                final String[] prefixes = {"", "Employee"};
                final AbstractModel meeting = adjoinModelUtil.adjoinModels(modelNames, prefixes, meetingName, models);
                // TODO This might not be needed any longer
                meeting.getDLMethodGenerators().clear();
                eventSubModels.put(meetingName, meeting);
            }
            {
                final String[] modelNames = {"Surgery", "Group", "Employee"};
                final String[] prefixes = {"", "Group", "Employee"};
                final AbstractModel surgery = adjoinModelUtil.adjoinModels(modelNames, prefixes, surgeryName, models);
                // TODO This might not be needed any longer
                surgery.getDLMethodGenerators().clear();
                eventSubModels.put(surgeryName, surgery);
            }

            final String[] modelNames = {"Event", "ShiftEnd", meetingName, surgeryName};
            final String[] prefixes = {"Event", "ShiftEnd", "Meeting", "Surgery"};

            // Sub-event with no sub-elements
            eventSubModels.put("ShiftEnd", models.get("ShiftEnd"));

            final AbstractModel adjoinedModel
                    = adjoinModelUtil.adjoinModels(
                    modelNames, prefixes, "Event", eventSubModels, customAttribute);

            derivedModels.add(adjoinedModel);

            return derivedModels;
        };
    }
}
