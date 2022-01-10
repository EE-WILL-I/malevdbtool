package com.malevdb.Application.Servlets;
import Utils.Logging.Logger;
import com.malevdb.Application.SessionManagement.SessionManager;
import com.malevdb.Application.SessionManagement.UserSession;

import com.malevdb.Localization.LocalizationManager;
import com.malevtool.Proccessing.SQLExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;

@Controller
public class LoginServlet {
    @GetMapping("/login")
    public String showForm() {
        return "views/authorization";
    }

    @PostMapping("/login")
    public String tryLogIn(HttpServletRequest request, RedirectAttributes attributes, @RequestParam(value = "login", defaultValue = "") String login, @RequestParam(value = "passwd", defaultValue = "") String passwd) {
        SQLExecutor executor = SQLExecutor.getInstance();
        Logger.log(this, "Logging as: " + login +" "+ passwd);
        ResultSet resultSet = executor.executeSelect(executor.loadSQLResource("get_user.sql"),
                "id, params", login, passwd);
        try {
            if(resultSet.next()) {
                String userId = String.valueOf(resultSet.getInt("id"));
                request.getSession().setAttribute("authorized", "true");
                request.getSession().setAttribute("user_login", login);
                request.getSession().setAttribute("user_id", userId);
                request.getSession().setAttribute("locale", resultSet.getString("params").split(";")[0]);
                LocalizationManager.setUserLocale(request.getSession());

                SessionManager.registerSession(request);
                Logger.log(this, "Login successful", 3);
                return "redirect:/";
            }
        } catch (Exception e) {
            Logger.log(this, e.getMessage(), 2);
            ServletUtils.showPopup(attributes, e.getLocalizedMessage(), "error");
        }
        attributes.addFlashAttribute("failed", "true");
        Logger.log(this, "Login failed", 3);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws SQLException {
        SessionManager.deregisterSession(request);
        request.getSession().removeAttribute("authorized");
        return "redirect:/login";
    }
}
