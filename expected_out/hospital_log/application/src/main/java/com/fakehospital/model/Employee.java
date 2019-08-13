package com.fakehospital.model;

import common.ServiceUtil;
import common.TypeExtract;
import java.util.Set;
import org.jsoup.nodes.Element;

final public class Employee {

    public final String employeeUuid;
    public final String fName;
    public final Long id;
    public final String lName;

    // AttributeBasedFromElemMethodGenerator
    public static Employee fromElem(final Element elem) throws Exception {

        final Set<String> attributeTracker = ServiceUtil.extractAttributeName(elem.attributes());
        final String employeeUuid = ServiceUtil.getUUID();

        final String fName = TypeExtract.getString((String) elem.attr("FName"));
        if (fName == null) {
            throw new java.lang.RuntimeException("Got null value for fName");
        }
        attributeTracker.remove("FName");

        final Long id = TypeExtract.getLong((String) elem.attr("ID"));
        if (id == null) {
            throw new java.lang.RuntimeException("Got null value for id");
        }
        attributeTracker.remove("ID");

        final String lName = TypeExtract.getString((String) elem.attr("LName"));
        if (lName == null) {
            throw new java.lang.RuntimeException("Got null value for lName");
        }
        attributeTracker.remove("LName");

        if (!attributeTracker.isEmpty()) {
            throw new java.lang.RuntimeException("Unaccounted for attributes: " + attributeTracker);
        }

        return new Employee(
                employeeUuid,
                fName,
                id,
                lName);

    }

    // ConstructorGenerator
    public Employee(
            final String employeeUuid,
            final String fName,
            final Long id,
            final String lName) {
        this.employeeUuid = employeeUuid;
        this.fName = fName;
        this.id = id;
        this.lName = lName;
    }


}
