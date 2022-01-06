package com.malevdb.Application.Servlets;

import Utils.Logging.Logger;
import com.malevdb.Localization.LocalizationManager;
import com.malevdb.MailService.MNSAuthenticator;
import com.malevdb.MailService.MessageBean;
import com.malevdb.Utils.JSONReader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MailServiceServlet {
    private final String MNSErrorMessage = "Mail notification service is unavailable. Check connection properties or try restart";
    private final String serviceURL = MNSAuthenticator.loadServiceRemoteURL();

    @GetMapping(value = "/mail")
    public String doGet(RedirectAttributes attributes) {
        try {
            ResponseEntity<String> responseEntity = new RestTemplate().exchange(serviceURL + "/status",
                    HttpMethod.GET, new HttpEntity<String>(MNSAuthenticator.getHeaders()), String.class);
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
        String[] recipients = request.getParameter("recipients").split("[,;\\s\\n]");
        String content = request.getParameter("message");
        String subject = request.getParameter("subject");
        if(content.isEmpty() || subject.isEmpty() || recipients.length == 0) {
            ServletUtils.showPopup(model, LocalizationManager.getString("MSS.empty_err"), "warning");
            return "views/mailService";
        }
        MessageBean message = new MessageBean(subject, content, recipients);
        Logger.log(this, "Trying to send message: " + message.toString(), 1);
        try {
            ResponseEntity<String> responseEntity = new RestTemplate().exchange(serviceURL + "/mail/send",
                    HttpMethod.POST, new HttpEntity<MessageBean>(message, MNSAuthenticator.getHeaders()), String.class);
            Logger.log(this, responseEntity.getBody(), 4);
            if(JSONReader.getArgumentValue("status", responseEntity.getBody()).equals("error")) {
                Logger.log(this, "Couldn't send message.", 2);
                ServletUtils.showPopup(attributes, JSONReader.getArgumentValue("message",
                        responseEntity.getBody()), "error");
            } else Logger.log(this, "Message sent successfully", 1);
        } catch (Exception e) {
            Logger.log(this, e.getMessage(), 2);
            ServletUtils.showPopup(attributes, e.getMessage(), "error");
        }
        ServletUtils.showPopup(attributes, "Sent successfully", "warning");
        return "redirect:/mail";
    }

    @PutMapping("/mail/updateCredentials/{user}/{pass}")
    public String doPutCreds(@PathVariable String user, @PathVariable String pass, RedirectAttributes attributes) {
        sendUpdateCredsRequest(user, pass, attributes);
        return "redirect:/mail";
    }

    @PutMapping("/mail/updateAccount/{login}/{pass}")
    public String doPutAccount(@PathVariable String login, @PathVariable String pass, RedirectAttributes attributes) {
        sendUpdateAccRequest(login, pass, attributes);
        return "redirect:/mail";
    }

    @PostMapping("/mail/updateCredentials")
    public String doPutCreds(HttpServletRequest request, RedirectAttributes attributes) {
        String user = request.getParameter("new_user");
        String pass = request.getParameter("new_pass");
        sendUpdateCredsRequest(user, pass, attributes);
        return "redirect:/mail";
    }

    @PostMapping("/mail/updateAccount")
    public String doPutAccount(HttpServletRequest request, RedirectAttributes attributes) {
        String login = request.getParameter("new_login");
        String pass = request.getParameter("new_pass");
        sendUpdateAccRequest(login, pass, attributes);
        return "redirect:/mail";
    }

    private void sendUpdateCredsRequest(String user, String pass, RedirectAttributes attributes) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("user", user);
        params.put("pass", pass);
        ResponseEntity<String> responseEntity =
                new RestTemplate().exchange(serviceURL + "/credentials/update/{user}/{pass}",
                        HttpMethod.PUT, new HttpEntity<String>(MNSAuthenticator.getHeaders()), String.class, params);
        if (!JSONReader.getArgumentValue("status", responseEntity.getBody()).equals("OK")) {
            ServletUtils.showPopup(attributes,
                    JSONReader.getArgumentValue("message", responseEntity.getBody()), "error");
        } else {
            ServletUtils.showPopup(attributes, "Updated", "warning");
            MNSAuthenticator.loadProvidedUserCredentials();
        }
    }

    private void sendUpdateAccRequest(String login, String pass, RedirectAttributes attributes) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("login", login);
        params.put("pass", pass);
        ResponseEntity<String> responseEntity =
                new RestTemplate().exchange(serviceURL + "/account/update/{login}/{pass}",
                        HttpMethod.PUT, new HttpEntity<String>(MNSAuthenticator.getHeaders()), String.class, params);
        if (!JSONReader.getArgumentValue("status", responseEntity.getBody()).equals("OK")) {
            ServletUtils.showPopup(attributes,
                    JSONReader.getArgumentValue("message", responseEntity.getBody()), "error");
        } else ServletUtils.showPopup(attributes, "Updated", "warning");
    }
}
