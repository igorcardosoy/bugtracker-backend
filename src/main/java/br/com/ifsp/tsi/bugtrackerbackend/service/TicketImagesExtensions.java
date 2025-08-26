package br.com.ifsp.tsi.bugtrackerbackend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketImagesExtensions {
    private static final String UPLOAD_DIR = "uploads/ticket-images/";

    public static List<String> saveTicketImages(List<MultipartFile> images) {
        List<String> imagePaths = new ArrayList<>();

        for (MultipartFile image : images) {
            if (image != null && !image.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                File dest = new File(UPLOAD_DIR + fileName);

                try {
                    dest.getParentFile().mkdirs();
                    image.transferTo(dest);
                    imagePaths.add(fileName);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save ticket image", e);
                }
            }
        }

        return imagePaths;
    }

    public static void removeTicketImages(List<String> imagePaths) {
        if (imagePaths != null) {
            for (String imagePath : imagePaths) {
                if (imagePath != null && !imagePath.isEmpty()) {
                    File file = new File(UPLOAD_DIR + imagePath);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        }
    }
}
