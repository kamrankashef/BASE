package base.v3;


import base.application.ApplicationBuilder;
import base.gen.DLGen;
import base.gen.ModelGen;
import base.model.AbstractModel;
import base.model.Constraint;
import base.model.PrimitiveField;
import base.parsergen.AbstractBuilderFromSource;
import base.parsergen.XMLBuilder;
import base.parsergen.rules.*;
import base.parsergen.rules.impl.StatefulTypeSetGuesser;
import base.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


// Follow the model of XMLGenTest
public abstract  class AbstractApplicationBuilder {

    protected String getExportDir() {
        return "/tmp/base_application";
    }

    /**
     * The generated code package name, e.g. com.companyname
     * @return
     */
    protected String getOrg() {
        return "com.example";
    }


    protected ModelAugmenterI getModelAugmenterI() {
        return ModelAugmenterI.EMPTY_AUGMENTER;
    }

    protected TypeSetsI getTypeSetsI() {
        return new StatefulTypeSetGuesser();
    }


    protected ModelTransformerI getModelTransformerI() {
        return ModelTransformerI.EMPTY_TRANSFORMER;
    }

    protected Set<Constraint> getConstraints() {
        return Collections.singleton(Constraint.NOT_NULL);
    }

    // TODO Migrate away from tight coupling of yaml config with application building.  Should be stream driven.
    public abstract String getYAMLSource();
    /**
     * Path to export the generated application to
     * @return
     */

    // TODO This should be replaced with "discovery" vs "generative" mode
    protected boolean getAutoGenTypeSet() {
        return false;
    }

    protected TypeRenamerI getTypeRenamerI() {
        return TypeRenamerI.DEFAULT_RENAMER;
    }

    // ********** Methods Generated ********** //

    // Elem Model methods
    // TODO ModelMethodGenerator needs an equals driven by name
    final private Set<ModelGen.ModelMethodGenerator> elemModelMethods = new HashSet<>();

    // See about reducing scope to private
    public Set<ModelGen.ModelMethodGenerator> getElemModelMethods() {
        return elemModelMethods;
    }

    public AbstractApplicationBuilder addElemModelMethod(ModelGen.ModelMethodGenerator modelMethodGenerator) {
        elemModelMethods.add(modelMethodGenerator);
        return this;
    }

    // Derived Model methods
    final private Set<ModelGen.ModelMethodGenerator> mergedModelMethods = new HashSet<>();

    public Set<ModelGen.ModelMethodGenerator> getMergedModelMethods() {
        return mergedModelMethods;
    }

    public AbstractApplicationBuilder addMergedModelMethod(ModelGen.ModelMethodGenerator modelMethodGenerator) {
        mergedModelMethods.add(modelMethodGenerator);
        return this;
    }

    // DL Methods
    final private Set<DLGen.DLMethodGenerator> dlMethods = new HashSet<>();

    public AbstractApplicationBuilder addMergedDLModelMethod(DLGen.DLMethodGenerator modelMethodGenerator) {
        dlMethods.add(modelMethodGenerator);
        return this;
    }

    public Set<DLGen.DLMethodGenerator> getMergedDLMethods() {
        return dlMethods;
    }

    // TODO Is this actually still needed?
    protected boolean allowMissingAttributes() {
        return true;
    }

    protected abstract SourceFiles getSourceFiles();

    // TODO This is convoluted - focus on this in next refactor
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

    final public void generateApplication() throws IOException, InterruptedException {

        final String exportDir = getExportDir();
        System.out.println("Deleting export dir: '" + exportDir + "'");
        FileUtil.deleteDir(new File(exportDir));

        final AbstractBuilderFromSource builder
                = getBuilder();

        // This was part of semi-automated data schema discovery
        // Perhaps create a logical break here
        // e.g. call this.postBuildHook()
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
        // This does the actual export
        ApplicationBuilder.buildElementParserAndLayerModels(getOrg(),
                builder.getElemModelMap(),
                builder.getGenedParsers(),
                getModelAugmenterI(),
                getModelTransformerI(),
                getMergedModelMethods(),
                getMergedDLMethods(),
                builder.getAntEntries().iterator().next(),
                exportDir);

        // This needs to be driven by target SQL schema
        ApplicationBuilder.convertToSqlServer(
                getExportDir()
                        + "/application/sql/schema.sql",
                getExportDir()
                        + "/application/sql/schema.sql");

    }

}
