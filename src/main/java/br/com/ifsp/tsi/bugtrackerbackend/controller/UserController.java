package br.com.ifsp.tsi.bugtrackerbackend.controller;

import br.com.ifsp.tsi.bugtrackerbackend.dto.ProfilePictureDto;
import br.com.ifsp.tsi.bugtrackerbackend.dto.UserDto;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Message;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Rating;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.Ticket;
import br.com.ifsp.tsi.bugtrackerbackend.model.entity.User;
import br.com.ifsp.tsi.bugtrackerbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/bugtracker/users")
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<UserDto> getUsers() {
        return ResponseEntity.ok(
                userService.getUserSignedIn()
        );
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

    @PutMapping()
    public ResponseEntity<User> updateUser(@RequestBody UserDto updateUserRequest) {
        return ResponseEntity.ok(
                userService.updateUser(updateUserRequest)
        );
    }
}