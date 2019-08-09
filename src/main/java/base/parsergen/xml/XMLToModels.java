package base.parsergen.xml;

import base.model.AbstractModel;
import base.model.FieldUtil;
import base.model.Model;
import base.model.PrimitiveField;
import base.model.PrimitiveType;
import base.normal.impl.TypeChecker;
import base.parsergen.rules.ParseRuleSet;
import base.util.CaseConversion;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// 1.  Define each model based on tag name - done
// 2.  Fill all non FK properties (assume all to be of type String) - done
// 3.  Run exporter to auto gen Java classes, schema and DAL - done
// 4.  Generate Jsoup XML parser Class to nest query of each type an init each
//     object of given type - done
// 5.  Create buildable project export - done
// 6.  Run Generated XML parser over sample file
// 7.  Export example (create a toXML generated method)
// 8.  Verify that the XML is equivalent
/**
 *
 * Parse XML document, building a XSDNesting structure which is a tree of Models
 * to represent the XML hierarchy
 *
 * XML Document to a map of models.
 */
public class XMLToModels {

    public List<String> pathBreadCrumbs = new LinkedList<>();
    public final Map<String, String> modelNameToPath = new HashMap<>();
    final private Set<String> xmlns = new HashSet<>();
    public final Map<String, AbstractModel> models = new HashMap<>();
    // If an attribute is not in all elements at a given level, allow it as a null
    // -> Merge it in (to do -> Migrate node merge functionality?
    private final ParseRuleSet parseRuleSet;

    public XMLToModels(final ParseRuleSet parseRuleSet) {
        this.parseRuleSet = parseRuleSet;
    }


    /*
             * Load a XSDNesting with the model hierarchy inferred from elem
             *
             * @param rootHierarchy Object to hold the model hierarchy
             * @param elem XML node
     */
    public void createTypeDefs(final ModelHierarchy rootHierarchy, final Element elem) {

        for (final Element e : elem.children()) {

            // Do not process text tags as objects
            if (XMLTagUtil.isATextTag(e, xmlns)) {
                continue;
            }

            final String tagName = e.tagName();

            pathBreadCrumbs.add(tagName);

            final String rootClassName = CaseConversion.toJavaClassName(tagName);

            final String modelName;

            // Check if empty
            if (e.attributes().size() == 0) {
                modelName = rootClassName + "Empty";
            } else {
                modelName = rootClassName;
            }

            final AbstractModel model;

            {
                // First queue up the attributes
                final Set<PrimitiveField> attributeFields = attributesToFields(modelName, e.attributes());

                // textTagsToFields
                final Set<PrimitiveField> textFields = textTagsToFields(modelName, e.children());

                final int expectedSize = textFields.size() + attributeFields.size();

                attributeFields.addAll(textFields);

                if (attributeFields.size() != expectedSize) {
                    throw new RuntimeException("Sizes did not match");
                }

                // Make final
                model = genModel(rootHierarchy, modelName, attributeFields);
            }

            // Assuming a single master nodes
            if (!rootHierarchy.subNestings.containsKey(model) /* && at a different path level ... */) {
                // Why not allow replacement?  Becauese being used elsewhere?
                rootHierarchy.subNestings.put(model, new ModelHierarchy(model, tagName));
            }
            final ModelHierarchy currNesting = rootHierarchy.subNestings.get(model);

            createTypeDefs(currNesting, e);
            pathBreadCrumbs.remove(pathBreadCrumbs.size() - 1);

        }
    }

    private Set<PrimitiveField> attributesToFields(final String modelName, final Attributes attributes) {

        final Set<PrimitiveField> fields = new HashSet<>();

        for (final Attribute a : attributes) {
            final String attributeName = a.getKey();
            final String asClassName;
            final String attributeValue = a.getValue();
            if (attributeName.contains(":")) {
                final String prefix = attributeName.split(":")[0];
                final String suffix = attributeName.split(":")[1];
                if (prefix.equals("xmlns")) {
                    xmlns.add(suffix);
                    continue;
                }
                if (xmlns.contains(prefix)) {
                    continue;
                }
                asClassName = CaseConversion.toJavaClassName(parseRuleSet.typeRenamer.rename(suffix));
            } else {
                asClassName = CaseConversion.toJavaClassName(parseRuleSet.typeRenamer.rename(attributeName));
            }

                final PrimitiveType type = parseRuleSet.typeSets.nameToType(modelName, asClassName, attributeValue);
            final PrimitiveField field = new PrimitiveField(asClassName, attributeName, type);
            field.setNullable(parseRuleSet.typeSets.lastAllowedNull());
            fields.add(field);
        }
        return fields;
    }

    private Set<PrimitiveField> textTagsToFields(final String modelName,
            final Elements elements) {
        final Set<PrimitiveField> fields = new HashSet<>();

        for (final Element elem : elements) {

            if (!XMLTagUtil.isATextTag(elem, xmlns) || elem.text().isEmpty()) {
                continue;
            }
            final String tagName = elem.tagName();
            final String asVarName = CaseConversion.toJavaClassName(parseRuleSet.typeRenamer.rename(tagName));
            final PrimitiveType type = parseRuleSet.typeSets.nameToType(modelName, asVarName, elem.text());
            final PrimitiveField field = new PrimitiveField(parseRuleSet.typeRenamer.rename(tagName), tagName, Collections.EMPTY_SET, type);
            field.setNullable(true);
            fields.add(field);
        }
        return fields;
    }

    private String currentPath() {
        return String.join(" > ", pathBreadCrumbs);
    }

    private AbstractModel pushNewModel(final String modelName, final Set<PrimitiveField> fields) {
        final Model model = new Model(
                modelName,
                Collections.EMPTY_LIST,
                Collections.EMPTY_LIST,
                parseRuleSet.org);
        models.put(modelName, model);
        modelNameToPath.put(modelName, currentPath());
        model.addPrimitiveFields(fields);
        return model;
    }

    private AbstractModel genModel(final ModelHierarchy rootXSDModel,
            final String modelName,
            final Set<PrimitiveField> fields) {

        final String currentPath = currentPath();

        if (models.containsKey(modelName)) {
            AbstractModel tempModel = models.get(modelName);

            boolean same = fields.size() == tempModel.getPrimitiveFields().size();

            for (final PrimitiveField field : tempModel.getPrimitiveFields()) {
                same &= fields.contains(field);
            }

            if (!same) {
                // 1. Dive deeper, name clash
                if (parseRuleSet.allowMissingAttributes
                        && modelNameToPath.get(modelName).equals(currentPath)) {
                    // figure out the difference set and allow them to be null
                    // Set up reference maps

                    {
                        // Nullify entries that don't already exist
                        final Map<String, PrimitiveField> newFieldsMap = FieldUtil.fieldsToMap(fields);
                        // Add fields that don't exist in existing model [set them to null}
                        for (final PrimitiveField field : tempModel.getPrimitiveFields()) {
                            if (!newFieldsMap.containsKey(field.getName())) {
                                field.setNullable(true);
                            }
                        }
                    }
                    // For each new field add it if it doesn't already exist [as null]
                    // Else merge it
                    final Map<String, PrimitiveField> tempModelFields = FieldUtil.fieldsToMap(tempModel.getPrimitiveFields());
                    for (final PrimitiveField newField : fields) {
                        final String fieldName = newField.getName();
                        if (!tempModelFields.containsKey(fieldName)) {
                            newField.setNullable(true);
                            tempModel.addPrimitiveField(newField);
                        } else {
                            // Merge
                            final PrimitiveField existingField = tempModelFields.get(fieldName);
                            TypeChecker.mergeInto(existingField, newField);
                        }
                    }
                    return tempModel;
                } else {
                    return genModel(rootXSDModel, modelName + rootXSDModel.model.getJavaClassName(), fields);// Force null pointer, else need parent data
                }
            } else {
                // 2.  The models match object descriptions match.  Return it and move on
                return tempModel;
            }
        }
        // 3.  Define new model
        return this.pushNewModel(modelName, fields);
    }

}
