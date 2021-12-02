package com.malevdb.Application.SessionManagement;

import com.malevdb.Application.Logging.Logger;
import com.malevdb.Database.SQLExecutor;
import com.malevdb.Localization.LocalizationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SessionManager {
    private static final SQLExecutor sqlExecutor = SQLExecutor.getInstance();

    public static boolean checkSession(HttpServletRequest request) {
        Logger.log(SessionManager.class, "Checking user session", 4);
        if(request.getSession().getAttribute("authorized") != null) {
            LocalizationManager.setUserLocale(request.getSession());
            return true;
        }
        String ip = request.getRemoteAddr();
        if(ip == null || ip.isEmpty())
            ip = request.getHeader("X-FORWARDED-FOR");
        ResultSet resultSet = sqlExecutor.executeSelect(sqlExecutor.loadSQLResource("get_user_session.sql"),
                "*", ip, request.getSession().getId());
        try {
            if(resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                request.getSession().setAttribute("user_id", userId);
                resultSet = sqlExecutor.executeSelect(sqlExecutor.loadSQLResource("select_any.sql"),
                        "*", "users");
                if(!resultSet.next())
                    return false;
                request.getSession().setAttribute("user_login", resultSet.getString("login"));
                Logger.log(SessionManager.class, "Session found", 4);
                return true;
            } else {
                Logger.log(SessionManager.class, "Session auth failed", 4);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static UserSession registerSession(HttpServletRequest request) {
        String id = request.getSession().getId();
        String userId = (String) request.getSession().getAttribute("user_id");
        String ip = request.getRemoteAddr();
        if(ip == null || ip.isEmpty())
            ip = request.getHeader("X-FORWARDED-FOR");
        UserSession usrSess = new UserSession(id, userId);
        request.getSession().setAttribute("user_session", usrSess);
        try {
        deregisterSession(request);
            sqlExecutor.executeInsert(sqlExecutor.loadSQLResource("insert_active_user_sessions.sql"),
                    "active_user_sessions", id, userId, null, ip);
        } catch (SQLException e) {
            Logger.log(SessionManager.class, "Can't register session", 2);
        }
        return usrSess;
    }

    public static void deregisterSession(HttpServletRequest request) throws SQLException {
        String ip = request.getRemoteAddr();
        if (ip == null || ip.isEmpty())
            ip = request.getHeader("X-FORWARDED-FOR");
        sqlExecutor.executeUpdate(sqlExecutor.loadSQLResource("delete_any.sql"),
                "active_user_sessions", "user_ip = '" + ip + "' or id", request.getSession().getId());
    }
}
