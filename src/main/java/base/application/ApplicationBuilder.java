package base.application;

import base.files.FileI;
import base.gen.APIGen;
import base.gen.DLGen;
import base.gen.ModelGen;
import base.gen.SchemaGen;
import base.model.sql.DBVendorI;
import base.model.sql.MySql;
import base.parsergen.rules.ModelAugmenterI;
import base.parsergen.rules.ModelTransformerI;
import base.model.CRUDAction;
import base.model.AbstractModel;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ApplicationBuilder {


    private static String BASE_HOME;

    private static final Logger LOGGER = Logger.getLogger(ApplicationBuilder.class.getName());

    static {
        try {
            final Configurations configs = new Configurations();
            BASE_HOME = System.getProperty("user.dir");
        } catch (Exception ex) {
            System.err.println("Fix this -- exception getting thrown on tests");
            ex.printStackTrace();
        }
    }

    public static int convertToSqlPostgres(final String exportDir,
            final String dest) throws IOException, InterruptedException {

        final String[] cmdArr = {
            BASE_HOME
            + "/scripts/aux_scripts"
            + "/vendor_migration/mysql_to_postgres/schema_migrate.sh",
            exportDir,
            dest};

        final Process p = Runtime.getRuntime().exec(cmdArr);

        String line;
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(
                p.getInputStream()));
        while ((line = stdInput.readLine()) != null) {
            LOGGER.info(">> " + line + "\n");
        }
        p.waitFor(5, TimeUnit.SECONDS);

        return p.exitValue();

    }


    final private static String SRC_DIR = "src/main/java";


    public static Map<String, String> buildClasses(final Collection<AbstractModel> elemModels,
                                            final Collection<ModelGen.ModelMethodGenerator> elemModelMethods,
                                            final Collection<AbstractModel> derivedModels,
                                            final Collection<ModelGen.ModelMethodGenerator> derivedModelMethods,
                                            final Collection<DLGen.DLMethodGenerator> derivedModelDLMethods,
                                            final boolean buildAPI) {

        final Map<String, String> classes = new LinkedHashMap<>();

        for (final AbstractModel m : derivedModels) {
            if (buildAPI) {
                for (final CRUDAction action : CRUDAction.values()) {
                    final String fileName = SRC_DIR
                            + "/"
                            + m.getServicePackage().replaceAll("\\.", "/")
                            + "/"
                            + action.getAPIClassName(m)
                            + ".java";
                    classes.put(fileName, APIGen.buildAPIClass(m, action));

                }
            }
            final String destPath = SRC_DIR + "/"
                + m.getModelPackage().replaceAll("\\.", "/") + "/" + m.getJavaClassName() + ".java";
            classes.put(destPath, ModelGen.toModelClassGen2("", m, derivedModelMethods));
            classes.put(SRC_DIR + "/" + m.getDlPackage().replaceAll("\\.", "/") + "/" + m.dlName() + ".java",
                    DLGen.toDLClass2("", m, derivedModelDLMethods));
        }
        for (final AbstractModel m : elemModels) {
            final String destPath = SRC_DIR + "/"
                    + m.getModelPackage().replaceAll("\\.", "/") + "/" + m.getJavaClassName() + ".java";
            classes.put(destPath, ModelGen.toModelClassGen2("", m, elemModelMethods));
        }
        return classes;
    }

    public static void fileExporter(final String dirPrefix, final Map<String, String> files)
            throws IOException {
        for (final Map.Entry<String, String> entry : files.entrySet()) {

            final String fileName = dirPrefix + "/" + entry.getKey();
            final File f = new File(fileName);
            final File dir = new File(f.getParent());
            dir.mkdirs();
            final FileWriter fw = new FileWriter(f);
            fw.write(entry.getValue());
            fw.flush();
        }
    }

    public static void buildElementParserAndLayerModels(
            final String org,
            final Map<String, AbstractModel> modelMap,
            final Map<String, String> additionalJavaFiles,
            final ModelAugmenterI modelAugmenter,
            final ModelTransformerI modelTransformer,
            final Set<ModelGen.ModelMethodGenerator> elemModelMethods,
            final Set<ModelGen.ModelMethodGenerator> mergedModelMethods,
            final Set<DLGen.DLMethodGenerator> mergedDLMethods,
            final String mainsBuildXML,
            final DBVendorI dbVendor,
            final String exportDir) throws IOException {

        final List<AbstractModel> elemModels
                = new LinkedList(modelMap.values());

        final List<AbstractModel> derivedModels
                = modelTransformer.getDerivedModels(org + ".derived", modelMap, modelAugmenter, mergedModelMethods, mergedDLMethods);

        System.out.println("Building classes");
        final Map<String, String> genFiles = ApplicationBuilder.buildClasses(elemModels, elemModelMethods, derivedModels, mergedModelMethods, new LinkedList<>(mergedDLMethods), false);
        genFiles.putAll(FileI.allBuildFile(org));

        final SchemaGen schemaGen = new SchemaGen(dbVendor);
        final String appName = "application";
        final String schema = schemaGen.buildSchema(appName, derivedModels);
        if(schema.isEmpty()) {
            System.err.println("Empty schema!");
        }
        genFiles.put("sql/schema.sql", schema);
        genFiles.putAll(additionalJavaFiles);
        ApplicationBuilder.fileExporter(exportDir + "/" + appName, genFiles);
    }

}
