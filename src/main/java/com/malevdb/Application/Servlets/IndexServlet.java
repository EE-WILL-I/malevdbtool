package com.malevdb.Application.Servlets;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexServlet {
    //@GetMapping("/")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String get(Model model) {
        return "index";
    }
}
