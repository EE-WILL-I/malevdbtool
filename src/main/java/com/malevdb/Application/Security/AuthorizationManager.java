package com.malevdb.Application.Security;

import Utils.Properties.PropertyReader;
import Utils.Properties.PropertyType;
import org.springframework.http.HttpHeaders;

import java.util.Base64;
import java.util.Locale;

public class AuthorizationManager {
    private static String encodedUser;

    public static boolean loadConfiguredServiceUserCredentials() {
        String user = PropertyReader.getPropertyValue(PropertyType.SERVER, "srevauth.user");
        String pass = PropertyReader.getPropertyValue(PropertyType.SERVER, "servauth.pass");
        if(user.isEmpty() || pass.isEmpty())
            return false;
        encodedUser = encodeCredentials(user, pass);
        return true;
    }

    public static void loadDefaultServiceUserCredentials() {
        encodedUser = encodeCredentials("wsainternaluser", "WSAINTERNALUER01");
    }

    public static String encodeCredentials(String user, String pass) {
        return  Base64.getEncoder().encodeToString((user + ":" + pass).getBytes());
    }

    public static boolean authorize(String credentials) {
        return encodedUser.equals(credentials);
    }

    public static HttpHeaders getHeaders() {
        return new HttpHeaders() {{
            String authHeader = "Basic " + encodedUser;
            set("Authorization", authHeader);
        }};
    }
}
