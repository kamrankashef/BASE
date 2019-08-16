package fullsuite;

import base.application.ApplicationBuilder;
import base.application.gen2.AbstractBASEConfig;
import base.dl.methodgenerators.InsertObject;
import base.dl.methodgenerators.InsertRaw;
import base.gen.DLGen;
import base.gen.ModelGen;
import base.model.AbstractModel;
import base.model.Constraint;
import base.model.PrimitiveField;
import base.model.methodgenerators.AttributeBasedFromElemMethodGenerator;
import base.model.methodgenerators.ConstructorGenerator;
import base.model.methodgenerators.DerivedModelConstructorGenerator;
import base.parsergen.AbstractBuilderFromSource;
import base.parsergen.XMLBuilder;
import base.parsergen.rules.ParseRuleSet;
import base.parsergen.rules.SourceFiles;
import base.parsergen.rules.TypeRenamerI;
import base.util.FileUtil;
import base.workflow.Helpers;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


public abstract class XMLGenTest extends AbstractBASEConfig {


    public abstract String getBaselineDir();

    protected SourceFiles getSourceFiles() throws IOException {
        final String yamlAsString = Helpers.resourceAsString(getClass(),getYAMLSource());
        final Map<String, Object> config = (Map<String, Object>) new Yaml().load(yamlAsString);
        final String rootDir = (String) config.get("rootDir");
        final SourceFiles sourceFiles = new SourceFiles(rootDir);

        for(final Map<String, String> sourceFileAsMap : (Collection<Map<String, String>>) config.get("sourceFiles")) {

            final String fileName = sourceFileAsMap.get("fileName");
            final String type = sourceFileAsMap.get("type");

            sourceFiles.addSourceFile(fileName, type, () -> {
                final String resourcePath = rootDir + "/" + fileName;
                return XMLGenTest.class.getClassLoader().getResourceAsStream(resourcePath);
            });
        }

        return sourceFiles;
    }

    protected AbstractBuilderFromSource getBuilder() throws IOException {
        return AbstractBuilderFromSource.run(new XMLBuilder(
                new ParseRuleSet(getOrg(),
                        getModelAugmenterI(),
                        getElemModelMethods(),
                        getTypeSetsI(),
                        getTypeRenamerI(),
                        getSourceFiles(),
                        allowMissingAttributes(),
                        getConstraints())
        ));
    }

    public TypeRenamerI getTypeRenamerI() {
        return TypeRenamerI.DEFAULT_RENAMER;
    }

    public Set<ModelGen.ModelMethodGenerator> getElemModelMethods() {
        final Set<ModelGen.ModelMethodGenerator> elemModelMethods = new LinkedHashSet<>();
        elemModelMethods.add(new AttributeBasedFromElemMethodGenerator());
        elemModelMethods.add(new ConstructorGenerator());
        return elemModelMethods;
    }

    public Set<ModelGen.ModelMethodGenerator> getMergedModelMethods() {
        final Set<ModelGen.ModelMethodGenerator> mergedModelMethods = new LinkedHashSet<>();
        mergedModelMethods.add(new ConstructorGenerator());
        mergedModelMethods.add(new DerivedModelConstructorGenerator());
        return mergedModelMethods;
    }

    public Set<DLGen.DLMethodGenerator> getMergedDLMethods() {
        final Set<DLGen.DLMethodGenerator> mergedDLMethods = new LinkedHashSet<>();
        mergedDLMethods.add(new InsertRaw());
        mergedDLMethods.add(new InsertObject());
        return mergedDLMethods;
    }

    @Test
    final public void runTest() throws FileNotFoundException, InterruptedException, IOException {

        final String exportDir = getExportDir();
        System.out.println("Export dir is " + exportDir);
        FileUtil.deleteDir(new File(exportDir));

        final String baselineDir = getBaselineDir();

        final AbstractBuilderFromSource builder
                = getBuilder();

        if (getAutoGenTypeSet()) {
            for (final AbstractModel model : builder.getElemModels()) {
                for (final PrimitiveField f : model.getSimplePrimitiveFields()) {
                    final String className = f.getJavaClassName();
                    final String type;
                    if (className.endsWith("Id")) {
                        type = "LONG";
                    } else if (className.endsWith("Score")) {
                        type = "INT";
                    } else {
                        type = "STRING";
                    }
                    System.out.println(type + "_SET.add(\"" + className + "\"); // Nullable: " + f.nullable());
                }
            }

        }

        System.out.println(builder.getElemModelMap());
        ApplicationBuilder.buildElementParserAndLayerModels(getOrg(),
                builder.getElemModelMap(),
                builder.getGenedParsers(),
                getModelAugmenterI(),
                getModelTransformerI(),
                getMergedModelMethods(),
                getMergedDLMethods(),
                builder.getAntEntries().iterator().next(),
                exportDir);

        ApplicationBuilder.convertToSqlServer(
                getExportDir()
                        + "/application/sql/schema.sql",
                getExportDir()
                        + "/application/sql/schema.sql");

        if (!getAutoGenTypeSet()) {
            FullSuite.runDiff(baselineDir, exportDir);
        }
    }

    @Override
    public String getExportDir() {
        return "/tmp/project_test_out";
    }

    @Override
    public boolean getAutoGenTypeSet() {
        return false;
    }

    @Override
    public boolean allowMissingAttributes() {
        return true;
    }

    protected Set<Constraint> getConstraints() {
        return Collections.singleton(Constraint.NOT_NULL);
    }

}
