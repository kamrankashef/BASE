package examples;

import base.dl.methodgenerators.DeleteById;
import base.dl.methodgenerators.InsertObject;
import base.dl.methodgenerators.InsertRaw;
import base.dl.methodgenerators.TruncateTable;
import base.model.AbstractModel;
import base.model.PrimitiveField;
import base.model.PrimitiveType;
import base.model.methodgenerators.AttributeBasedFromElemMethodGenerator;
import base.model.methodgenerators.ConstructorGenerator;
import base.model.methodgenerators.DerivedModelConstructorGenerator;
import base.parsergen.rules.SourceFiles;
import base.util.AdjoinModelUtil;
import base.v3.AbstractApplicationBuilder;
import base.v3.XMLApplicationBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Take an XML document and build application
 */
public class Basic {


    public static void main(String[] args) throws IOException, InterruptedException {

        // The only three required properties
        // 1. org - package for generated classes
        // 2. exportDir - where project should be generated - careful, this will wipe out the directory, so don't use $HOME
        // 3. sourceFiles - StreamGetters that provide InputStreams to the sample datafiles
        final String org = "com.example";
        final String exportDir = System.getProperty("user.home") + "/sample_base_project";
        final SourceFiles sourceFiles = new SourceFiles();

        {
            final String pathToInputFile = args[0];
            SourceFiles.StreamGetter streamGetter = () -> new FileInputStream(pathToInputFile);
            sourceFiles.addSourceFile("ExampleType", streamGetter);
        }

        // Create an XML builder
        final AbstractApplicationBuilder appBuilder = new XMLApplicationBuilder("com.example", sourceFiles, exportDir);


        // Auto-generate methods for the generated classes
        appBuilder
                .addElemModelMethods(new AttributeBasedFromElemMethodGenerator())
                .addElemModelMethods(new ConstructorGenerator())
                .addMergedModelMethod(new ConstructorGenerator())
                .addMergedModelMethod(new DerivedModelConstructorGenerator())
                .addMergedDLModelMethod(new InsertRaw())
                .addMergedDLModelMethod(new InsertObject())
                .addMergedDLModelMethod(new TruncateTable())
                .addMergedDLModelMethod(new DeleteById(new PrimitiveField("eventId", PrimitiveType.TEXT)));

        // Set the ModelAugmenter
        appBuilder.setModelAugmenterI(model -> {
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

        // *********** COMBINE ElemModels to create DerivedModels
        appBuilder.setModelTransformerI(
                (pkg, models, modelAugmenter, mergedModelMethods, mergedDLMethods) -> {
                    final PrimitiveField customAttribute = new PrimitiveField("customAttribute", PrimitiveType.TINY_TEXT);

                    final List<AbstractModel> derivedModels = new LinkedList<>();
                    final AdjoinModelUtil adjoinModelUtil = new AdjoinModelUtil(pkg, false);

                    // Prefixed with Z to not collide with inferred Meeting and Surgery types
                    // But it doesn't matter because both of these will be combined into the
                    // Event type.
                    final String meetingName = "ZMeeting";
                    final String surgeryName = "ZSurgery";

                    final Map<String, AbstractModel> eventSubModels = new HashMap<>();


                    {
                        // Combine the Meeting and Employee models
                        final String[] modelNames = {"Meeting", "Employee"};
                        // Do not prefix the Meeting attributes in the Model, but prefix the Employee attributes with
                        // "employee".  E.g. Employee.id becomes ZMeeting.employeeId
                        final String[] prefixes = {"", "Employee"};
                        final AbstractModel meeting = adjoinModelUtil.adjoinModels(modelNames, prefixes, meetingName, models);
                        // This keeps a DL for this intermediate Model from being generated
                        meeting.getDLMethodGenerators().clear();
                        eventSubModels.put(meetingName, meeting);
                        derivedModels.add(meeting);
                    }
                    {
                        final String[] modelNames = {"Surgery", "Group", "Employee"};
                        final String[] prefixes = {"", "Group", "Employee"};
                        final AbstractModel surgery = adjoinModelUtil.adjoinModels(modelNames, prefixes, surgeryName, models);
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
        // Export Maven project with Elem, Merged Model and data layer classes along with supporting class and SQL
        // schema file defining the backing tables for the Merged Models
        appBuilder.build();
        System.out.println("In '" + exportDir + "/application' perform mvn compile");
    }
}
