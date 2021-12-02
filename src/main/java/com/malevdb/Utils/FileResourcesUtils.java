package com.malevdb.Utils;

import com.malevdb.Application.Logging.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileResourcesUtils {

    public static String RESOURCE_PATH;
    private static ClassLoader classLoader;
    private static StringBuilder stringBuilder;

    public static String getFileDataAsString(String filePath) throws IOException, IllegalArgumentException {
        Logger.log(FileResourcesUtils.class, "Loading resource at: " + filePath, 4);
        if (filePath.isEmpty())
            throw new IllegalArgumentException("Path is empty");

        Path path = Paths.get(RESOURCE_PATH + filePath);
        stringBuilder = new StringBuilder();
        Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8);
        lines.forEach(line -> stringBuilder.append(line));
        Logger.log(FileResourcesUtils.class, "Resource loaded", 4);
        return stringBuilder.toString();
    }

    public static FileInputStream getFileAsStream(String filePath) throws IOException {
            return new FileInputStream(filePath);
    }

    public static FileInputStream getFileAsStream(File file) throws IOException {
        return new FileInputStream(file);
    }

    public static File transferMultipartFile(MultipartFile multipartFile, String outPath) throws IOException {
        File file = new File(outPath);
        if(file.exists() && !file.setWritable(true))
            throw new IOException("File is locked");
        if(file.createNewFile()) {
            Logger.log(FileResourcesUtils.class, "Created file: " + file.getAbsolutePath(), 1);
            multipartFile.transferTo(file);
            //Logger.log(FileResourcesUtils.class, "Transfered file: " + file.getAbsolutePath(), 3);
            return file;
        }
        else throw new IOException("Cannot transfer file");
    }

    public static ClassLoader getClassLoader() {
        if (classLoader == null)
            classLoader = FileResourcesUtils.class.getClassLoader();
        return classLoader; }
}
