package com.malevdb.Application.Servlets;
import com.malevdb.Application.Logging.Logger;
import com.malevdb.Application.SessionManagement.SessionManager;
import com.malevdb.Application.SessionManagement.UserSession;
import com.malevdb.Database.SQLExecutor;

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
    public String showForm(Model model, @ModelAttribute("failed") String failed) {
        if(failed.equals("true")) {
            model.addAttribute("displayError", "true");
        }
        return "views/authorization";
    }

    @PostMapping("/login")
    public String tryLogIn(HttpServletRequest request, RedirectAttributes attributes, @RequestParam(value = "login", defaultValue = "") String login, @RequestParam(value = "passwd", defaultValue = "") String passwd) {
        SQLExecutor executor = SQLExecutor.getInstance();
        Logger.log(this, "Logging as: " + login +" "+ passwd);
        ResultSet resultSet = executor.executeSelect(executor.loadSQLResource("get_user.sql"), "id", login, passwd);
        try {
            if(resultSet.next()) {
                String userId = String.valueOf(resultSet.getInt("id"));
                request.getSession().setAttribute("user_name", login);
                request.getSession().setAttribute("user_id", userId);
                SessionManager.registerSession(request);
                Logger.log(this, "Login successful", 3);
                return "redirect:/";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        attributes.addFlashAttribute("failed", "true");
        Logger.log(this, "Login failed", 3);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        SessionManager.deregisterSession(request);
        return "redirect:/login";
    }
}
