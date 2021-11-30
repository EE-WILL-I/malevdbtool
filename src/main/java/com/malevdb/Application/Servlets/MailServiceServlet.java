package com.malevdb.Application.Servlets;

import com.malevdb.MailService.MailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MailServiceServlet {
    @GetMapping("/mail")
    public String doGet() {
        return "views/mailService";
    }

    @PostMapping("/mail/send")
    public String doPost(HttpServletRequest request) {
        String[] recipients = request.getParameter("recipients").split(",");
        String message = request.getParameter("message");
        String subject = request.getParameter("subject");
        MailSender.getInstance().sendMessage(message, subject, recipients);
        return "redirect:/mail";
    }
}
