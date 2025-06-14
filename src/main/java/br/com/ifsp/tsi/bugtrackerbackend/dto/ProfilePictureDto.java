package br.com.ifsp.tsi.bugtrackerbackend.dto;

public record ProfilePictureDto(
        byte[] profilePicture,
        String contentType,
        String length
) { }