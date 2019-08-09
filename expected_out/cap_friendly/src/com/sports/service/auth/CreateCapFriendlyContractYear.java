package com.sports.service.auth;

import com.sports.model.CapFriendlyContractYear;
import com.sports.datalayer.CapFriendlyContractYearDL;
import com.kamserverutils.common.exec.ErrorType;
import com.kamserverutils.common.exec.ExecutionResult;
import com.kamserverutils.common.util.ServiceUtil;
import com.kamserverutils.common.util.StringUtil;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;

// Auto-generated
@WebServlet("/api/create_cap_friendly_contract_year")
final public class CreateCapFriendlyContractYear extends AuthenticatedServlet { 

    private static final Logger LOGGER = Logger.getLogger(CreateCapFriendlyContractYear.class.getName());

    @Override
    protected Map<String, Object> doGetBody(
        final HttpServletRequest req,
        final HttpServletResponse resp)
        throws Exception {

        final String capFriendlyContractGuid = (String)  req.getParameter("cap_friendly_contract_guid");
        if(StringUtil.isNullOrEmptyStr(capFriendlyContractGuid)) {
            return ServiceUtil.messageMap(new ErrorType("cap_friendly_contract_guid_missing", "CapFriendlyContractYearUuid is a required field"));
        }
        final Double ahl_salary = (String)  req.getParameter("ahl_salary");
        final Double avv = (String)  req.getParameter("avv");
        final Double cap_hit = (String)  req.getParameter("cap_hit");
        final String clause = (String)  req.getParameter("clause");
        final Double nhl_salary = (String)  req.getParameter("nhl_salary");
        final Double p_bonus = (String)  req.getParameter("p_bonus");
        final Double s_bonus = (String)  req.getParameter("s_bonus");
        final String season = (String)  req.getParameter("season");

        try (final Connection conn = getNoAutoCommitConnection()) {
            final ExecutionResult<CapFriendlyContractYear> er = CapFriendlyContractYearDL.create(conn
                , capFriendlyContractGuid
                , ahl_salary
                , avv
                , cap_hit
                , clause
                , nhl_salary
                , p_bonus
                , s_bonus
                , season);
            if(er.isError()) {
                conn.rollback();
                return ServiceUtil.messageMap(er.errorMsg());
            }
            conn.commit();

            return ServiceUtil.successMap("cap_friendly_contract_year", er.entity().toMap());
        }
    }

}

