package br.edu.ifrs.pitanga.core.app.http;

import org.springframework.web.bind.annotation.RestController;

import br.edu.ifrs.pitanga.core.app.http.dto.CreateUserRequest;
import br.edu.ifrs.pitanga.core.app.http.dto.UserResponse;
import br.edu.ifrs.pitanga.core.domain.school.User;
import br.edu.ifrs.pitanga.core.domain.school.services.UserService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<UserResponse> register(@RequestBody CreateUserRequest request) {
        User user = userService.handle(request);
        URI uri = URI.create("/users/" + user.getId());
        UserResponse response = UserResponse.fromEntity(user);
        return ResponseEntity.created(uri).body(response);
    }
}
