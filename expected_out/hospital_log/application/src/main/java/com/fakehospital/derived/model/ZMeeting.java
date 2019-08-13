package com.fakehospital.derived.model;


final public class ZMeeting {

    public final String zMeetingUuid;
    public final String description;
    public final String employeeFName;
    public final Long employeeId;
    public final String employeeLName;

    // ConstructorGenerator
    public ZMeeting(
            final String zMeetingUuid,
            final String description,
            final String employeeFName,
            final Long employeeId,
            final String employeeLName) {
        this.zMeetingUuid = zMeetingUuid;
        this.description = description;
        this.employeeFName = employeeFName;
        this.employeeId = employeeId;
        this.employeeLName = employeeLName;
    }

    // DerivedModelConstructorGenerator
    public ZMeeting(
            final com.fakehospital.model.Meeting meeting,
            final com.fakehospital.model.Employee employee) {
        this.zMeetingUuid = common.ServiceUtil.getUUID();
        this.description = meeting.description;
        this.employeeFName = employee.fName;
        this.employeeId = employee.id;
        this.employeeLName = employee.lName;
    }


}
