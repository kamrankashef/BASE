package base.parsegen.xml.hospitalevents;

import base.model.AbstractModel;
import base.model.PrimitiveField;
import base.model.PrimitiveType;
import base.util.AdjoinModelUtil;
import base.v3.AbstractApplicationBuilder;
import fullsuite.XMLGenTest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TestHospitalEvents extends XMLGenTest {


    final boolean preserveConstraints = false;

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
    protected String getExportDir() {
        return "/tmp/test_hospital_app";
    }

    @Override
    protected void applyOverrides(final AbstractApplicationBuilder abstractApplicationBuilder) {

        // abstractApplicationBuilder.disableConstraints();

        // Set the ModelAugmenter
        abstractApplicationBuilder.setModelAugmenterI(model -> {
            final Map<PrimitiveField, String> augmentedFields = new HashMap<>();

            if (model.getJavaClassName().equals("HospitalEvents")) {
                final String fieldName = "localTimeAug";
                final String def = "common.DateUtil.extract(\"yyyyMMdd hh:mm:ss.S\", date + \" \" + localTime, localTimeZone)";

                augmentedFields.put(
                        new PrimitiveField(fieldName, PrimitiveType.TIMESTAMP_FRACTION_3),
                        def);
            }
            return augmentedFields;
        });
        abstractApplicationBuilder.setModelTransformerI(
                (pkg, models, modelAugmenter, mergedModelMethods, mergedDLMethods) -> {
                    final PrimitiveField customAttribute = new PrimitiveField("customAttribute", PrimitiveType.TINY_TEXT);

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
                        derivedModels.add(meeting);
                    }
                    {
                        final String[] modelNames = {"Surgery", "Group", "Employee"};
                        final String[] prefixes = {"", "Group", "Employee"};
                        final AbstractModel surgery = adjoinModelUtil.adjoinModels(modelNames, prefixes, surgeryName, models);
                        // TODO This might not be needed any longer
                        surgery.getDLMethodGenerators().clear();
                        eventSubModels.put(surgeryName, surgery);
                        derivedModels.add(surgery);
                    }

                    final String[] modelNames = {"Event", "ShiftEnd", meetingName, surgeryName};
                    final String[] prefixes = {"Event", "ShiftEnd", "Meeting", "Surgery"};

                    // Models that can be adjoined as auto-built
                    eventSubModels.put("ShiftEnd", models.get("ShiftEnd"));
                    eventSubModels.put("Event", models.get("Event"));

                    final AbstractModel adjoinedModel
                            = adjoinModelUtil.adjoinModels(
                            modelNames, prefixes, "Event", eventSubModels, customAttribute);

                    derivedModels.add(adjoinedModel);

                    return derivedModels;
                });

    }


}
