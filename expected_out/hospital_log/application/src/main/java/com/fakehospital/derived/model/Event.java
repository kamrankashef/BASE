package com.fakehospital.derived.model;


final public class Event {

    public final String eventUuid;
    public final String customAttribute;
    public final String eventEasternTimeZoneTime;
    public final String eventId;
    public final Integer eventLocalDate;
    public final String eventLocalTime;
    public final String eventName;
    public final Integer eventNumber;
    public final String meetingDescription;
    public final String meetingEmployeeFName;
    public final Long meetingEmployeeId;
    public final String meetingEmployeeLName;
    public final String shiftEndSummary;
    public final String surgeryEmployeeFName;
    public final Long surgeryEmployeeId;
    public final String surgeryEmployeeLName;
    public final Integer surgeryFloor;
    public final String surgeryGroupCode;
    public final Long surgeryGroupId;
    public final String surgeryGroupName;
    public final String surgeryRoomNumber;

    // ConstructorGenerator
    public Event(
            final String eventUuid,
            final String customAttribute,
            final String eventEasternTimeZoneTime,
            final String eventId,
            final Integer eventLocalDate,
            final String eventLocalTime,
            final String eventName,
            final Integer eventNumber,
            final String meetingDescription,
            final String meetingEmployeeFName,
            final Long meetingEmployeeId,
            final String meetingEmployeeLName,
            final String shiftEndSummary,
            final String surgeryEmployeeFName,
            final Long surgeryEmployeeId,
            final String surgeryEmployeeLName,
            final Integer surgeryFloor,
            final String surgeryGroupCode,
            final Long surgeryGroupId,
            final String surgeryGroupName,
            final String surgeryRoomNumber) {
        this.eventUuid = eventUuid;
        this.customAttribute = customAttribute;
        this.eventEasternTimeZoneTime = eventEasternTimeZoneTime;
        this.eventId = eventId;
        this.eventLocalDate = eventLocalDate;
        this.eventLocalTime = eventLocalTime;
        this.eventName = eventName;
        this.eventNumber = eventNumber;
        this.meetingDescription = meetingDescription;
        this.meetingEmployeeFName = meetingEmployeeFName;
        this.meetingEmployeeId = meetingEmployeeId;
        this.meetingEmployeeLName = meetingEmployeeLName;
        this.shiftEndSummary = shiftEndSummary;
        this.surgeryEmployeeFName = surgeryEmployeeFName;
        this.surgeryEmployeeId = surgeryEmployeeId;
        this.surgeryEmployeeLName = surgeryEmployeeLName;
        this.surgeryFloor = surgeryFloor;
        this.surgeryGroupCode = surgeryGroupCode;
        this.surgeryGroupId = surgeryGroupId;
        this.surgeryGroupName = surgeryGroupName;
        this.surgeryRoomNumber = surgeryRoomNumber;
    }

    // DerivedModelConstructorGenerator
    public Event(
            final String customAttribute,
            final com.fakehospital.model.Event event,
            final com.fakehospital.derived.model.ZMeeting zMeeting,
            final com.fakehospital.model.ShiftEnd shiftEnd,
            final com.fakehospital.derived.model.ZSurgery zSurgery) {
        this.eventUuid = common.ServiceUtil.getUUID();
        this.customAttribute = customAttribute;
        this.eventEasternTimeZoneTime = event.easternTimeZoneTime;
        this.eventId = event.id;
        this.eventLocalDate = event.localDate;
        this.eventLocalTime = event.localTime;
        this.eventName = event.name;
        this.eventNumber = event.number;
        this.meetingDescription = zMeeting.description;
        this.meetingEmployeeFName = zMeeting.employeeFName;
        this.meetingEmployeeId = zMeeting.employeeId;
        this.meetingEmployeeLName = zMeeting.employeeLName;
        this.shiftEndSummary = shiftEnd.summary;
        this.surgeryEmployeeFName = zSurgery.employeeFName;
        this.surgeryEmployeeId = zSurgery.employeeId;
        this.surgeryEmployeeLName = zSurgery.employeeLName;
        this.surgeryFloor = zSurgery.floor;
        this.surgeryGroupCode = zSurgery.groupCode;
        this.surgeryGroupId = zSurgery.groupId;
        this.surgeryGroupName = zSurgery.groupName;
        this.surgeryRoomNumber = zSurgery.roomNumber;
    }


}
