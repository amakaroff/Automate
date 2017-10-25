package org.makarov.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    public static String readFile(String fileName) {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream stream = loader.getResourceAsStream(fileName);
            return IOUtils.toString(stream);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
