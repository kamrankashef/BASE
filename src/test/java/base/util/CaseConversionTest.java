package base.util;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CaseConversionTest {

    @Test
    public void isUpperTest() {
        assertFalse(CaseConversion.isUpper("1"));
        assertTrue(CaseConversion.isDigit("1"));
    }

    @Test
    public void testToJavaVarName() {
        assertEquals("toi3EmptyNetEv", CaseConversion.toJavaVariableName("TOI3EmptyNetEV"));

        assertEquals("hi", CaseConversion.toJavaVariableName("Hi"));
        assertEquals("isTempId", CaseConversion.toJavaVariableName("IsTempID"));
        assertEquals("id", CaseConversion.toJavaVariableName("ID"));
        assertEquals("i", CaseConversion.toJavaVariableName("I"));
        assertEquals("id", CaseConversion.toJavaVariableName("ID"));
        assertEquals("bye", CaseConversion.toJavaVariableName("bye"));
        assertEquals("fooBar", CaseConversion.toJavaVariableName("fooBar"));
        assertEquals("bye", CaseConversion.toJavaVariableName("BYE"));
        assertEquals("fName", CaseConversion.toJavaVariableName("FName"));
        assertEquals("trIck", CaseConversion.toJavaVariableName("TRIck"));
        assertEquals("faceoffs3v3Won", CaseConversion.toJavaVariableName("faceoffs3v3Won"));
        assertEquals("toiEmptyNetEv", CaseConversion.toJavaVariableName("TOIEmptyNetEV"));
        assertEquals("homeCoachFName", CaseConversion.toJavaVariableName("homeCoachFName"));

        assertEquals("assistaId", CaseConversion.toJavaVariableName("AssistaID"));
        assertEquals("assist1Id", CaseConversion.toJavaVariableName("Assist1ID"));
        assertEquals("toi4v5Aug", CaseConversion.toJavaVariableName("TOI4v5Aug"));

        assertEquals("fooX", CaseConversion.toJavaVariableName("FooX"));
        assertEquals("fooX", CaseConversion.toJavaVariableName("fooX"));
    }

    @Test
    public void toJavaClassName() {
        assertEquals("Ab", CaseConversion.toJavaClassName("ab"));
        assertEquals("Ab", CaseConversion.toJavaClassName("AB"));
        assertEquals("I", CaseConversion.toJavaClassName("I"));
        assertEquals("I", CaseConversion.toJavaClassName("i"));
        assertEquals("I_foo", CaseConversion.toJavaClassName("i_foo"));
        assertEquals("Points_Against_Pg", CaseConversion.toJavaClassName("Points_Against_PG"));
        assertEquals("HS_state", CaseConversion.toJavaClassName("HS_state"));
        assertEquals("EventNum", CaseConversion.toJavaClassNameUnunderscore("Event_Num"));
        assertEquals("Eventnum", CaseConversion.toJavaClassNameUnunderscore("EventNum"));
    }

    @Test
    public void testToDBColName() {

        assertEquals("ab", CaseConversion.toDBName("Ab"));
        assertEquals("hi", CaseConversion.toDBName("Hi"));
        assertEquals("i", CaseConversion.toDBName("i"));
        assertEquals("is_temp_id", CaseConversion.toDBName("IsTempID"));
        assertEquals("id", CaseConversion.toDBName("ID"));
        assertEquals("bye", CaseConversion.toDBName("bye"));
        assertEquals("foo_bar", CaseConversion.toDBName("fooBar"));
        assertEquals("bye", CaseConversion.toDBName("BYE"));
        assertEquals("f_name", CaseConversion.toDBName("FName"));
        assertEquals("tr_ick", CaseConversion.toDBName("TRIck"));
        assertEquals("faceoffs3v3_won", CaseConversion.toDBName("faceoffs3v3Won"));
        assertEquals("toi_empty_net_ev", CaseConversion.toDBName("TOIEmptyNetEV"));
        assertEquals("home_coach_f_name", CaseConversion.toDBName("homeCoachFName"));
        assertEquals("first__second", CaseConversion.toDBName("First_Second"));

        assertEquals("hit__ab", CaseConversion.toDBName("Hit_Ab"));
        assertEquals("first__sx", CaseConversion.toDBName("First_Sx"));
        assertEquals("first__sx", CaseConversion.toDBName("First_Sx"));
        assertEquals("first__t__blah", CaseConversion.toDBName("First_T_Blah"));
        assertEquals("first__t", CaseConversion.toDBName("First_T"));
        assertEquals("first__sx", CaseConversion.toDBName("First_Sx"));
        assertEquals("first_sx", CaseConversion.toDBName("First_sx"));
        assertEquals("firs_t__sx", CaseConversion.toDBName("Firs_t_Sx"));
        assertEquals("firs__t__sx", CaseConversion.toDBName("Firs_T_Sx"));

        assertEquals("first__s", CaseConversion.toDBName("First_S"));
        assertEquals("points_against_pg", CaseConversion.toDBName("PointsAgainstPG"));
        assertEquals("hs_state", CaseConversion.toDBName("Hs_state"));
    }

    @Test
    public void testFirstToUpper() {
        assertEquals("Bye", CaseConversion.firstLetterUp("bye"));
        assertEquals("A", CaseConversion.firstLetterUp("a"));

    }
}

