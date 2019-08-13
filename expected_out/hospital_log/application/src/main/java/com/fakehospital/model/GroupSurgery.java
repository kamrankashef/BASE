package com.fakehospital.model;

import common.ServiceUtil;
import common.TypeExtract;
import java.util.Set;
import org.jsoup.nodes.Element;

final public class GroupSurgery {

    public final String groupSurgeryUuid;
    public final Long id;
    public final String role;

    // AttributeBasedFromElemMethodGenerator
    public static GroupSurgery fromElem(final Element elem) throws Exception {

        final Set<String> attributeTracker = ServiceUtil.extractAttributeName(elem.attributes());
        final String groupSurgeryUuid = ServiceUtil.getUUID();

        final Long id = TypeExtract.getLong((String) elem.attr("ID"));
        if (id == null) {
            throw new java.lang.RuntimeException("Got null value for id");
        }
        attributeTracker.remove("ID");

        final String role = TypeExtract.getString((String) elem.attr("Role"));
        if (role == null) {
            throw new java.lang.RuntimeException("Got null value for role");
        }
        attributeTracker.remove("Role");

        if (!attributeTracker.isEmpty()) {
            throw new java.lang.RuntimeException("Unaccounted for attributes: " + attributeTracker);
        }

        return new GroupSurgery(
                groupSurgeryUuid,
                id,
                role);

    }

    // ConstructorGenerator
    public GroupSurgery(
            final String groupSurgeryUuid,
            final Long id,
            final String role) {
        this.groupSurgeryUuid = groupSurgeryUuid;
        this.id = id;
        this.role = role;
    }


}
