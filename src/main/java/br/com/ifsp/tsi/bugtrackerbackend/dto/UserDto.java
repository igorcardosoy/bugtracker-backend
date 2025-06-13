package br.com.ifsp.tsi.bugtrackerbackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record UserDto(
        Long id,
        String name,
        String email,
        @JsonIgnore String password,
        String profilePicture,
        Collection<? extends GrantedAuthority> authorities
) {

}
