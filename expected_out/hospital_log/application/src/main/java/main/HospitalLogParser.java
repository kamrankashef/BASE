package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import common.FileUtil;
import common.InitDatabase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import com.fakehospital.model.ShiftEnd;
import com.fakehospital.model.Group;
import com.fakehospital.model.EmployeeMeeting;
import com.fakehospital.model.ErRotation;
import com.fakehospital.model.Employee;
import com.fakehospital.model.Meeting;
import com.fakehospital.model.EmployeesEmpty;
import com.fakehospital.model.Surgery;
import com.fakehospital.model.GroupSurgery;
import com.fakehospital.model.Event;
import com.fakehospital.model.EmployeeGroupSurgery;
import com.fakehospital.model.HospitalEvents;

public class HospitalLogParser {

    public static void main(final String ... args) throws Exception {

        final String fileName = args[0];
        final String xml = FileUtil.fileToString(fileName);
        final Document doc = Jsoup.parse(xml, "", Parser.xmlParser());

        try (Connection conn = InitDatabase.getConnection(false)) {

            for(final Element hospitalEventsElem:doc.select("> HospitalEvents")) {

                final HospitalEvents hospitalEvents = HospitalEvents.fromElem(hospitalEventsElem);

                for(final Element erRotationElem:hospitalEventsElem.select("> ErRotation")) {

                    final ErRotation erRotation = ErRotation.fromElem(erRotationElem);

                    for(final Element groupElem:erRotationElem.select("> Group")) {

                        final Group group = Group.fromElem(groupElem);

                        for(final Element employeesEmptyElem:groupElem.select("> Employees")) {

                            final EmployeesEmpty employeesEmpty = EmployeesEmpty.fromElem(employeesEmptyElem);

                            for(final Element employeeElem:employeesEmptyElem.select("> Employee")) {

                                final Employee employee = Employee.fromElem(employeeElem);
                            }
                        }
                    }

                    for(final Element eventElem:erRotationElem.select("> Event")) {

                        final Event event = Event.fromElem(eventElem);

                        for(final Element shiftEndElem:eventElem.select("> ShiftEnd")) {

                            final ShiftEnd shiftEnd = ShiftEnd.fromElem(shiftEndElem);
                        }

                        for(final Element meetingElem:eventElem.select("> Meeting")) {

                            final Meeting meeting = Meeting.fromElem(meetingElem);

                            for(final Element employeeMeetingElem:meetingElem.select("> Employee")) {

                                final EmployeeMeeting employeeMeeting = EmployeeMeeting.fromElem(employeeMeetingElem);
                            }
                        }

                        for(final Element surgeryElem:eventElem.select("> SURGERY")) {

                            final Surgery surgery = Surgery.fromElem(surgeryElem);

                            for(final Element groupSurgeryElem:surgeryElem.select("> Group")) {

                                final GroupSurgery groupSurgery = GroupSurgery.fromElem(groupSurgeryElem);

                                for(final Element employeeGroupSurgeryElem:groupSurgeryElem.select("> Employee")) {

                                    final EmployeeGroupSurgery employeeGroupSurgery = EmployeeGroupSurgery.fromElem(employeeGroupSurgeryElem);
                                }
                            }
                        }
                    }
                }
            }
            conn.commit();
        }
    }

}
