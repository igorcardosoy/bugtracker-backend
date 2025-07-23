package br.com.ifsp.tsi.bugtrackerbackend.dto;

import org.springframework.web.multipart.MultipartFile;

public record UpdateUserDTO(
        String name,
        String password,
        String newPassword,
        MultipartFile profilePicture,
        String[] roles,
        String email
) {
}