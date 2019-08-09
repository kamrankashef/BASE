package com.base.application;

import com.base.files.FileI;
import com.base.gen.APIGen;
import com.base.gen.DLGen;
import com.base.gen.ModelGen;
import com.base.gen.SchemaGen;
import com.base.parsergen.rules.ModelAugmenterI;
import com.base.parsergen.rules.ModelTransformerI;
import com.base.model.CRUDAction;
import com.base.model.AbstractModel;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.log4j.helpers.Loader;

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
            final String propsPath = System.getenv("BASE_PROPS_PATH");
            final Configuration config = configs.properties(new File(propsPath));
            BASE_HOME = config.getString("base.home");
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

        String line = null;
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(
                p.getInputStream()));
        while ((line = stdInput.readLine()) != null) {
            LOGGER.info(">> " + line + "\n");
        }
        p.waitFor(5, TimeUnit.SECONDS);

        return p.exitValue();

    }

    public static int convertToSqlServer(final String exportDir,
            final String dest) throws IOException, InterruptedException {

        final String[] cmdArr = {
            BASE_HOME
            + "/scripts/aux_scripts"
            + "/vendor_migration/mysql_to_sql_server/schema_migrate.sh",
            exportDir,
            dest};

        final Process p = Runtime.getRuntime().exec(cmdArr);

        String line = null;
        try(BufferedReader stdInput = new BufferedReader(new InputStreamReader(
                p.getInputStream()));) {
            while ((line = stdInput.readLine()) != null) {
                System.out.println(">> " + line + "\n");
            }
        }
        p.waitFor();

        return p.exitValue();

    }

    private final ApplicationDescription application;

    public boolean buildAPI = true;
    final private String srcDir = "src";

    public ApplicationBuilder(final ApplicationDescription application) {
        this.application = application;
    }

    public Map<String, String> buildClasses() throws IOException {

        final Map<String, String> classes = new LinkedHashMap<>();

        for (final AbstractModel m : application.models) {
            if (buildAPI) {
                for (final CRUDAction action : CRUDAction.values()) {
                    final String fileName = this.srcDir
                            + "/"
                            + m.getServicePackage().replaceAll("\\.", "/")
                            + "/"
                            + action.getAPIClassName(m)
                            + ".java";
                    classes.put(fileName, APIGen.buildAPIClass(m, action));

                }
            }
            classes.put(this.srcDir + "/" + m.getModelPackage().replaceAll("\\.", "/") + "/" + m.getJavaClassName() + ".java", ModelGen.toModelClass(m));
        }
        for (final AbstractModel m : application.models) {
            if (!m.getDLMethodGenerators().isEmpty()) {
                classes.put(this.srcDir + "/" + m.getDlPackage().replaceAll("\\.", "/") + "/" + m.dlName() + ".java", DLGen.toDLClass(m));
            }
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
            final Set<ModelGen.ModelMethodGenerator> mergedModelMethods,
            final Set<DLGen.DLMethodGenerator> mergedDLMethods,
            final String mainsBuildXML,
            final String exportDir) throws IOException {

        final List<AbstractModel> elemModels
                = new LinkedList(modelMap.values());

        final List<AbstractModel> derivedModels
                = modelTransformer.getDerivedModels(org + ".derived", modelMap, modelAugmenter, mergedModelMethods, mergedDLMethods);

        final LinkedList<AbstractModel> modelsToExport = new LinkedList<>();
        modelsToExport.addAll(elemModels);
        modelsToExport.addAll(derivedModels);

        final ApplicationDescription app = new ApplicationDescription("application", org, modelsToExport);
        ApplicationBuilder appBldr = new ApplicationBuilder(app);
        appBldr.buildAPI = false;
        final Map<String, String> genFiles = appBldr.buildClasses();
        genFiles.putAll(FileI.allBuildFile(app.org));

        final Collection<AbstractModel> schemaModels = new LinkedList<>();
        schemaModels.addAll(modelsToExport);
        genFiles.put("build_mains.xml", mainsBuildXML);

        final SchemaGen schemaGen = new SchemaGen(SchemaGen.DBVendor.MYSQL);
        final String schema = schemaGen.buildSchema(app.name.toLowerCase(), schemaModels);
        if(schema.isEmpty()) {
            System.err.println("Empty schema!");
        }
        genFiles.put("sql/schema.sql", schema);
        genFiles.putAll(additionalJavaFiles);
        ApplicationBuilder.fileExporter(exportDir + "/" + app.name.toLowerCase(), genFiles);
    }

}
