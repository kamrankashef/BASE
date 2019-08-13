package com.fakehospital.derived.model;


final public class ZSurgery {

    public final String zSurgeryUuid;
    public final String employeeFName;
    public final Long employeeId;
    public final String employeeLName;
    public final Integer floor;
    public final String groupCode;
    public final Long groupId;
    public final String groupName;
    public final String roomNumber;

    // ConstructorGenerator
    public ZSurgery(
            final String zSurgeryUuid,
            final String employeeFName,
            final Long employeeId,
            final String employeeLName,
            final Integer floor,
            final String groupCode,
            final Long groupId,
            final String groupName,
            final String roomNumber) {
        this.zSurgeryUuid = zSurgeryUuid;
        this.employeeFName = employeeFName;
        this.employeeId = employeeId;
        this.employeeLName = employeeLName;
        this.floor = floor;
        this.groupCode = groupCode;
        this.groupId = groupId;
        this.groupName = groupName;
        this.roomNumber = roomNumber;
    }

    // DerivedModelConstructorGenerator
    public ZSurgery(
            final com.fakehospital.model.Surgery surgery,
            final com.fakehospital.model.Employee employee,
            final com.fakehospital.model.Group group) {
        this.zSurgeryUuid = common.ServiceUtil.getUUID();
        this.floor = surgery.floor;
        this.roomNumber = surgery.roomNumber;
        this.employeeFName = employee.fName;
        this.employeeId = employee.id;
        this.employeeLName = employee.lName;
        this.groupCode = group.code;
        this.groupId = group.id;
        this.groupName = group.name;
    }


}
