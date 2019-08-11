package base.parsergen.rules;

import base.gen.ModelGen;
import base.model.Constraint;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class ParseRuleSet {

    public final String org;

    public final ModelAugmenterI modelAugmenter;
    public final TypeRenamerI typeRenamer;
    public final TypeSetsI typeSets;

    public final Set<ModelGen.ModelMethodGenerator> elemModelMethods;
    public final SourceFiles sourceFiles;
    public final boolean allowMissingAttributes;
    public final Set<Constraint> defaultConstraints;

    public ParseRuleSet(
            final String org,
            final ModelAugmenterI modelAugmenter,
            final Set<ModelGen.ModelMethodGenerator> elemModelMethods,
            final TypeSetsI typeSets,
            final String exportDir,
            final SourceFiles sourceFiles) {
        this(org, modelAugmenter, elemModelMethods,
                typeSets,
                TypeRenamerI.DEFAULT_RENAMER,
                sourceFiles,
                false,
                Collections.EMPTY_SET);
    }
    // Type sets used and disregarded

    public ParseRuleSet(
            final String org,
            final ModelAugmenterI modelAugmenter,
            final Set<ModelGen.ModelMethodGenerator> elemModelMethods,
            final TypeSetsI typeSets,
            final TypeRenamerI renamer,
            final SourceFiles sourceFiles) throws IOException {
        this(org, modelAugmenter, elemModelMethods,
                typeSets,
                renamer,
                sourceFiles,
                false,
                Collections.EMPTY_SET);
    }
    // Type sets used and disregarded

    public ParseRuleSet(
            final String org,
            final ModelAugmenterI modelAugmenter,
            final Set<ModelGen.ModelMethodGenerator> elemModelMethods,
            final TypeSetsI typeSets,
            final TypeRenamerI renamer,
            final SourceFiles sourceFiles,
            final boolean allowMissingAttributes,
            final Set<Constraint> defaultConstraints) {
        this.org = org;
        this.modelAugmenter = modelAugmenter;
        this.elemModelMethods = elemModelMethods;
        this.typeSets = typeSets;
        this.typeRenamer = renamer;
        this.sourceFiles = sourceFiles;
        this.allowMissingAttributes = allowMissingAttributes;
        this.defaultConstraints = defaultConstraints;
    }

}
