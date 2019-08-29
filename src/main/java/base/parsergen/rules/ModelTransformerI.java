package base.parsergen.rules;

import base.gen.DLGen;
import base.gen.ModelGen;
import base.model.AbstractModel;
import base.model.PrimitiveField;
import base.util.AdjoinModelUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@FunctionalInterface
public interface ModelTransformerI {

    public static final ModelTransformerI EMPTY_TRANSFORMER
            = (final String pkg,
                    final Map<String, AbstractModel> models,
                    final ModelAugmenterI modelAugmenter,
                    final Set<ModelGen.ModelMethodGenerator> mergedModelMethods,
                    final Set<DLGen.DLMethodGenerator> mergedDLMethods)
            -> Collections.EMPTY_LIST;

    public static ModelTransformerI getSimplePassThroughElemTransformer(
            final Collection<String> modelNames,
            final PrimitiveField... fields) {
        return (final String pkg,
                final Map<String, AbstractModel> models,
                final ModelAugmenterI modelAugmenter,
                final Set<ModelGen.ModelMethodGenerator> mergedModelMethods,
                final Set<DLGen.DLMethodGenerator> mergedDLMethods) -> {
            final AdjoinModelUtil adjoinModelUtil = new AdjoinModelUtil(pkg, true);

            final List<AbstractModel> derivedModels = new LinkedList<>();

            for (final String modelName : modelNames) {
                derivedModels.add(adjoinModelUtil.adjoinFields(
                        modelName,
                        modelName.replaceAll("Elem$", ""),
                        models, 
                        fields));
            }

            return derivedModels;
        };
    }

    public List<AbstractModel> getDerivedModels(
            final String pkg,
            final Map<String, AbstractModel> models,
            final ModelAugmenterI modelAugmenter,
            final Set<ModelGen.ModelMethodGenerator> mergedModelMethods,
            final Set<DLGen.DLMethodGenerator> mergedDLMethods);
}
