package com.base.parsergen.rules;

import com.base.gen.ModelGen;
import com.base.model.Constraint;
import com.base.parsergen.rules.training.SourceFilesI;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class ParseRuleSet {

    public final String org;

    public final ModelAugmenterI modelAugmenter;
    public final TypeRenamerI typeRenamer;
    public final TypeSetsI typeSets;

    public final Set<ModelGen.ModelMethodGenerator> elemModelMethods;
    public final SourceFilesI sourceFiles;
    public final boolean allowMissingAttributes;
    public final Set<Constraint> defaultConstraints;

    public ParseRuleSet(
            final String org,
            final ModelAugmenterI modelAugmenter,
            final Set<ModelGen.ModelMethodGenerator> elemModelMethods,
            final TypeSetsI typeSets,
            final String exportDir,
            final SourceFilesI sourceFiles) {
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
            final SourceFilesI sourceFiles) throws IOException {
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
            final SourceFilesI sourceFiles,
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
