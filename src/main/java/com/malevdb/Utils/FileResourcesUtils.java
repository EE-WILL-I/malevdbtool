package com.malevdb.Utils;

import com.malevdb.Application.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileResourcesUtils {

    public static final String RESOURCE_PATH = "C:\\Users\\Bogdan\\Documents\\Java\\JDBC\\resources\\";
    private static ClassLoader classLoader;
    private static StringBuilder stringBuilder;

    public static String getFileDataAsString(String filePath) throws IOException, IllegalArgumentException {
        if (filePath.isEmpty())
            throw new IllegalArgumentException("Path is empty");

        Path path = Paths.get(RESOURCE_PATH + filePath);

        if (classLoader == null)
            classLoader = FileResourcesUtils.class.getClassLoader();

        stringBuilder = new StringBuilder();


        Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8);

        lines.forEach(line -> stringBuilder.append(line));

        return stringBuilder.toString();
    }

    public static FileInputStream getFileAsStream(String filePath) throws IOException {
            return new FileInputStream(filePath);
    }
}
