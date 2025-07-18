package br.com.ifsp.tsi.bugtrackerbackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record UserDto(
        Long userId,
        String name,
        String email,
        @JsonIgnore
        String username,
        @JsonIgnore String password,
        String profilePicturePath,
        @JsonIgnore
        Collection<? extends GrantedAuthority> authorities,
        List<String> roles
) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
}