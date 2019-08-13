package com.fakehospital.model;

import common.ServiceUtil;
import common.TypeExtract;
import java.util.Set;
import org.jsoup.nodes.Element;

final public class EmployeeGroupSurgery {

    public final String employeeGroupSurgeryUuid;
    public final Long id;

    // AttributeBasedFromElemMethodGenerator
    public static EmployeeGroupSurgery fromElem(final Element elem) throws Exception {

        final Set<String> attributeTracker = ServiceUtil.extractAttributeName(elem.attributes());
        final String employeeGroupSurgeryUuid = ServiceUtil.getUUID();

        final Long id = TypeExtract.getLong((String) elem.attr("ID"));
        if (id == null) {
            throw new java.lang.RuntimeException("Got null value for id");
        }
        attributeTracker.remove("ID");

        if (!attributeTracker.isEmpty()) {
            throw new java.lang.RuntimeException("Unaccounted for attributes: " + attributeTracker);
        }

        return new EmployeeGroupSurgery(
                employeeGroupSurgeryUuid,
                id);

    }

    // ConstructorGenerator
    public EmployeeGroupSurgery(
            final String employeeGroupSurgeryUuid,
            final Long id) {
        this.employeeGroupSurgeryUuid = employeeGroupSurgeryUuid;
        this.id = id;
    }


}
