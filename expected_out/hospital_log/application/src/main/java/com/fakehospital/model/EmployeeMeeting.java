package com.fakehospital.model;

import common.ServiceUtil;
import common.TypeExtract;
import java.util.Set;
import org.jsoup.nodes.Element;

final public class EmployeeMeeting {

    public final String employeeMeetingUuid;
    public final Long id;

    // AttributeBasedFromElemMethodGenerator
    public static EmployeeMeeting fromElem(final Element elem) throws Exception {

        final Set<String> attributeTracker = ServiceUtil.extractAttributeName(elem.attributes());
        final String employeeMeetingUuid = ServiceUtil.getUUID();

        final Long id = TypeExtract.getLong((String) elem.attr("ID"));
        if (id == null) {
            throw new java.lang.RuntimeException("Got null value for id");
        }
        attributeTracker.remove("ID");

        if (!attributeTracker.isEmpty()) {
            throw new java.lang.RuntimeException("Unaccounted for attributes: " + attributeTracker);
        }

        return new EmployeeMeeting(
                employeeMeetingUuid,
                id);

    }

    // ConstructorGenerator
    public EmployeeMeeting(
            final String employeeMeetingUuid,
            final Long id) {
        this.employeeMeetingUuid = employeeMeetingUuid;
        this.id = id;
    }


}
