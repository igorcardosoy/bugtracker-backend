package br.com.ifsp.tsi.bugtrackerbackend.service;

import br.com.ifsp.tsi.bugtrackerbackend.exception.ProfilePictureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Log4j2
public class ProfilePictureExtensions {
    public static String saveProfilePicture(MultipartFile file) {
        try {

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path uploadDir = Paths.get("uploads/profile-pictures");

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path destination = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            log.info("Imagem de perfil salva: {}", fileName);
            return fileName;

        } catch (IOException e) {
            log.error("Erro ao salvar imagem de perfil", e);
            throw new ProfilePictureException("Não foi possível salvar a imagem de perfil: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static void removeOldProfilePicture(String oldFileName) {
        if (oldFileName == null || oldFileName.isEmpty()) {
            return;
        }

        try {
            Path filePath = Paths.get("uploads/profile-pictures", oldFileName);
            Files.deleteIfExists(filePath);
            log.info("Imagem de perfil removida: {}", oldFileName);
        } catch (IOException e) {
            log.error("Erro ao remover imagem de perfil", e);
            throw new ProfilePictureException("Não foi possível remover a imagem de perfil: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}