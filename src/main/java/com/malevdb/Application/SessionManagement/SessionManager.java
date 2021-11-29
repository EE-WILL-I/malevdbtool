package com.malevdb.Application.SessionManagement;

import com.malevdb.Application.Logging.Logger;
import com.malevdb.Database.SQLExecutor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SessionManager {
    private static final SQLExecutor sqlExecutor = SQLExecutor.getInstance();

    public static boolean checkSession(HttpServletRequest request) {
        Logger.log(SessionManager.class, "Checking user session", 4);
        String ip = request.getRemoteAddr();
        //sqlExecutor.executeCall(sqlExecutor.loadSQLResource("clean_timeout_sessions.sql"));
        ResultSet resultSet = sqlExecutor.executeSelect(sqlExecutor.loadSQLResource("get_user_session.sql"),
                "*", "user_ip", ip);
        try {
            if( resultSet.next()) {
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
        UserSession usrSess = new UserSession(id, userId);
        request.getSession().setAttribute("user_session", usrSess);
        deregisterSession(request);
        sqlExecutor.executeCall(sqlExecutor.loadSQLResource("clean_timeout_sessions.sql"));
        sqlExecutor.executeInsert(sqlExecutor.loadSQLResource("insert_active_user_sessions.sql"),
                "active_user_sessions", id, userId, ip);
        return usrSess;
    }

    public static void deregisterSession(HttpServletRequest request) {
            sqlExecutor.executeUpdate(sqlExecutor.loadSQLResource("delete_any.sql"),
                    "active_user_sessions", "user_ip", request.getRemoteAddr());
    }
}
