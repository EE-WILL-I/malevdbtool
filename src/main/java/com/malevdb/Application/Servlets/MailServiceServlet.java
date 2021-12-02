package com.malevdb.Application.Servlets;

import com.malevdb.MailService.MailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MailServiceServlet {
    @GetMapping("/mail")
    public String doGet() {
        return "views/mailService";
    }

    @PostMapping("/mail/send")
    public String doPost(HttpServletRequest request, RedirectAttributes attributes) {
        String[] recipients = request.getParameter("recipients").split(",");
        String message = request.getParameter("message");
        String subject = request.getParameter("subject");
        try {
            MailSender.getInstance().sendMessage(message, subject, recipients);
        } catch (MessagingException e) {
            ServletUtils.showPopup(attributes, e.getMessage(), "error");
        }
        return "redirect:/mail";
    }
}
