package br.com.ifsp.tsi.bugtrackerbackend.dto;

import org.springframework.web.multipart.MultipartFile;

public record UpdateUserDTO(
        String name,
        String email,
        String password,
        MultipartFile profilePicture
) {
}