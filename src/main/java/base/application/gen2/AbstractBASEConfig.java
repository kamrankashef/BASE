package base.application.gen2;


import base.gen.DLGen;
import base.gen.ModelGen;
import base.parsergen.rules.ModelAugmenterI;
import base.parsergen.rules.ModelTransformerI;
import base.parsergen.rules.TypeRenamerI;
import base.parsergen.rules.TypeSetsI;

import java.util.Set;

/**
 * Entry point for users to define how they want their applications built.
 * TODO There is overlap between this and another class takes items like theses into it's constructor.  This is better.
 * TODO The design should converge to something like this fed into the application builder.
 */
abstract public class AbstractBASEConfig {

    /**
     * The generated code package name, e.g. com.companyname
     * @return
     */
    public abstract String getOrg();

    public abstract String getYAMLSource();

    public abstract ModelAugmenterI getModelAugmenterI();

    public abstract TypeSetsI getTypeSetsI();

    /**
     * ModelTransformer that takes the inferred models and creates the derived models
     * @return
     */
    public abstract ModelTransformerI getModelTransformerI();

    /**
     * Path to export the generated application to
     * @return
     */
    public abstract String getExportDir();

    public abstract boolean getAutoGenTypeSet();

    public abstract TypeRenamerI getTypeRenamerI();

    // ********** Methods Generated ********** //
    public abstract Set<ModelGen.ModelMethodGenerator> getElemModelMethods();

    public abstract Set<ModelGen.ModelMethodGenerator> getMergedModelMethods();

    public abstract Set<DLGen.DLMethodGenerator> getMergedDLMethods();

    // TODO Is this actually still needed?
    public abstract boolean allowMissingAttributes();

}
