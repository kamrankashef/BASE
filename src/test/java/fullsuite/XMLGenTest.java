package fullsuite;

import base.dl.methodgenerators.InsertObject;
import base.dl.methodgenerators.InsertRaw;
import base.model.methodgenerators.AttributeBasedFromElemMethodGenerator;
import base.model.methodgenerators.ConstructorGenerator;
import base.model.methodgenerators.DerivedModelConstructorGenerator;
import base.parsergen.rules.SourceFiles;
import base.v3.AbstractApplicationBuilder;
import base.workflow.Helpers;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;


public abstract class XMLGenTest {


    protected abstract String getBaselineDir();

    protected abstract String getYAMLSource();

    protected abstract void applyOverrides(AbstractApplicationBuilder abstractApplicationBuilder);

    protected String getExportDir() {
        return "/tmp/project_test_out";
    }

    protected String getOrg() {
        return "no.org.set";
    }

    protected SourceFiles getSourceFiles() throws IOException {
        final String yamlAsString = Helpers.resourceAsString(getClass(), getYAMLSource());
        final Map<String, Object> config = (Map<String, Object>) new Yaml().load(yamlAsString);
        final String rootDir = (String) config.get("rootDir");
        final SourceFiles sourceFiles = new SourceFiles(rootDir);

        for (final Map<String, String> sourceFileAsMap : (Collection<Map<String, String>>) config.get("sourceFiles")) {

            final String fileName = sourceFileAsMap.get("fileName");
            final String type = sourceFileAsMap.get("type");

            sourceFiles.addSourceFile(type, () -> {
                final String resourcePath = rootDir + "/" + fileName;
                return XMLGenTest.class.getClassLoader().getResourceAsStream(resourcePath);
            });
        }

        return sourceFiles;
    }


    @Test
    final public void runTest() throws FileNotFoundException, InterruptedException, IOException {

        final AbstractApplicationBuilder abstractApplicationBuilder
                = new AbstractApplicationBuilder(getOrg(), getSourceFiles(), getExportDir())
                .addElemModelMethods(new AttributeBasedFromElemMethodGenerator())
                .addElemModelMethods(new ConstructorGenerator())
                .addMergedModelMethod(new ConstructorGenerator())
                .addMergedModelMethod(new DerivedModelConstructorGenerator())
                .addMergedDLModelMethod(new InsertRaw())
                .addMergedDLModelMethod(new InsertObject());


        applyOverrides(abstractApplicationBuilder);

        final String baselineDir = getBaselineDir();

        // if (!getAutoGenTypeSet()) {
        // Previously had switch in for discovery mode
        // should expose this via something like abstractApplicationBuilder.analyze()
        FullSuite.runDiff(getBaselineDir(), getExportDir());
        //}
    }

}

