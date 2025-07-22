package br.com.ifsp.tsi.bugtrackerbackend.dto;

import java.util.List;

public record UserPageDto(
        List<UserDto> users,
        long totalElements,
        int totalPages
) {
}
