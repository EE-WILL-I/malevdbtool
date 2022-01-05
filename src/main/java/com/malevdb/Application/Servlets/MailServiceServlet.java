package com.malevdb.Application.Servlets;

import Utils.Logging.Logger;
import com.malevdb.MailService.MailSender;
import com.malevdb.MailService.MailingAccountBean;
import com.malevdb.MailService.MessageBean;
import com.malevdb.Utils.JSONReader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MailServiceServlet {
    private final String MNSErrorMessage = "Mail notification service is unavailable. Check connection properties or try restart";
    @GetMapping(value = "/mail")
    public String doGet(RedirectAttributes attributes) {
        try {
            ResponseEntity<String> responseEntity =
                    new RestTemplate().getForEntity("http://localhost:8000/status", String.class);
            if (!JSONReader.getArgumentValue("status", responseEntity.getBody()).equals("OK")) {
                ServletUtils.showPopup(attributes, MNSErrorMessage, "error");
                return "redirect:/";
            }
        } catch (Exception e) {
            Logger.log(this, e.getMessage(), 2);
            ServletUtils.showPopup(attributes, MNSErrorMessage + "\n" + e.getMessage(), "error");
            return "redirect:/";
        }
        return "views/mailService";
    }

    @PostMapping("/mail/send")
    public String doPost(HttpServletRequest request, Model model, RedirectAttributes attributes) {
        String[] recipients = request.getParameter("recipients").split(",");
        String message = request.getParameter("message");
        String subject = request.getParameter("subject");
        if(message.isEmpty() || subject.isEmpty() || recipients.length == 0) {
            ServletUtils.showPopup(model, "Не все поля заполнены или введены неверные значения.", "warning");
            return "views/mailService";
        }
        try {
            ResponseEntity<String> responseEntity =
                    new RestTemplate().postForEntity("http://localhost:8000/mail/send",
                            new MessageBean(new MailingAccountBean("11", "test@mail.ru"),
                                    subject, message, recipients), String.class);
            Logger.log(this, responseEntity.getBody(), 4);
            if(JSONReader.getArgumentValue("status", responseEntity.getBody()).equals("error"))
                ServletUtils.showPopup(attributes, JSONReader.getArgumentValue("message",
                        responseEntity.getBody()), "error");
        } catch (Exception e) {
            Logger.log(this, e.getMessage(), 2);
            ServletUtils.showPopup(attributes, e.getMessage(), "error");
        }
        return "redirect:/mail";
    }
}
