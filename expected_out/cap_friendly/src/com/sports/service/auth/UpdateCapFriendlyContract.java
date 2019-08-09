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
@WebServlet("/api/update_cap_friendly_contract")
final public class UpdateCapFriendlyContract extends AuthenticatedServlet { 

    private static final Logger LOGGER = Logger.getLogger(UpdateCapFriendlyContract.class.getName());

    @Override
    protected Map<String, Object> doGetBody(
        final HttpServletRequest req,
        final HttpServletResponse resp)
        throws Exception {

        final String capFriendlyContractUuid = (String)  req.getParameter("cap_friendly_contract_uuid");
        if(StringUtil.isNullOrEmptyStr(capFriendlyContractUuid)) {
            return ServiceUtil.messageMap(new ErrorType("cap_friendly_contract_uuid_missing", "CapFriendlyContractUuid is a required field"));
        }

        final String capFriendlyPlayerGuid = (String)  req.getParameter("cap_friendly_player_guid");
        if(StringUtil.isNullOrEmptyStr(capFriendlyPlayerGuid)) {
            return ServiceUtil.messageMap(new ErrorType("cap_friendly_player_guid_missing", "CapFriendlyContractUuid is a required field"));
        }
        final Double ch = (String)  req.getParameter("ch");
        final String expiry_status = (String)  req.getParameter("expiry_status");
        final String length_str = (String)  req.getParameter("length_str");
        final Date signing_date = (String)  req.getParameter("signing_date");
        final String signing_team = (String)  req.getParameter("signing_team");
        final String type = (String)  req.getParameter("type");
        final Double value = (String)  req.getParameter("value");

        try (final Connection conn = getNoAutoCommitConnection()) {
            final ExecutionResult<CapFriendlyContract> er = CapFriendlyContractDL.update(conn
                , capFriendlyContractUuid
                , capFriendlyPlayerGuid
                , ch
                , expiry_status
                , length_str
                , signing_date
                , signing_team
                , type
                , value);
            if(er.isError()) {
                conn.rollback();
                return ServiceUtil.messageMap(er.errorMsg());
            }
            conn.commit();

            return ServiceUtil.successMap("cap_friendly_contract", er.entity().toMap());
        }
    }

}

