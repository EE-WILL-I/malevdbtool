package com.malevdb.Utils;

import com.malevdb.Application.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class PropertyReader {
    private static FileInputStream fileInputStream;
    private static Properties PROPERTIES;
    private static final String PROPERTIES_PATH = FileResourcesUtils.RESOURCE_PATH + "properties/";
    private static final String FILE_POSTFIX = ".properties";

    public static String getProperty(PropertyType property, String key) {
        readProperty(property.name().toLowerCase(Locale.ROOT));
        return PROPERTIES.getProperty(key);
    }

    private static void readProperty(String property) {
        try {
            fileInputStream = new FileInputStream(PROPERTIES_PATH + property + FILE_POSTFIX);
            PROPERTIES = new Properties();
            PROPERTIES.load(fileInputStream);
            Logger.Log(PropertyReader.class, "Property loaded: " + property);
        } catch (IOException e) {
            Logger.Log(PropertyReader.class, "Cannot read property: " + property);
            e.printStackTrace();
        } finally {
            if (fileInputStream != null)
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
