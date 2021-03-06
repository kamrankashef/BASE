package base.util;


import base.model.AbstractModel;
import base.model.Model;
import base.model.PrimitiveField;

import java.util.Map;

public class AdjoinModelUtil {

    final public String org;

    // TODO Can this be removed?  Definitely seems like it
    final private boolean keepInheritConstraints;

    public AdjoinModelUtil(
            final String org) {
        this(org, true);
    }

    public AdjoinModelUtil(
            final String org,
            final boolean inheritConstraint) {
        this.org = org;
        this.keepInheritConstraints = inheritConstraint;
    }

    public AbstractModel adjoinFields(final String tranlastedBaseName,
            final Map<String, AbstractModel> models,
            final PrimitiveField... fields) {
        return adjoinFields(tranlastedBaseName, tranlastedBaseName, models, fields);
    }

    public AbstractModel adjoinFields(final String tranlastedBaseName,
            final String newName,
            final Map<String, AbstractModel> models,
            final PrimitiveField... fields) {
        return adjoinFields(tranlastedBaseName, newName, models, new PrimitiveField[]{}, fields);
    }

    public AbstractModel adjoinFields(
            final String newName,
            final PrimitiveField... fields) {
        final AbstractModel model = new Model(newName, org);
        for (final PrimitiveField field : fields) {
            model.addPrimitiveField(field);
        }

        return model;
    }

    public AbstractModel adjoinFields(final String tranlastedBaseName,
            final Map<String, AbstractModel> models,
            final PrimitiveField[] fieldsArr,
            final PrimitiveField... fields) {
        return adjoinFields(tranlastedBaseName, tranlastedBaseName, models, fields, fieldsArr);
    }

    public AbstractModel adjoinModels(final String[] modelNames,
            final String[] prefixes,
            final String newName,
            final Map<String, AbstractModel> models,
            final PrimitiveField... fields) {
        return adjoinModels(modelNames, prefixes, newName, models, new PrimitiveField[]{}, fields);
    }

    // Base adjoin 1
    // TODO Refactor to consolidate logic
    private AbstractModel adjoinModels(final String[] modelNames,
            final String[] prefixes,
            final String newName,
            final Map<String, AbstractModel> models,
            final PrimitiveField[] fieldsArr,
            final PrimitiveField... fields) {
        final AbstractModel[] modelsArr = new Model[modelNames.length];
        for (int i = 0; i < modelNames.length; i++) {
            final AbstractModel model = models.get(modelNames[i]);
            if(model == null) {
                throw new IllegalArgumentException("Model at index " + i + " with name " + modelNames[i] + " is null");
            }
            modelsArr[i] = model;
        }

        return new Model(modelsArr, prefixes, keepInheritConstraints, org, newName, fieldsArr, fields);
    }

    // Base adjoin 2
    public AbstractModel adjoinModels(final String modelName1,
            final String prefix1,
            final String modelName2,
            final String prefix2,
            final String newName,
            final Map<String, AbstractModel> models,
            final PrimitiveField[] fieldsArr,
            final PrimitiveField... fields) {

        return new Model(
                new AbstractModel[]{models.get(modelName1), models.get(modelName2)},
                new String[]{prefix1, prefix2},
                org,
                newName, fieldsArr, fields);
    }

    // Base adjoin 3
    private AbstractModel adjoinFields(final String tranlastedBaseName,
            final String newName,
            final Map<String, AbstractModel> models,
            final PrimitiveField[] fieldsArr,
            final PrimitiveField... fields
    ) {
        if (!models.containsKey(tranlastedBaseName)) {
            throw new RuntimeException("Could not find entry " + tranlastedBaseName + ".\nKnown keys are " + models.keySet());
        }

        return new Model(new AbstractModel[]{models.get(tranlastedBaseName)}, new String[]{""}, keepInheritConstraints, org, newName, fieldsArr, fields);
    }

}
