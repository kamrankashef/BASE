package com.sports.service.auth;

import com.sports.model.CapFriendlyPlayer;
import com.sports.datalayer.CapFriendlyPlayerDL;
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
@WebServlet("/api/delete_cap_friendly_player")
final public class DeleteCapFriendlyPlayer extends AuthenticatedServlet { 

    private static final Logger LOGGER = Logger.getLogger(DeleteCapFriendlyPlayer.class.getName());

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
            final ExecutionResult<Void> er = CapFriendlyPlayerDL.delete(conn
                , capFriendlyPlayerUuid);
            if(er.isError()) {
                conn.rollback();
                return ServiceUtil.messageMap(er.errorMsg());
            }
            conn.commit();

            return ServiceUtil.successMap();
        }
    }

}

