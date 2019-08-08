package com.base.appliation.gen2;

import com.base.application.ApplicationBuilder;
import com.base.files.FileI;
import com.base.gen.DLGen;
import com.base.gen.ModelGen;
import com.base.gen.SchemaGen;
import com.base.model.AbstractModel;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates exported application and managed deployment of models / parsers.
 */
public class DeploymentMananger {

    final SchemaGen schemaGen;

    public DeploymentMananger(final SchemaGen.DBVendor dbVenor) {
        schemaGen = new SchemaGen(dbVenor);
    }

    public static boolean clobber(final ApplicationSettings appSettings) {
        return com.base.util.FileUtil.deleteDir(appSettings.applicationDirectory());
    }

    public static void initWithClobber(final ApplicationSettings appSettings) throws IOException {
        clobber(appSettings);
        init(appSettings);
    }

    public static void init(final ApplicationSettings appSettings) throws IOException {

        final Map<String, String> genFiles = new HashMap<>();
        if (appSettings.applicationDirectory().exists()) {
            throw new IllegalStateException("Application deployment already exists :'" + appSettings.applicationDirectory() + "'");
        }

        genFiles.putAll(FileI.allBuildFile(appSettings.getOrg()));
        genFiles.put("build_mains.xml", new BuildMainsManager().toString());
        ApplicationBuilder.fileExporter(appSettings.applicationDirectory().getPath(), genFiles);
    }

    public static void setMain(
            final ApplicationSettings appSettings,
            final String parserName,
            final String parser,
            final String description) throws IOException {
        final Map<String, String> sourceFiles = new HashMap<>();

        final BuildMainsManager buildManager = new BuildMainsManager(appSettings.applicationDirectory() + "/build_mains.xml");

        // Make build_main.xml entry
        buildManager.addReplaceTarget(parserName, description);

        sourceFiles.put("build_mains.xml", buildManager.toString());
        sourceFiles.put("src/main/" + parserName + ".java", parser);

        ApplicationBuilder.fileExporter(appSettings.applicationDirectory().getPath(), sourceFiles);
    }

    // TODO Add functionality to wipe out portions of old code
    public void addSourcefiles(
            final ApplicationSettings appSettings,
            final ModelsGroup modelsGroup) throws IOException {
        final Map<String, String> sourceFiles = new HashMap<>();

        for (final AbstractModel model : modelsGroup.models) {
            if (!modelsGroup.getModelGeneratorsInstances().isEmpty()) {
                // 2 - Generate models
                final String modelStr = ModelGen.toModelClassGen2(appSettings.getOrg(), model, modelsGroup.getModelGeneratorsInstances());
                final String modelPath = destination("src/", model.getExpanededModelPackage(appSettings.getOrg()), model.getJavaClassName());
                sourceFiles.put(modelPath, modelStr);
            }
            if (!modelsGroup.getDLMethodGeneratorsInstances().isEmpty()) {
                // 3 - Generate DL
                final String dlStr = DLGen.toDLClass2(appSettings.getOrg(), model, modelsGroup.getDLMethodGeneratorsInstances());
                final String dlPath = destination("src", model.getExpanededDLPackage(appSettings.getOrg()), model.dlName());
                sourceFiles.put(dlPath, dlStr);

                // 4 - Generate Schema
                final String sqlDef = schemaGen.toSQLDef(model);
                final String sqlPath = "sql/create_table_" + model.toDBName() + ".sql";
                sourceFiles.put(sqlPath, sqlDef);
            }
        }

        // 5 - Apply to application
        ApplicationBuilder.fileExporter(
                appSettings.applicationDirectory().getPath(),
                sourceFiles);
    }

    public static void main(String[] args) throws IOException {
//                        IOUtil.debug(appSettings.getName());
//                IOUtil.debug(appSettings.getOrg());
//                IOUtil.debug(appSettings.applicationDirectory().toString());
//                IOUtil.debug(appSettings.applicationParentDirectory().toString());
//                DeploymentMananger.initWithClobber(appSettings);
        DeploymentMananger.initWithClobber(new ApplicationSettings("SECData", "io.dataswitch", "/Users/anon/Desktop"));
    }

    public static String destination(
            final String srcDir,
            final String _package,
            final String className) {
        return srcDir + "/"
                + _package.replaceAll("\\.", "/")
                + "/" + className
                + ".java";
    }
}
