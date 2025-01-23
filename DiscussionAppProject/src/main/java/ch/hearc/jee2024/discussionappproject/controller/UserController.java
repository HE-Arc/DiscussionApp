package ch.hearc.jee2024.discussionappproject.controller;

import ch.hearc.jee2024.discussionappproject.model.Discussion;
import ch.hearc.jee2024.discussionappproject.model.User;
import ch.hearc.jee2024.discussionappproject.service.DiscussionService;
import ch.hearc.jee2024.discussionappproject.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final DiscussionService discussionService;

    private final UserService userService;

    public UserController(UserService userService, DiscussionService discussionService) {
        this.userService = userService;
        this.discussionService = discussionService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        if (user.getUsername() == null || user.getPassword() == null || user.getRole() == null) {
            throw new IllegalArgumentException("Tous les champs sont obligatoires");
        }
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{userId}/discussions")
    public ResponseEntity<List<Discussion>> getDiscussionsByUser(@PathVariable Long userId) {
        List<Discussion> discussions = discussionService.getDiscussionsByUser(userId);
        return ResponseEntity.ok(discussions);
    }
}
