package com.malevdb.Utils;

import com.malevdb.Application.Logging.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class PropertyReader {
    private static FileInputStream fileInputStream;
    private static Properties PROPERTIES;
    private static final Map<String, Properties> PROPERTIES_MAP = new HashMap<>();
    private static final String PROPERTIES_PATH = FileResourcesUtils.RESOURCE_PATH + "properties/";
    private static final String FILE_POSTFIX = ".properties";

    public static String getPropertyValue(PropertyType property, String key) {
        if (getProperties(property) == null) {
            Logger.log(PropertyReader.class, "Cannot load key: " + key, 2);
            return "";
        }
        Logger.log(PropertyReader.class, "Key loaded: " + key, 4);
        return PROPERTIES.getProperty(key);
    }

    public static Properties getProperties(PropertyType property) {
        String propertyName = property.name().toLowerCase(Locale.ROOT);
        if(PROPERTIES_MAP.containsKey(propertyName)){
            PROPERTIES = PROPERTIES_MAP.get(propertyName);
        }
        else {
            if(!readProperty(propertyName)) {
                return null;
            }
        }
        return PROPERTIES;
    }

    private static boolean readProperty(String property) {
        try {
            fileInputStream = FileResourcesUtils.getFileAsStream(PROPERTIES_PATH + property + FILE_POSTFIX);
            PROPERTIES = new Properties();
            PROPERTIES.load(fileInputStream);
            PROPERTIES_MAP.put(property, PROPERTIES);
            Logger.log(PropertyReader.class, "Property loaded: " + property, 4);
            return true;
        } catch (IOException e) {
            Logger.log(PropertyReader.class, "Cannot read property: " + property, 2);
            e.printStackTrace();
            return false;
        } finally {
            if (fileInputStream != null)
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static Properties loadServerProps() {
        try {
            fileInputStream = FileResourcesUtils.getFileAsStream(PROPERTIES_PATH + PropertyType.SERVER + FILE_POSTFIX);
            PROPERTIES = new Properties();
            PROPERTIES.load(fileInputStream);
            Logger.loggingLevel = Integer.parseInt(PROPERTIES.getProperty("app.loggingLevel"));
            return PROPERTIES;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
