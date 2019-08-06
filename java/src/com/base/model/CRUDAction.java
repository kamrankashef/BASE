package com.base.model;

import com.base.util.CaseConversion;

public enum CRUDAction {

    CREATE("create", false, true, true, true),
    READ("get", true, false, true, false),
    UPDATE("update", true, true, true, true),
    DELETE("delete", true, false, false, true);

    private final String asString;
    public final boolean requiresGuid;
    public final boolean includeObjParams;
    public final boolean returnsObj;
    public final boolean requiresCommit;

    private CRUDAction(final String asString,
            final boolean requiresGuid,
            final boolean includeObjParams,
            final boolean returnsObj,
            final boolean requiresCommit
    ) {
        this.asString = asString;
        this.requiresGuid = requiresGuid;
        this.includeObjParams = includeObjParams;
        this.returnsObj = returnsObj;
        this.requiresCommit = requiresCommit;
    }

    public String getAPIServiceName(final AbstractModel model) {
        return asString + "_" + model.toDBName();
    }

    public String returnType(final AbstractModel model) {
        if (!returnsObj) {
            return "Void";
        }
        return model.getJavaClassName();
    }

    public String getAPIClassName(final AbstractModel model) {
        return CaseConversion.firstLetterUp(this.asString + model.getJavaClassName());
    }

    @Override
    public String toString() {
        return this.asString;
    }

}
