package com.malevdb.Application.Servlets;

import com.malevdb.MailService.MailSender;
import com.malevdb.MailService.MailingAccountBean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MailServiceServlet {
    @GetMapping(value = "/mail")
    public String doGet() {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("id", "77");
        //uriVariables.put("login", "user@super.nice");
        ResponseEntity<String> responseEntity =
                new RestTemplate().postForEntity("http://localhost:8000/account/provide",
                 new MailingAccountBean("15", "login@test.con"), String.class);
        System.out.println(responseEntity.getBody());
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
