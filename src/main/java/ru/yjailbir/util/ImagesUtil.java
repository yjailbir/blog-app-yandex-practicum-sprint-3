package ru.yjailbir.util;

import org.springframework.core.io.Resource;

import java.io.*;

public class ImagesUtil {
    public static String saveImage(Resource image, String folder) {
        String resPath = image.hashCode() + image.getFilename();

        try (
                InputStream in = image.getInputStream();
                OutputStream out = new FileOutputStream(folder + File.separator + resPath)
        ) {
            out.write(in.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return resPath;
    }
}
