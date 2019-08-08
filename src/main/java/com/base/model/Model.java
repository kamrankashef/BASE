package com.base.model;

import com.base.parsergen.rules.ModelAugmenterI;
import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

// Constituent Models with prefixes
// For assignment
// for Model m : constituentModels
//   pt = new PrimitiveField(prefix + field.getJavaClassName(), field.contraints, field.getType());
//   pt.toJavaDeclaration + " = " + m.toJavaVariableName + "." + toJavafield.toJavaVariableName  + ";" 
public class Model extends AbstractModel {

    @SerializedName("inherit_constraints")
    final boolean inheritConstraints;

    private static List<PrimitiveField> joinArray(
            final PrimitiveField[] fieldsArr,
            final PrimitiveField... fields) {
        final List< PrimitiveField> arr = new LinkedList<>();
        for (int i = 0; i < fieldsArr.length; i++) {
            arr.add(fieldsArr[i]);
        }

        for (int i = 0; i < fields.length; i++) {
            arr.add(fields[i]);
        }

        return arr;
    }

    @SerializedName("fk_fields")
    private final Set<ForiegnKeyField> fkFields = new TreeSet<>();
    @SerializedName("primitive_fields")
    private final Set<PrimitiveField> primitiveFields = new TreeSet<>();
    @SerializedName("augmented_fields")
    private final Map<PrimitiveField, String> augmentedFields = new TreeMap<>();

    // constituent models and prefixes
    @SerializedName("constituent_models")
    final private Map<String, AbstractModel> constituentModels = new TreeMap<>();
    @SerializedName("adjoined_fields")
    private List<PrimitiveField> adjoinedFields = new LinkedList<>();

    public Model(String modelName, List<ForiegnKeyField> fkFields, List<PrimitiveField> primitiveFields, String org) {
        this(modelName, fkFields, primitiveFields, false, org);
    }

    public Model(final String unstructuredName,
            final String org,
            final PrimitiveField... primitiveFields
    ) {
        this(unstructuredName, org, new PrimitiveField[0], primitiveFields);
    }

    @Override
    public Set<PrimitiveField> getSimplePrimitiveFields() {
        return new TreeSet<>(primitiveFields);
    }

    public Model(final AbstractModel[] models,
            final String[] prefixes,
            final String org,
            final String unstructuredName,
            final PrimitiveField[] fieldsArr,
            final PrimitiveField... fields) {
        this(models, prefixes, true, org, unstructuredName, fieldsArr, fields);
    }

    public Model(final AbstractModel[] models,
            final String[] prefixes,
            final boolean inheritConstraints,
            final String org,
            final String unstructuredName,
            final PrimitiveField[] fieldsArr,
            final PrimitiveField... fields) {

        this(unstructuredName, Collections.EMPTY_LIST, joinArray(fieldsArr, fields), inheritConstraints, org);
        // Slightly messy tracking of adjoined fields.
        this.adjoinedFields = joinArray(fieldsArr, fields);

        for (int i = 0; i < prefixes.length; i++) {
            if (prefixes[i] == null) {
                throw new RuntimeException("Prefix not found");
            }
            if (models[i] == null) {
                // Referenced a model not in the map
                throw new RuntimeException("Model not found for prefix '" + prefixes[i] + "' at index " + i);
            }
            constituentModels.put(prefixes[i], models[i]);
        }
    }

    @Override
    public TreeMap<String, AbstractModel> getConstituentModels() {
        return new TreeMap<>(constituentModels);
    }

    transient private Map<String, PrimitiveField> externalFields = new HashMap<>();

    @Override
    public void setExternalFields(final Map<String, PrimitiveField> externalFields) {
        this.externalFields = externalFields;
        for (final AbstractModel constituent : constituentModels.values()) {
            constituent.setExternalFields(externalFields);
        }
    }

    private Map<String, AbstractModel> externalModels = new HashMap<>();

    @Override
    public void setExternalModels(final Map<String, AbstractModel> externalModels) {
        this.externalModels = externalModels;
        for (final AbstractModel constituent : constituentModels.values()) {
            constituent.setExternalModels(externalModels);
        }
    }

    @Override
    public TreeMap<String, AbstractModel> getConstituentModelsPlusLinked() {
        final TreeMap retMap = new TreeMap<>();
        TOP_OF_LOOP:
        for (final String uuid : referencedModels) {
            for (final Map.Entry<String, AbstractModel> entry : externalModels.entrySet()) {
                final String prefix = entry.getKey();
                final AbstractModel externalModel = entry.getValue();
                if (externalModel.uuid().equals(uuid)) {
                    retMap.put(prefix, externalModel);
                    continue TOP_OF_LOOP;
                }
            }
            throw new IllegalStateException("Could not link to model: " + uuid);
        }
        retMap.putAll(constituentModels);

        return retMap;
    }

    public Model(
            final String unstructuredName,
            final List<ForiegnKeyField> fkFields,
            final List<PrimitiveField> primitiveFields,
            final boolean inheritConstraints,
            final String subPackage
    ) {
        super(unstructuredName, subPackage);
        this.fkFields.addAll(fkFields);
        this.primitiveFields.addAll(primitiveFields);
        this.inheritConstraints = inheritConstraints;
    }

    public Model(final String unStructuredName, final String subPackage) {
        this(unStructuredName, new LinkedList<>(), new LinkedList<>(), false, subPackage);
    }

    Model(final AbstractModel[] models,
            final String[] names,
            final String neworg,
            final String somethingNew) {
        this(models, names, neworg, somethingNew, new PrimitiveField[]{});
    }

    public Model(final String subName,
            final String org,
            final PrimitiveField[] fieldsArr,
            final PrimitiveField... fields) {
        this(new AbstractModel[]{}, new String[]{}, org, subName, fieldsArr, fields);
    }

    @Override
    public void addFKField(final ForiegnKeyField field) {
        this.fkFields.add(field);
    }

    final public List<PrimitiveField> getAdjoinedFields() {
        return new LinkedList<>(this.adjoinedFields);
    }

    @Override
    final public Set<ForiegnKeyField> getFkFields() {
        return Collections.unmodifiableSet(fkFields);
    }

    @Override
    final public void addPrimitiveField(final PrimitiveField field) {
        this.primitiveFields.add(field);
    }

    final public void addPrimitiveFields(final Set<PrimitiveField> fields) {
        this.primitiveFields.addAll(fields);
    }

    @Override
    public List<AbstractModel> getLinkedModels() {
        final List<AbstractModel> linkedModels = new LinkedList<>();
        for (final String uuid : referencedModels) {
            linkedModels.add(externalModels.get(uuid));
        }
        return linkedModels;
    }

    @Override
    public List<PrimitiveField> getLinkedFields() {
        final List<PrimitiveField> linkedModels = new LinkedList<>();
        for (final String uuid : referencedFields) {
            linkedModels.add(externalFields.get(uuid));
        }
        return linkedModels;
    }

    @Override
    public List<AbstractModel> getAvailableLinkedModels() {
        final List<AbstractModel> linkedModels = new LinkedList<>();
        for (final AbstractModel linkableModel : externalModels.values()) {
            if (!referencedModels.contains(linkableModel.uuid())) {
                linkedModels.add(linkableModel);
            }
        }
        return linkedModels;
    }

    @Override
    public List<PrimitiveField> getPrimitiveFields() {
        return new LinkedList<>(primitiveFields);
    }

    @Override
    public List<PrimitiveField> getAvailablePrimitiveFields() {
        final List<PrimitiveField> availableFields = new LinkedList<>();
        // Add the suppressed fields
        for (final String uuid : super.suppressedFields) {
            for (final PrimitiveField field : primitiveFields) {
                if (uuid.equals(field.uuid())) {
                    availableFields.add(field);
                }
            }
        }

        // Add the manual fields not included
        for (final PrimitiveField field : externalFields.values()) {
            if (!super.referencedFields.contains(field.uuid())) {
                availableFields.add(field);
            }
        }
        return availableFields;
    }

    @Override
    public List<PrimitiveField> getPrimitiveFieldsWithLinked() {
        Set<PrimitiveField> pFields = new TreeSet<>(primitiveFields);

        // Add enternal fields
        for (final String addUUID : super.referencedFields) {
            pFields.add(externalFields.get(addUUID));
        }

        // Remove suppressed
        for (final String removeUUID : super.suppressedFields) {
            PrimitiveField fieldToRemove = null;
            for (final PrimitiveField field : pFields) {
                if (field.uuid().equals(removeUUID)) {
                    fieldToRemove = field;
                    break;
                }
            }
            if (fieldToRemove != null) {
                pFields.remove(fieldToRemove);
            } else {
                throw new RuntimeException("Unknown suppressed field " + removeUUID);
            }

        }

        for (final Map.Entry<String, AbstractModel> entry : allExternalModels().entrySet()) {

            final AbstractModel m = entry.getValue();
            for (final PrimitiveField pf : m.getPrimitiveFieldsWithLinked()) {
                PrimitiveField localField = getLocalField(m, pf);
                pFields.add(localField);
            }
            for (final PrimitiveField pf : m.getAugmentedFields().keySet()) {
                PrimitiveField localField = getLocalField(m, pf);
                pFields.add(localField);
            }
        }

        return Collections.unmodifiableList(new LinkedList<>(pFields));
    }

    @Override
    final public Map<PrimitiveField, String> getAugmentedFields() {
        return Collections.unmodifiableMap(augmentedFields);
    }

    @Override
    public void applyAugmented(final ModelAugmenterI modelAugmenter) {
        this.augmentedFields.putAll(modelAugmenter.getAugmentedFields(this));
    }

    @Override
    public String toString() {
        return getJavaClassName();
    }

    public Map<String, AbstractModel> allExternalModels() {
        final Map<String, AbstractModel> allExternalModels = new HashMap<>();
        allExternalModels.putAll(constituentModels);

        for (final String modelUUID : super.referencedModels) {
            allExternalModels.put("", externalModels.get(modelUUID));
        }
        return allExternalModels;
    }

    public PrimitiveField getLocalField(AbstractModel constituent, PrimitiveField pf) {

        final String prefix
                = allExternalModels()
                        .entrySet()
                        .stream()
                        .filter((entry) -> {
                    return entry.getValue().equals(constituent);
                }).findFirst().get().getKey();

        if (inheritConstraints) {
            return new PrimitiveField(prefix + pf.getJavaClassName(), pf.contraints, pf.getType());
        }
        return new PrimitiveField(prefix + pf.getJavaClassName(), Collections.EMPTY_SET, pf.getType());
    }

}
