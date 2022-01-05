package com.malevdb.Application.Servlets;

import com.malevdb.Localization.UTF8Control;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.ResourceBundle;

public class ServletUtils {
    public static void showPopup(Model model, String message, String type) {
        model.addAttribute("show_popup", type);
        model.addAttribute("popup_message", message);
    }

    public static void showPopup(RedirectAttributes attributes, String message, String type) {
        attributes.addFlashAttribute("show_popup", type);
        attributes.addFlashAttribute("popup_message", message);
    }
}
