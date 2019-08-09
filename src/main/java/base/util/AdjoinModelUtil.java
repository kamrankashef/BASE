package base.util;

import base.gen.DLGen;
import base.gen.ModelGen;
import base.parsergen.rules.ModelAugmenterI;
import base.model.AbstractModel;
import base.model.Model;
import base.model.PrimitiveField;
import java.util.Map;
import java.util.Set;

public class AdjoinModelUtil {

    final public String org;

    // TODO Can this be removed?  Definitely seems like it
    final private ModelAugmenterI modelAugmenter;
    final Set<ModelGen.ModelMethodGenerator> mergedModelMethods;
    final Set<DLGen.DLMethodGenerator> mergedDLMethods;
    final boolean keepInheritConstraints;

    public AdjoinModelUtil(
            final String org,
            final ModelAugmenterI modelAugmenter,
            final Set<ModelGen.ModelMethodGenerator> mergedModelMethods,
            final Set<DLGen.DLMethodGenerator> mergedDLMethods) {
        this(org, modelAugmenter, mergedModelMethods, mergedDLMethods, true);
    }

    public AdjoinModelUtil(
            final String org,
            final ModelAugmenterI modelAugmenter,
            final Set<ModelGen.ModelMethodGenerator> mergedModelMethods,
            final Set<DLGen.DLMethodGenerator> mergedDLMethods,
            final boolean inheritConstraint) {
        this.org = org;
        this.modelAugmenter = modelAugmenter;
        this.mergedModelMethods = mergedModelMethods;
        this.mergedDLMethods = mergedDLMethods;
        this.keepInheritConstraints = inheritConstraint;
    }

    // TODO - Seems like the model and laying generators should
    // be independent operations.
    public void addDLAndModelMethods(final AbstractModel model) {

        for (final ModelGen.ModelMethodGenerator mmg : mergedModelMethods) {
            model.addModelMethodGenerator(mmg);
        }

        for (final DLGen.DLMethodGenerator dmg : mergedDLMethods) {
            model.addDLMethodGenerator(dmg);
        }

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

        addDLAndModelMethods(model);

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
    public AbstractModel adjoinModels(final String[] modelNames,
            final String[] prefixes,
            final String newName,
            final Map<String, AbstractModel> models,
            final PrimitiveField[] fieldsArr,
            final PrimitiveField... fields) {
        final AbstractModel[] modelsArr = new Model[modelNames.length];
        for (int i = 0; i < modelNames.length; i++) {
            final AbstractModel model = models.get(modelNames[i]);
            if(model == null) {
                System.out.println("Model at index " + i + " with name " + modelNames[i] + " is null");
            }
            modelsArr[i] = model;
        }

        final AbstractModel model = new Model(modelsArr, prefixes, keepInheritConstraints, org, newName, fieldsArr, fields);

        addDLAndModelMethods(model);

        return model;

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

        final Model model = new Model(
                new AbstractModel[]{models.get(modelName1), models.get(modelName2)},
                new String[]{prefix1, prefix2},
                org,
                newName, fieldsArr, fields);

        addDLAndModelMethods(model);

        return model;
    }

    // Base adjoin 3
    public AbstractModel adjoinFields(final String tranlastedBaseName,
            final String newName,
            final Map<String, AbstractModel> models,
            final PrimitiveField[] fieldsArr,
            final PrimitiveField... fields
    ) {
        if (!models.containsKey(tranlastedBaseName)) {
            throw new RuntimeException("Could not find entry " + tranlastedBaseName + ".\nKnown keys are " + models.keySet());
        }

        final AbstractModel model = new Model(new AbstractModel[]{models.get(tranlastedBaseName)}, new String[]{""}, keepInheritConstraints, org, newName, fieldsArr, fields);
        addDLAndModelMethods(model);
        return model;
    }

}
