package fullsuite;

import com.base.application.ApplicationBuilder;
import com.base.dl.methodgenerators.InsertObject;
import com.base.dl.methodgenerators.InsertRaw;
import com.base.gen.DLGen;
import com.base.gen.ModelGen;
import com.base.parsergen.rules.ModelAugmenterI;
import com.base.parsergen.rules.ModelTransformerI;
import com.base.parsergen.rules.SourceFiles;
import com.base.parsergen.rules.TypeRenamerI;
import com.base.parsergen.rules.TypeSetsI;
import com.base.parsergen.XMLBuilder;
import com.base.model.AbstractModel;
import com.base.model.Constraint;
import com.base.model.PrimitiveField;
import com.base.model.methodgenerators.AttributeBasedFromElemMethodGenerator;
import com.base.model.methodgenerators.ConstructorGenerator;
import com.base.model.methodgenerators.DerivedModelConstructorGenerator;
import com.base.parsergen.AbstractBuilderFromSource;
import com.base.parsergen.rules.ParseRuleSet;
import com.base.parsergen.rules.training.SourceFilesI;
import com.base.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import com.base.workflow.Helpers;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

public abstract class XMLGenTest {

    public abstract String getOrg();

    public abstract String getYAMLSource();

    public abstract String getBaselineDir();

    public abstract ModelAugmenterI getModelAugmenterI();

    public abstract TypeSetsI getTypeSetsI();

    public abstract ModelTransformerI getModelTransformerI();

    protected SourceFilesI getSourceFiles() throws FileNotFoundException {
        throw new RuntimeException("This needs to get converted");
        // return Helpers.classToTestDir(this.getClass(), getYAMLSource());
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

    public String getExportDir() {
        return "/tmp/project_test_out";
    }

    public boolean getAutoGenTypeSet() {
        return false;
    }

    public boolean allowMissingAttributes() {
        return true;
    }

    protected Set<Constraint> getConstraints() {
        return Collections.singleton(Constraint.NOT_NULL);
    }

}
