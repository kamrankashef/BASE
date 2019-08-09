package com.msg.service.auth;

import com.msg.model.CapFriendlyPlayer;
import com.msg.datalayer.CapFriendlyPlayerDL;
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
@WebServlet("/api/get_cap_friendly_player")
final public class GetCapFriendlyPlayer extends AuthenticatedServlet { 

    private static final Logger LOGGER = Logger.getLogger(GetCapFriendlyPlayer.class.getName());

    @Override
    protected Map<String, Object> doGetBody(
        final HttpServletRequest req,
        final HttpServletResponse resp)
        throws Exception {

        final String capFriendlyPlayerUuid = (String)  req.getParameter("cap_friendly_player_uuid");
        if(StringUtil.isNullOrEmptyStr(capFriendlyPlayerUuid)) {
            return ServiceUtil.messageMap(new ErrorType("cap_friendly_player_uuid_missing", "CapFriendlyPlayerUuid is a required field"));
        }


        try (final Connection conn = getNoAutoCommitConnection()) {
            final ExecutionResult<CapFriendlyPlayer> er = CapFriendlyPlayerDL.get(conn
                , capFriendlyPlayerUuid);
            if(er.isError()) {
                return ServiceUtil.messageMap(er.errorMsg());
            }

            return ServiceUtil.successMap("cap_friendly_player", er.entity().toMap());
        }
    }

}

