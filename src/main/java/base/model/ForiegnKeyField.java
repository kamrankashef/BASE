package base.model;

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
    public String toDBRow() {

        final String fkTable = fkModel.toDBName();
        final String fkRefName = fkTable + "_guid";
        
        String returnStr
                = fkRefName + " " + PrimitiveType.CHAR_36.sqlType + (nullable() ? "" : " NOT NULL") +"\n"
                + ",FOREIGN KEY (" + fkRefName + ")"
                + " REFERENCES " + fkTable + "(" + fkRefName +")";
        
        if (this.onDeleteCascade) {
            returnStr += " ON DELETE CASCADE";
        }
        return returnStr;
    }

    @Override
    public String toJavaType() {
        return "String";
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
