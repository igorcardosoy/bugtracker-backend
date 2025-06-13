package br.com.ifsp.tsi.bugtrackerbackend.service;

import br.com.ifsp.tsi.bugtrackerbackend.dto.UserDto;
import br.com.ifsp.tsi.bugtrackerbackend.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + email));

        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getProfilePicture(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                        .toList()
        );
    }
}
