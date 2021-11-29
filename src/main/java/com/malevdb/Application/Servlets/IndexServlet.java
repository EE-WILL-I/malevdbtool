package com.malevdb.Application.Servlets;

import com.malevdb.Application.SessionManagement.SessionManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexServlet {
    @GetMapping({"/", "/index"})
    public String index(Model model, HttpServletRequest request) {
        String userName = (String)request.getSession().getAttribute("user");
        if(userName == null || userName.isEmpty())
            request.getSession().setAttribute("user", "User Name");
        model.addAttribute("user", userName);
        return "views/index";
    }
}
