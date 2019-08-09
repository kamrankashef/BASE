package com.base.application.gen2;

import com.base.model.AbstractModel;
import com.base.model.PrimitiveField;
import com.base.parsergen.rules.SourceFiles;
import com.google.gson.annotations.SerializedName;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Hard-coded with Manual, Elem and composite models.
 */
public class BuildConfig implements Comparable<BuildConfig> {

    public static final String ELEM_MODELS_KEYS = "ELEM_MODELS";
    public static final String MANUAL_MODELS_KEY = "MANUAL_MODELS";
    public static final String COMPOSITE_MODELS_KEY = "COMPOSITE_MODELS";

    @SerializedName("source_file")
    final public SourceFiles.SourceFile sourceFile;
    @SerializedName("package")
    final public String _package;
    @SerializedName("manual_fields")
    final public List<PrimitiveField> manualFields;
    @SerializedName("model_groups")
    final public List<ModelsGroup> modelGroups;

    // Used to coordinate references to undelying fields and models
//    transient public final Map<String, PrimitiveField> addibleFields = new HashMap<>();
//    transient public final Map<String, AbstractModel> addibleModels = new HashMap<>();
    transient public final DependencyManager<PrimitiveField, AbstractModel> fieldsDependencyManager
            = new DependencyManager<>();
    transient public final DependencyManager<AbstractModel, AbstractModel> modelDependencyManager
            = new DependencyManager<>();

    public void addManualField(final PrimitiveField field) {
        for(final PrimitiveField existingField:manualFields) {
            if(existingField.uuid().equals(field.uuid())) {
                return;
            }
        }
        manualFields.add(field);
        fieldsDependencyManager.addToPool(field);
    }
    
    /**
     * 1 - addToPool for fields
     *
     * 2 - setExternalFields for all models
     *
     * 3 - setExternalModels for composites
     *
     * 4 - addToPool for models (only non-composite models)
     *
     * 5 - set dependency for fields
     *
     * 6 - set dependency for models
     */
    public void initDependencies() {
//        this.fieldsDependencyManager.clear();
//        this.modelDependencyManager.clear();

        // 1 - addToPool for fields
        for (final PrimitiveField manualField : manualFields) {
            fieldsDependencyManager.addToPool(manualField);
        }

        for (final ModelsGroup modelsGroup : modelGroups) {

            for (final AbstractModel model : modelsGroup.models) {

                // 2 - setExternalFields for all models
                model.setExternalFields(fieldsDependencyManager.getAddablePool());
                model.getPrimitiveFieldsWithLinked();
                if (COMPOSITE_MODELS_KEY.equals(modelsGroup.name)) {
                    // Do not add to pool
                    // 3 - setExternalModels for composites
                    model.setExternalModels(modelDependencyManager.getAddablePool());
                } else {
                    // 4 - addToPool for models (only non-composite models)
                    modelDependencyManager.addToPool(model);
                    // Do not allow composition for non-composites
                    model.setExternalModels(new HashMap<>());
                }
            }
        }

        for (final ModelsGroup modelsGroup : modelGroups) {

            for (final AbstractModel model : modelsGroup.models) {
                for (final PrimitiveField linkedField : model.getLinkedFields()) {
                    System.out.println("Adding " + linkedField.uuid() + " reffed by " + model.uuid());
                    fieldsDependencyManager.addDepedency(linkedField, model);
                }
                for (final AbstractModel linkedModel : model.getLinkedModels()) {
                    modelDependencyManager.addDepedency(linkedModel, model);
                }

            }
        }
    }

    
    public BuildConfig() {
        manualFields = new LinkedList<>();
        modelGroups = new LinkedList<>();
        _package = "empty.build.config";
        sourceFile = null;
    }

    public BuildConfig(
            final SourceFiles.SourceFile sourceFile,
            final String _package) {
        this.sourceFile = sourceFile;
        this._package = _package;
        manualFields = new LinkedList<>();
        modelGroups = new LinkedList<>();
    }

    public void setReplaceModels(final String groupName,
            final Collection<AbstractModel> models) {
        boolean found = false;
        for (final ModelsGroup group : modelGroups) {
            if (group.name.equals(groupName)) {
                found = true;
                group.models.clear();
                group.models.addAll(models);
            }
        }
        if (!found) {
            modelGroups.add(new ModelsGroup(groupName, models, Collections.EMPTY_SET, Collections.EMPTY_SET));
        }
    }

    public void setModelsGroups(final Collection<ModelsGroup> modelGroups) {
        this.modelGroups.clear();
        this.modelGroups.addAll(modelGroups);
        initDependencies();
    }

    public void setManualFields(Collection<PrimitiveField> manualFields) {
        this.manualFields.clear();
        this.manualFields.addAll(manualFields);
        initDependencies();
    }

    @Override
    public int compareTo(final BuildConfig o) {
        if (o == null) {
            return -1;
        }
        final int fileCompare = this._package.compareTo(o._package);
        if (fileCompare != 0) {
            return fileCompare;
        }
        return this.sourceFile.fileName.compareTo(o.sourceFile.fileName);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.sourceFile.fileName);
        hash = 89 * hash + Objects.hashCode(this._package);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BuildConfig other = (BuildConfig) obj;
        if (!Objects.equals(this._package, other._package)) {
            return false;
        }
        if (!Objects.equals(this.sourceFile.fileName, other.sourceFile.fileName)) {
            return false;
        }
        return true;
    }

}
