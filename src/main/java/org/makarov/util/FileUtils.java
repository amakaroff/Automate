package org.makarov.util;

import org.apache.commons.io.IOUtils;
import org.makarov.automate.exception.AutomateException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    public static String readFile(String fileName) {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream stream = loader.getResourceAsStream(fileName);
            if (stream == null) {
                File file = new File(fileName);
                if (!file.exists()) {
                    throw new AutomateException("File: " + fileName + " is not found in resources!");
                } else {
                    return org.apache.commons.io.FileUtils.readFileToString(file);
                }
            }

            return IOUtils.toString(stream);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
