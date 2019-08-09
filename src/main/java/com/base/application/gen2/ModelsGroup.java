package com.base.application.gen2;

import com.base.gen.DLGen;
import com.base.gen.ModelGen;
import com.base.model.AbstractModel;
import com.google.gson.annotations.SerializedName;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Grouping of models and with method / datalayer configuration.
 */
public class ModelsGroup {

    @SerializedName("models")
    final public Set<AbstractModel> models;
    @SerializedName("name")
    final public String name;
    
    // These should be moved into a higher level
    @SerializedName("dl_methods_classes")
    final public Set<String> dlMethodClasses;
    @SerializedName("model_methods_classes")
    final public Set<String> modelMethodClasses;

    public ModelsGroup(
            String name,
            Collection<AbstractModel> models,
            Set<String> modelMethodClasses,
            Set<String> dlMethodClasses
    ) {
        this.models = new HashSet<>(models);
        this.name = name;
        this.dlMethodClasses = dlMethodClasses;
        this.modelMethodClasses = modelMethodClasses;
    }

    public List<ModelGen.ModelMethodGenerator> getModelGeneratorsInstances() {
        final List<ModelGen.ModelMethodGenerator> retList = new LinkedList<>();
        for (final String str : modelMethodClasses) {
            try {
                Class c = Class.forName(str);
                retList.add((ModelGen.ModelMethodGenerator) c.newInstance());
            } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return retList;
    }

    public List<DLGen.DLMethodGenerator> getDLMethodGeneratorsInstances() {
        final List<DLGen.DLMethodGenerator> retList = new LinkedList<>();
        for (final String str : dlMethodClasses) {
            try {
                Class c = Class.forName(str);
                retList.add((DLGen.DLMethodGenerator) c.newInstance());
            } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return retList;
    }
    
    

}
