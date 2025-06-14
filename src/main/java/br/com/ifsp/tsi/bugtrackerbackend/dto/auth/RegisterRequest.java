package br.com.ifsp.tsi.bugtrackerbackend.dto.auth;

import org.springframework.web.multipart.MultipartFile;

public record RegisterRequest(
    String name,
    String email,
    String password,
    MultipartFile profilePicture
) {
}
