package base.model;

import base.model.sql.DBVendorI;
import com.google.gson.annotations.SerializedName;
import java.util.Set;

public class ForiegnKeyField extends AbstractField {

    @SerializedName("fk_model")
    final public AbstractModel fkModel;
    @SerializedName("on_delete_cascade")
    public final boolean onDeleteCascade;
    
    public ForiegnKeyField(
            final Set<Constraint> contraints,
            final AbstractModel fkModel, // TODO Is there a benefit to making this limited to Models vs AbstractModels?
            final boolean onDeleteCascade) {
        super(contraints);
        this.fkModel = fkModel;
        this.onDeleteCascade = onDeleteCascade;
    }

    @Override
    public String toDBRow(final DBVendorI dbVendor) {

        final String fkTable = fkModel.toDBName();
        // TODO Change to UUID
        final String fkRefName = fkTable + "_guid";

        return dbVendor.fkField(fkRefName, fkTable, fkRefName, this.nullable(), this.onDeleteCascade);
        

    }

    @Override
    public String toJavaType() {
        return "String";
    }

    @Override
    public PrimitiveType getPrimitiveType() {
        return PrimitiveType.CHAR_36;
    }

    @Override
    public String getName() {
        return fkModel.getJavaClassName() + "Guid";
    }

    @Override
    public String getNullableMethod() {
        return "getNullableString";
    }

    @Override
    public String setNullableMethod() {
        return "setNullableString";
    }

}
