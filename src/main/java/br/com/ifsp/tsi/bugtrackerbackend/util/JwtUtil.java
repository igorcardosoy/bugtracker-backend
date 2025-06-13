package br.com.ifsp.tsi.bugtrackerbackend.util;


import br.com.ifsp.tsi.bugtrackerbackend.dto.UserDto;
import br.com.ifsp.tsi.bugtrackerbackend.dto.auth.JwtResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Log4j2
public class JwtUtil {

    @Value("${bugtracker.jwtSecret}")
    private String jwtSecretStr;
    private SecretKey jwtSecret;

    @PostConstruct
    public void init() {
        jwtSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretStr));
    }


    @Value("${bugtracker.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(UserDto userDto) {
        return Jwts.builder()
                .subject(userDto.email())
                .claim("id", userDto.id())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(jwtSecret)
                .compact();

    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(jwtSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public JwtResponse createJwtResponse(UserDto userDto) {
        String jwt = generateJwtToken(userDto);

        List<String> roles = userDto.authorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(
                jwt,
                userDto.id(),
                userDto.email(),
                roles
        );
    }

    public void validateJwtToken(String authToken) {
        Jwts.parser()
                .verifyWith(jwtSecret)
                .build()
                .parseSignedClaims(authToken);
    }

}
