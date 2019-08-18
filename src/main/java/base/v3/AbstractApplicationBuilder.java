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
public class AbstractApplicationBuilder {

    final SourceFiles sourceFiles;

    private String exportDir = "/tmp/base_application";
    private String org = "com.example";
    private ModelAugmenterI modelAugmenter = ModelAugmenterI.EMPTY_AUGMENTER;
    private TypeSetsI typeSets = new StatefulTypeSetGuesser();
    private ModelTransformerI modelTransformer = ModelTransformerI.EMPTY_TRANSFORMER;
    private Set<Constraint> constraints = Collections.singleton(Constraint.NOT_NULL);
    private boolean autoGenTypeSetEnabled = false;
    private TypeRenamerI typeRenamer = TypeRenamerI.DEFAULT_RENAMER;

    // TODO ModelMethodGenerator needs an equals driven by name
    final private Set<ModelGen.ModelMethodGenerator> elemModelMethods = new HashSet<>();
    final private Set<ModelGen.ModelMethodGenerator> mergedModelMethods = new HashSet<>();
    final private Set<DLGen.DLMethodGenerator> dlMethods = new HashSet<>();

    // TODO Is this actually still needed?
    private boolean allowMissing = true;

    public AbstractApplicationBuilder(final String org, final SourceFiles sourceFiles, final String exportDir) {
        this.org = org;
        this.sourceFiles = sourceFiles;
        this.exportDir = exportDir;
    }


    public AbstractApplicationBuilder setModelAugmenterI(final ModelAugmenterI modelAugmenter) {
        this.modelAugmenter = modelAugmenter;
        return this;
    }

    public AbstractApplicationBuilder setTypeSetsI(final TypeSetsI typeSets) {
        this.typeSets = typeSets;
        return this;
    }

    public AbstractApplicationBuilder setModelTransformerI(final ModelTransformerI modelTransformer) {
        this.modelTransformer = modelTransformer;
        return this;
    }


    public AbstractApplicationBuilder disableConstraints() {
        constraints.clear();
        return this;
    }

    // TODO Migrate away from tight coupling of yaml config with application building.  Should be stream driven.
    // TODO Streams of Source data should be provided in the constructor
    // public abstract String getYAMLSource();

    /**
     * Path to export the generated application to
     *
     * @return
     */

    // TODO This should be replaced with "discovery" vs "generative" mode
    public AbstractApplicationBuilder enableAutoGenTypeSet() {
        this.autoGenTypeSetEnabled = true;
        return this;
    }


    public AbstractApplicationBuilder setTypeRenamerI(final TypeRenamerI typeRenamer) {
        this.typeRenamer = typeRenamer;
        return this;
    }

    // ********** Methods Generated ********** //


    public AbstractApplicationBuilder addElemModelMethods(final ModelGen.ModelMethodGenerator modelMethodGenerator) {
        elemModelMethods.add(modelMethodGenerator);
        return this;
    }


    public AbstractApplicationBuilder addMergedModelMethod(ModelGen.ModelMethodGenerator modelMethodGenerator) {
        mergedModelMethods.add(modelMethodGenerator);
        return this;
    }


    public AbstractApplicationBuilder addMergedDLModelMethod(DLGen.DLMethodGenerator modelMethodGenerator) {
        dlMethods.add(modelMethodGenerator);
        return this;
    }

    public AbstractApplicationBuilder setAllowMissingAttributes(final boolean allowMissing) {
        this.allowMissing = allowMissing;
        return this;
    }


    // TODO This is convoluted - focus on this in next refactor
    // For the immediate teram extend this to a CSV version
    protected AbstractBuilderFromSource getBuilder() throws IOException {
        // TODO Break out Builder - E.g. CSV vs XML Builder.
        return AbstractBuilderFromSource.run(new XMLBuilder(
                new ParseRuleSet(this.org,
                        this.modelAugmenter,
                        this.elemModelMethods,
                        this.typeSets,
                        this.typeRenamer,
                        this.sourceFiles,
                        this.allowMissing,
                        this.constraints)
        ));
    }

    final public void build() throws IOException, InterruptedException {

        final String exportDir = this.exportDir;
        System.out.println("Deleting export dir: '" + exportDir + "'");
        FileUtil.deleteDir(new File(exportDir));

        final AbstractBuilderFromSource builder
                = getBuilder();

        // This was part of semi-automated data schema discovery
        // Perhaps create a logical break here
        // e.g. call this.postBuildHook()
        if (this.autoGenTypeSetEnabled) {
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
        ApplicationBuilder.buildElementParserAndLayerModels(this.org,
                builder.getElemModelMap(),
                builder.getGenedParsers(),
                this.modelAugmenter,
                this.modelTransformer,
                this.mergedModelMethods,
                this.dlMethods,
                builder.getAntEntries().iterator().next(),
                exportDir);

        // This needs to be driven by target SQL schema
        ApplicationBuilder.convertToSqlServer(
                this.exportDir
                        + "/application/sql/schema.sql",
                this.exportDir
                        + "/application/sql/schema.sql");

    }

}
