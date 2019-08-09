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
@WebServlet("/api/get_cap_friendly_contract_year")
final public class GetCapFriendlyContractYear extends AuthenticatedServlet { 

    private static final Logger LOGGER = Logger.getLogger(GetCapFriendlyContractYear.class.getName());

    @Override
    protected Map<String, Object> doGetBody(
        final HttpServletRequest req,
        final HttpServletResponse resp)
        throws Exception {

        final String capFriendlyContractYearUuid = (String)  req.getParameter("cap_friendly_contract_year_uuid");
        if(StringUtil.isNullOrEmptyStr(capFriendlyContractYearUuid)) {
            return ServiceUtil.messageMap(new ErrorType("cap_friendly_contract_year_uuid_missing", "CapFriendlyContractYearUuid is a required field"));
        }


        try (final Connection conn = getNoAutoCommitConnection()) {
            final ExecutionResult<CapFriendlyContractYear> er = CapFriendlyContractYearDL.get(conn
                , capFriendlyContractYearUuid);
            if(er.isError()) {
                return ServiceUtil.messageMap(er.errorMsg());
            }

            return ServiceUtil.successMap("cap_friendly_contract_year", er.entity().toMap());
        }
    }

}

