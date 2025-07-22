package br.com.ifsp.tsi.bugtrackerbackend.controller;

import br.com.ifsp.tsi.bugtrackerbackend.dto.ProfilePictureDto;
import br.com.ifsp.tsi.bugtrackerbackend.dto.UpdateUserDTO;
import br.com.ifsp.tsi.bugtrackerbackend.dto.UserDto;
import br.com.ifsp.tsi.bugtrackerbackend.dto.UserPageDto;
import br.com.ifsp.tsi.bugtrackerbackend.dto.ticket.TicketResponseDTO;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Message;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Rating;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Ticket;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.User;
import br.com.ifsp.tsi.bugtrackerbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bugtracker/users")
@PreAuthorize("hasRole('USER') or hasRole('TECHNICIAN') or hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserPageDto> list(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(
                userService.list(page, pageSize)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(UserDto.fromUser(user));
    }

    @GetMapping("/picture")
    public ResponseEntity<?> getUserProfilePicture() throws IOException {
        var userSignedIn = userService.getUserSignedIn();

        ProfilePictureDto profilePictureDTO = userService.getUserProfilePicture(userSignedIn);

        return ResponseEntity.ok()
                .header("Content-Type", profilePictureDTO.contentType())
                .header("Content-Length", profilePictureDTO.length())
                .body(profilePictureDTO.profilePicture());
    }

    @GetMapping("/ratings")
    public ResponseEntity<List<Rating>> getUserRatings() {
        var userSignedIn = userService.getUserSignedIn();

        return ResponseEntity.ok(
                userService.getUserRatings(userSignedIn)
        );
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<Ticket>> getUserTickets() {
        var userSignedIn = userService.getUserSignedIn();

        return ResponseEntity.ok(
                userService.getUserTickets(userSignedIn)
        );
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getUserMessages() {
        var userSignedIn = userService.getUserSignedIn();

        return ResponseEntity.ok(
                userService.getUserMessages(userSignedIn)
        );
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> updateUser(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture
    ) {

        return ResponseEntity.ok(
                userService.updateUser(
                        new UpdateUserDTO(name, password, newPassword, profilePicture)
                )
        );
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        this.userService.deleteById(id);
    }
}