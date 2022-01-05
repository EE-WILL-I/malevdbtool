package com.malevdb.Utils;

import Utils.FileResourcesUtils;
import Utils.Logging.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class ExtendedResourcesUtils extends FileResourcesUtils {
    public static File transferMultipartFile(MultipartFile multipartFile, String outPath) throws IOException {
        File file = new File(outPath);
        if(file.exists() && !file.setWritable(true))
            throw new IOException("File is locked");
        if(file.createNewFile()) {
            Logger.log(FileResourcesUtils.class, "Created file: " + file.getAbsolutePath(), 1);
            multipartFile.transferTo(file);
            //Logger.log(Utils.FileResourcesUtils.class, "Transfered file: " + file.getAbsolutePath(), 3);
            return file;
        }
        else throw new IOException("Cannot transfer file");
    }
}
