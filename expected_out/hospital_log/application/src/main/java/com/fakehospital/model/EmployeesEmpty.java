package com.fakehospital.model;

import common.ServiceUtil;
import common.TypeExtract;
import java.util.Set;
import org.jsoup.nodes.Element;

final public class EmployeesEmpty {

    public final String employeesEmptyUuid;

    // AttributeBasedFromElemMethodGenerator
    public static EmployeesEmpty fromElem(final Element elem) throws Exception {

        final Set<String> attributeTracker = ServiceUtil.extractAttributeName(elem.attributes());
        final String employeesEmptyUuid = ServiceUtil.getUUID();

        if (!attributeTracker.isEmpty()) {
            throw new java.lang.RuntimeException("Unaccounted for attributes: " + attributeTracker);
        }

        return new EmployeesEmpty(
                employeesEmptyUuid);

    }

    // ConstructorGenerator
    public EmployeesEmpty(
            final String employeesEmptyUuid) {
        this.employeesEmptyUuid = employeesEmptyUuid;
    }


}
