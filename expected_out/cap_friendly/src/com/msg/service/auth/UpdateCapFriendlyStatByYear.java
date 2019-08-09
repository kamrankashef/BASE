package com.msg.service.auth;

import com.msg.model.CapFriendlyStatByYear;
import com.msg.datalayer.CapFriendlyStatByYearDL;
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
@WebServlet("/api/update_cap_friendly_stat_by_year")
final public class UpdateCapFriendlyStatByYear extends AuthenticatedServlet { 

    private static final Logger LOGGER = Logger.getLogger(UpdateCapFriendlyStatByYear.class.getName());

    @Override
    protected Map<String, Object> doGetBody(
        final HttpServletRequest req,
        final HttpServletResponse resp)
        throws Exception {

        final String capFriendlyStatByYearUuid = (String)  req.getParameter("cap_friendly_stat_by_year_uuid");
        if(StringUtil.isNullOrEmptyStr(capFriendlyStatByYearUuid)) {
            return ServiceUtil.messageMap(new ErrorType("cap_friendly_stat_by_year_uuid_missing", "CapFriendlyStatByYearUuid is a required field"));
        }

        final String capFriendlyPlayerGuid = (String)  req.getParameter("cap_friendly_player_guid");
        if(StringUtil.isNullOrEmptyStr(capFriendlyPlayerGuid)) {
            return ServiceUtil.messageMap(new ErrorType("cap_friendly_player_guid_missing", "CapFriendlyStatByYearUuid is a required field"));
        }
        final Double a = (String)  req.getParameter("a");
        final Double g = (String)  req.getParameter("g");
        final Double gp = (String)  req.getParameter("gp");
        final String league = (String)  req.getParameter("league");
        final Double pim = (String)  req.getParameter("pim");
        final String playoffs = (String)  req.getParameter("playoffs");
        final Double playoffs_a = (String)  req.getParameter("playoffs_a");
        final Double playoffs_g = (String)  req.getParameter("playoffs_g");
        final Double playoffs_gp = (String)  req.getParameter("playoffs_gp");
        final Double playoffs_pim = (String)  req.getParameter("playoffs_pim");
        final Double playoffs_tp = (String)  req.getParameter("playoffs_tp");
        final String season = (String)  req.getParameter("season");
        final String team = (String)  req.getParameter("team");
        final Double tp = (String)  req.getParameter("tp");

        try (final Connection conn = getNoAutoCommitConnection()) {
            final ExecutionResult<CapFriendlyStatByYear> er = CapFriendlyStatByYearDL.update(conn
                , capFriendlyStatByYearUuid
                , capFriendlyPlayerGuid
                , a
                , g
                , gp
                , league
                , pim
                , playoffs
                , playoffs_a
                , playoffs_g
                , playoffs_gp
                , playoffs_pim
                , playoffs_tp
                , season
                , team
                , tp);
            if(er.isError()) {
                conn.rollback();
                return ServiceUtil.messageMap(er.errorMsg());
            }
            conn.commit();

            return ServiceUtil.successMap("cap_friendly_stat_by_year", er.entity().toMap());
        }
    }

}

