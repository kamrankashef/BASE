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
@WebServlet("/api/get_cap_friendly_stat_by_year")
final public class GetCapFriendlyStatByYear extends AuthenticatedServlet { 

    private static final Logger LOGGER = Logger.getLogger(GetCapFriendlyStatByYear.class.getName());

    @Override
    protected Map<String, Object> doGetBody(
        final HttpServletRequest req,
        final HttpServletResponse resp)
        throws Exception {

        final String capFriendlyStatByYearUuid = (String)  req.getParameter("cap_friendly_stat_by_year_uuid");
        if(StringUtil.isNullOrEmptyStr(capFriendlyStatByYearUuid)) {
            return ServiceUtil.messageMap(new ErrorType("cap_friendly_stat_by_year_uuid_missing", "CapFriendlyStatByYearUuid is a required field"));
        }


        try (final Connection conn = getNoAutoCommitConnection()) {
            final ExecutionResult<CapFriendlyStatByYear> er = CapFriendlyStatByYearDL.get(conn
                , capFriendlyStatByYearUuid);
            if(er.isError()) {
                return ServiceUtil.messageMap(er.errorMsg());
            }

            return ServiceUtil.successMap("cap_friendly_stat_by_year", er.entity().toMap());
        }
    }

}

