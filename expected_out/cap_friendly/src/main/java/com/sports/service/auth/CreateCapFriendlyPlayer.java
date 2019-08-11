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
@WebServlet("/api/create_cap_friendly_player")
final public class CreateCapFriendlyPlayer extends AuthenticatedServlet { 

    private static final Logger LOGGER = Logger.getLogger(CreateCapFriendlyPlayer.class.getName());

    @Override
    protected Map<String, Object> doGetBody(
        final HttpServletRequest req,
        final HttpServletResponse resp)
        throws Exception {

        final Integer age = (String)  req.getParameter("age");
        final String birth_date_str = (String)  req.getParameter("birth_date_str");
        final Integer draft_rount = (String)  req.getParameter("draft_rount");
        final Integer draft_year = (String)  req.getParameter("draft_year");
        final String drafted_by = (String)  req.getParameter("drafted_by");
        final Integer drafted_overall = (String)  req.getParameter("drafted_overall");
        final Integer elc_signing_age = (String)  req.getParameter("elc_signing_age");
        final Long elite_prospect_id = (String)  req.getParameter("elite_prospect_id");
        final String full_name = (String)  req.getParameter("full_name");
        final String height_str = (String)  req.getParameter("height_str");
        final Integer number = (String)  req.getParameter("number");
        final String shoots = (String)  req.getParameter("shoots");
        final String team_name = (String)  req.getParameter("team_name");
        final Integer waivers_signing_age = (String)  req.getParameter("waivers_signing_age");

        try (final Connection conn = getNoAutoCommitConnection()) {
            final ExecutionResult<CapFriendlyPlayer> er = CapFriendlyPlayerDL.create(conn
                , age
                , birth_date_str
                , draft_rount
                , draft_year
                , drafted_by
                , drafted_overall
                , elc_signing_age
                , elite_prospect_id
                , full_name
                , height_str
                , number
                , shoots
                , team_name
                , waivers_signing_age);
            if(er.isError()) {
                conn.rollback();
                return ServiceUtil.messageMap(er.errorMsg());
            }
            conn.commit();

            return ServiceUtil.successMap("cap_friendly_player", er.entity().toMap());
        }
    }

}

