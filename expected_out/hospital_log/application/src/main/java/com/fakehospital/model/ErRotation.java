package com.fakehospital.model;

import common.ServiceUtil;
import common.TypeExtract;
import java.util.Set;
import org.jsoup.nodes.Element;

final public class ErRotation {

    public final String erRotationUuid;
    public final String hospital;
    public final Long id;

    // AttributeBasedFromElemMethodGenerator
    public static ErRotation fromElem(final Element elem) throws Exception {

        final Set<String> attributeTracker = ServiceUtil.extractAttributeName(elem.attributes());
        final String erRotationUuid = ServiceUtil.getUUID();

        final String hospital = TypeExtract.getString((String) elem.attr("Hospital"));
        if (hospital == null) {
            throw new java.lang.RuntimeException("Got null value for hospital");
        }
        attributeTracker.remove("Hospital");

        final Long id = TypeExtract.getLong((String) elem.attr("ID"));
        if (id == null) {
            throw new java.lang.RuntimeException("Got null value for id");
        }
        attributeTracker.remove("ID");

        if (!attributeTracker.isEmpty()) {
            throw new java.lang.RuntimeException("Unaccounted for attributes: " + attributeTracker);
        }

        return new ErRotation(
                erRotationUuid,
                hospital,
                id);

    }

    // ConstructorGenerator
    public ErRotation(
            final String erRotationUuid,
            final String hospital,
            final Long id) {
        this.erRotationUuid = erRotationUuid;
        this.hospital = hospital;
        this.id = id;
    }


}
