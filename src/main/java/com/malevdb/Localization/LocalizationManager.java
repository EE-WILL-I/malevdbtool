package com.malevdb.Localization;

import Utils.FileResourcesUtils;
import Utils.Logging.Logger;
import Utils.Properties.PropertyReader;
import Utils.Properties.PropertyType;
import com.malevdb.Application.Servlets.ServletUtils;

import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationManager {
    private static final String BUNDLE_BASE_NAME = PropertyReader.getPropertyValue(PropertyType.APPLICATION,
            "bundle.basename");
    private static ResourceBundle bundle;
    private static final UTF8Control utf8Control = new UTF8Control();
    private static String savedLocaleParam = "";

    public static void setUserLocale(HttpSession session) {
        try {
            String localeParam = (String) session.getAttribute("locale");
            if(localeParam.equals(savedLocaleParam) && bundle != null)
                return;
            savedLocaleParam = localeParam;
            String language = localeParam.split("\\.")[0];
            String country = localeParam.split("\\.")[1];
            bundle = ResourceBundle.getBundle("resourceBundles." + BUNDLE_BASE_NAME, new Locale(language, country),
                    FileResourcesUtils.getClassLoader());
        } catch (NullPointerException e) {
            Logger.log(ServletUtils.class, "User's locale not found", 3);
            bundle = ResourceBundle.getBundle("resourceBundles." + BUNDLE_BASE_NAME,
                    new Locale("ru", "RU"), FileResourcesUtils.getClassLoader(), utf8Control);
        }
    }

    public static String getString(String key) {
        if(bundle == null)
            setUserLocale(null);
        return bundle.getString(key);
    }
}
