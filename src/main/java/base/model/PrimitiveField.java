package base.model;

import base.model.sql.DBVendorI;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

// TODO This with Abstract field are a mess, clean them up
// TODO Distinction between Primitive and Abstract fields serves no purpose, merge them
public class PrimitiveField extends AbstractField {

    @SerializedName("primitive_type")
    private PrimitiveType primitiveType;
    @SerializedName("name")
    private String name;
    @SerializedName("third_party_identifier")
    public String thirdPartyIdentifier;

    public PrimitiveField(final String name,
            final String thirdPartyIdentifier,
            final PrimitiveType primitiveType) {
        this(name, thirdPartyIdentifier, Collections.EMPTY_SET, primitiveType);
    }

    public PrimitiveField(final String name,
            final String thirdPartyIdentifier,
            final PrimitiveType primitiveType,
            final boolean nullable) {
        this(name, thirdPartyIdentifier, primitiveType);
        setNullable(nullable);
    }

    public PrimitiveField(final String name,
            final PrimitiveType primitiveType) {
        this(name, name, Collections.EMPTY_SET, primitiveType);
    }

    public PrimitiveField(final String name,
            final Constraint contraint,
            final PrimitiveType primitiveType) {
        this(name, name, Collections.singleton(contraint), primitiveType);
    }

    public PrimitiveField(final String name,
            final String thirdPartyIdentifier,
            final Constraint contraint,
            final PrimitiveType primitiveType) {
        this(name, thirdPartyIdentifier, Collections.singleton(contraint), primitiveType);
    }

    public PrimitiveField(final String baseName,
            final Set<Constraint> contraints,
            final PrimitiveType primitiveType) {
        this(baseName, baseName, contraints, primitiveType);
    }

    public PrimitiveField(final String baseName,
            final String thirdPartyIdentifier,
            final Set<Constraint> contraints,
            final PrimitiveType primitiveType) {
        super(contraints);
        this.name = baseName;
        this.primitiveType = primitiveType;
        this.thirdPartyIdentifier = thirdPartyIdentifier;
    }

    public PrimitiveField(final String name, final PrimitiveType primitiveType, final boolean nullable) {
        this(name, nullable ? Collections.EMPTY_SET : Collections.singleton(Constraint.NOT_NULL), primitiveType);
    }

    public PrimitiveType getPrimitiveType() {
        return this.primitiveType;
    }

    public void setPrimitiveType(final PrimitiveType primitiveType) {
        this.primitiveType = primitiveType;
    }

    public void setThirdPartyIdentifier(final String thirdPartyIdentifier) {
        this.thirdPartyIdentifier = thirdPartyIdentifier;
    }

    public String getThirdPartyIdentifier() {
        return this.thirdPartyIdentifier;
    }

    @Override
    public String toDBRow(final DBVendorI dbVendor) {
        return dbVendor.toDBRow(this.toDBName(), primitiveType, this.nullable());
    }

    @Override
    public String toJavaType() {
        return this.primitiveType.toJavaTypeName();
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getNullableMethod() {
        return primitiveType.getNullableMethod();
    }

    @Override
    public String setNullableMethod() {
        return primitiveType.setNullableMethod();
    }

    @Override
    public int hashCode() {
        return (this.primitiveType + this.name).hashCode();
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
        final PrimitiveField other = (PrimitiveField) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.primitiveType != other.primitiveType) {
            return false;
        }
        return true;
    }

    public PrimitiveType getType() {
        return this.primitiveType;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
