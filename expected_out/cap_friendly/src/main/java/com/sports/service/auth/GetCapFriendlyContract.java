package com.sports.service.auth;

import com.sports.model.CapFriendlyContract;
import com.sports.datalayer.CapFriendlyContractDL;
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
@WebServlet("/api/get_cap_friendly_contract")
final public class GetCapFriendlyContract extends AuthenticatedServlet { 

    private static final Logger LOGGER = Logger.getLogger(GetCapFriendlyContract.class.getName());

    @Override
    protected Map<String, Object> doGetBody(
        final HttpServletRequest req,
        final HttpServletResponse resp)
        throws Exception {

        final String capFriendlyContractUuid = (String)  req.getParameter("cap_friendly_contract_uuid");
        if(StringUtil.isNullOrEmptyStr(capFriendlyContractUuid)) {
            return ServiceUtil.messageMap(new ErrorType("cap_friendly_contract_uuid_missing", "CapFriendlyContractUuid is a required field"));
        }


        try (final Connection conn = getNoAutoCommitConnection()) {
            final ExecutionResult<CapFriendlyContract> er = CapFriendlyContractDL.get(conn
                , capFriendlyContractUuid);
            if(er.isError()) {
                return ServiceUtil.messageMap(er.errorMsg());
            }

            return ServiceUtil.successMap("cap_friendly_contract", er.entity().toMap());
        }
    }

}

