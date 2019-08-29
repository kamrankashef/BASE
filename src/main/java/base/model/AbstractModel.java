package base.model;

import base.gen.DLGen;
import base.gen.ModelGen;
import base.gen.SourceBuilder;
import base.parsergen.rules.ModelAugmenterI;
import base.util.CaseConversion;
import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;
import kamserverutils.common.util.StringUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

/**
 * A model has - subpackage - unstructuredName - primitiveFields (native) ->
 * augmented fields (native) - primitiveFields (from constituent Models) ->
 * augmented fields (from constituent Models) - fkFields (native only)
 */
public abstract class AbstractModel
        implements
        MetaObject,
        Comparable<AbstractModel> {

    @SerializedName("unstructured_name")
    private volatile String unstructuredName;

    @SerializedName("guid_field")
    private final PrimitiveField guidField;

    @SerializedName("schema")
    private volatile String schema;

    @SerializedName("table_name")
    private volatile String tableName;

    @SerializedName("service_package")
    private volatile String servicePackage;

    @SerializedName("model_package")
    private volatile String modelPackage;

    @SerializedName("dl_package")
    private volatile String dlPackage;

    @SerializedName("org")
    private volatile String subpackage;

    @SerializedName("java_class_name")
    private volatile String javaClassName;

    @SerializedName("third_party_mappings") // TODO, does this actually get used?
    private Map<String, String> thirdPartyMapping = new HashMap<>();

    @SerializedName("uuid")
    private final String uuid;

    @SerializedName("suppressed_fields")
    protected final Set<String> suppressedFields = new HashSet<>();

    @SerializedName("reference_fields")
    protected final Set<String> referencedFields = new HashSet<>();

    @SerializedName("reference_models")
    protected final Set<String> referencedModels = new HashSet<>();

    final transient private List<ModelGen.ModelMethodGenerator> modelGenerators = new LinkedList<>();
    final transient private List<DLGen.DLMethodGenerator> dlGenerators = new LinkedList<>();

    public static <T extends MetaObject> Map<String, T> metaObjectToUUIDLookup(Collection<T> fields) {
        final Map<String, T> manualFieldsLookup
                = new HashMap<>();
        for (final T field : fields) {
            manualFieldsLookup.put(field.uuid(), field);
        }
        return manualFieldsLookup;
    }

    public abstract void addFKField(final ForiegnKeyField field);

    public abstract void setExternalFields(final Map<String, PrimitiveField> externalFields);

    public abstract List<PrimitiveField> getLinkedFields();

    public abstract void setExternalModels(final Map<String, AbstractModel> externalModels);

    public abstract TreeMap<String, AbstractModel> getConstituentModels();

    public abstract TreeMap<String, AbstractModel> getConstituentModelsPlusLinked();

    public abstract List<AbstractModel> getLinkedModels();

    public abstract List<AbstractModel> getAvailableLinkedModels();

    private List<List<AbstractField>> keys = new LinkedList<>();

    public void addKey(final AbstractField ... fields) {
        this.keys.add(Lists.newArrayList(fields));
    }

    public List<List<AbstractField>> getKeys() {
        return keys;
    }

    public final Set<String> suppressedFields() {
        return new HashSet<>(this.suppressedFields);
    }

    public final void suppressField(final String uuid) {
        suppressedFields.add(uuid);
    }

    public final void unsuppressField(final String uuid) {
        suppressedFields.remove(uuid);
    }

    // For fields managed else where
    public final void addReferenceField(final String uuid) {
        referencedFields.add(uuid);
    }

    public Collection<String> referencedFields() {
        return new LinkedList<>(referencedFields);
    }

    public final void removeReferenceField(final String uuid) {
        referencedFields.remove(uuid);
    }

    public final void addReferenceModel(final String uuid) {
        referencedModels.add(uuid);
    }

    public Collection<String> referencedModels() {
        return new LinkedList<>(referencedModels);
    }

    public final void removeReferenceModel(final String uuid) {
        referencedModels.remove(uuid);
    }

    public String getExpanededModelPackage(final String parentPkg) {
        if (StringUtil.isNullOrEmptyStr(parentPkg)) {
            return this.modelPackage;
        }
        return parentPkg + "." + modelPackage;
    }

    public String getExpanededDLPackage(final String parentPkg) {
        if (StringUtil.isNullOrEmptyStr(parentPkg)) {
            return this.dlPackage;
        }
        return parentPkg + "." + dlPackage;
    }

    public String getExpanededServicePackage(final String parentPkg) {
        if (StringUtil.isNullOrEmptyStr(parentPkg)) {
            return this.servicePackage;
        }
        return parentPkg + "." + servicePackage;
    }

    public AbstractModel(
            final String unstructuredName,
            final String org
    ) {
        uuid = UUID.randomUUID().toString();
        guidField = new PrimitiveField("UnsetUuid", Collections.EMPTY_SET, PrimitiveType.CHAR_36);
//        this.guidField.setNullable(false);
        this.guidField.setNotUserGenerate();

        this.applyName(unstructuredName, unstructuredName);
        setOrg(org);
    }

    final public void setOrg(final String org) {
        this.subpackage = org;
        this.servicePackage = org + ".service.auth";
        this.modelPackage = org + ".model";
        this.dlPackage = org + ".datalayer";
    }

    public final void applyName(final String name, String unstructuredName) {
        this.unstructuredName = unstructuredName;
        final String[] parts = name.split("\\.");

        switch (parts.length) {
            case 2:
                this.javaClassName = CaseConversion.toJavaClassName(parts[1]);
                schema = CaseConversion.toDBName(parts[0]);
                break;
            case 1:
                this.javaClassName = CaseConversion.toJavaClassName(parts[0]);
                schema = null;
                break;
            default:
                throw new RuntimeException();
        }
        final String candiateName = CaseConversion.javaClassNameToDBTableName(this.javaClassName);
        if (AbstractField.isSQLReservedWord(candiateName)) {
            tableName = candiateName + "_";
        } else {
            tableName = candiateName;
        }

        this.guidField.setName(javaClassName + "Uuid");

    }

    abstract public Set<ForiegnKeyField> getFkFields();

    abstract public Set<PrimitiveField> getSimplePrimitiveFields();

    abstract public void addPrimitiveField(final PrimitiveField field);

    abstract public List<PrimitiveField> getPrimitiveFields();

    abstract public List<PrimitiveField> getPrimitiveFieldsWithLinked();

    abstract public List<PrimitiveField> getAvailablePrimitiveFields();

    abstract public Map<PrimitiveField, String> getAugmentedFields();

    @Override
    final public String uuid() {
        return this.uuid;
    }

    public List<AbstractField> allOriginalFields() {
        final List<AbstractField> all = new LinkedList<>();
        all.add(getGuidField());
        all.addAll(allNonGuidOriginalFields());
        return Collections.unmodifiableList(all);
    }

    public List<AbstractField> allNonGuidOriginalFields() {

        // TODO for some reason TreeSet sets off exception but hash set does not
        final Set<AbstractField> allSet = new TreeSet<>();
        final List<AbstractField> all = new LinkedList<>();
        all.addAll(getFkFields());
        all.addAll(getPrimitiveFieldsWithLinked());
        all.addAll(getAugmentedFields().keySet());
        allSet.addAll(getFkFields());
        allSet.addAll(getPrimitiveFieldsWithLinked());
        allSet.addAll(getAugmentedFields().keySet());

        // TODO Figure out why this could happen
        if (allSet.size() != all.size()) {
            throw new RuntimeException("all(" + all.size() + ") != allSet(" + allSet.size() + ")");
        }

        return Collections.unmodifiableList(all);
    }

    @Override
    final public int compareTo(final AbstractModel o) {
        return getJavaClassName().compareTo(o.getJavaClassName());
    }

    final public String toJavaDeclaration() {
        return this.toJavaDeclaration(false);
    }

    final public String toJavaDeclaration(final boolean fullyQualified) {
        if (!fullyQualified) {
            return "final " + this.getJavaClassName() + " " + this.toJavaVariableName();
        } else {
            return "final " + this.getCanonicalName("") + " " + this.toJavaVariableName();
        }
    }

    final public String toFullyQualifiedJavaDeclaration(final String parentPackage) {
        return "final " + this.getExpanededModelPackage(parentPackage) + "." + this.getJavaClassName() + " " + this.toJavaVariableName();
    }

    @Override
    final public String getJavaClassName() {
        return javaClassName;
    }

    @Override
    final public int hashCode() {
        return this.getJavaClassName().hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractModel other = (AbstractModel) obj;

        return Objects.equals(this.getJavaClassName(), other.getJavaClassName());
    }

//    final private String getUnstrcturedName() {
//        return this.getUnstructuredName();
//    }
    final public String toDBName() {
        return (schema == null ? "" : (schema + ".")) + tableName;
    }

    final public String toJavaVariableName() {
        return CaseConversion.toJavaVariableName(javaClassName);
    }

    final public String dlName() {
        return getJavaClassName() + "DL";
    }

    final public String toEnglish() {
        return CaseConversion.toEnglish(this.toDBName());
    }

    final public String calledConstructor(final int tabCount) {

        final SourceBuilder bldr = new SourceBuilder();

        bldr.append("new ")
                .append(this.getJavaClassName())
                .append("(");
        {
            boolean first = true;
            for (final AbstractField field : this.allOriginalFields()) {
                bldr.append(first ? "" : ",").append("\n")
                        .append(tabCount + 1, field.toJavaVariableName());

                first = false;
            }
        }
        bldr.append(")");
        return bldr.toString();
    }

    final public Map<String, String> getThirdPartyMapping() {
        return thirdPartyMapping;
    }

    final public void setThirdPartyMapping(final Map<String, String> thirdPartyMapping) {
        this.thirdPartyMapping = thirdPartyMapping;
    }

    final public String getUnstructuredName() {
        return unstructuredName;
    }

    final public void setUnstructuredName(final String unstructuredName) {
        this.unstructuredName = unstructuredName;
    }

    final public PrimitiveField getGuidField() {
        return guidField;
    }

    final public String getServicePackage() {
        return servicePackage;
    }

    final public String getModelPackage() {
        return modelPackage;
    }

    final public String getDlPackage() {
        return dlPackage;
    }

    final public String getOrg() {
        return subpackage;
    }

    public final String getCanonicalName(final String parentPackage) {
        return getExpanededModelPackage(parentPackage) + "." + javaClassName;
    }

    abstract public void applyAugmented(final ModelAugmenterI modelAugmenter);

    @Override
    public String toString() {
        return getJavaClassName();
    }
}
