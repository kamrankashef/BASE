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
                    final AdjoinModelUtil adjoinModelUtil = new AdjoinModelUtil(pkg, preserveConstraints);

                    // Use the echoed model heirachy to determine how to stitch Elem Models into Derived Models
                    // Xml:
                    //    HospitalEvents: Date, EasternTimeZoneTime, Filename, Id, LocalTime, LocalTimeZone
                    //       ErRotation: Hospital, Id
                    //          Group: Code, Id, Name
                    //             EmployeesEmpty:
                    //                Employee: FName, Id, LName
                    //          Event: EasternTimeZoneTime, Id, LocalDate, LocalTime, Name, Number
                    //             ShiftEnd: Summary
                    //             Meeting: Description
                    //                EmployeeMeeting: Id
                    //             Surgery: Floor, RoomNumber
                    //                GroupSurgery: Id, Role
                    //                   EmployeeGroupSurgery: Id
                    final String[] prefixes = {"Event", "ShiftEnd", "Meeting", "MeetingEmployee", "Surgery", "SurgeryGroup", "SurgeryEmployee"};
                    final String[] modelNames = {"Event", "ShiftEnd", "Meeting", "EmployeeMeeting", "Surgery", "GroupSurgery", "EmployeeGroupSurgery"};

                    final AbstractModel adjoinedModel
                            = adjoinModelUtil.adjoinModels(
                            modelNames, prefixes, "Event", models, customAttribute);

                    derivedModels.add(adjoinedModel);

                    return derivedModels;
                });

    }


}
