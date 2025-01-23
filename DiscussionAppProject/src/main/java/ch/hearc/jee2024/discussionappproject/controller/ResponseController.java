package ch.hearc.jee2024.discussionappproject.controller;

import ch.hearc.jee2024.discussionappproject.model.Discussion;
import ch.hearc.jee2024.discussionappproject.model.Response;
import ch.hearc.jee2024.discussionappproject.model.Role;
import ch.hearc.jee2024.discussionappproject.model.User;
import ch.hearc.jee2024.discussionappproject.service.DiscussionService;
import ch.hearc.jee2024.discussionappproject.service.ResponseService;
import ch.hearc.jee2024.discussionappproject.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/responses")
public class ResponseController {
    private final ResponseService responseService;
    private final UserService userService;
    private final DiscussionService discussionService;

    public ResponseController(ResponseService responseService, UserService userService, DiscussionService discussionService) {
        this.responseService = responseService;
        this.userService = userService;
        this.discussionService = discussionService;
    }

    @GetMapping
    public List<Response> getAllResponses() {
        return responseService.getAllResponses();
    }

    @GetMapping("/{id}")
    public Response getResponseById(@PathVariable Long id) {
        return responseService.getResponseById(id).orElseThrow(() -> new RuntimeException("Response not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable Long id, @RequestParam(required = true) Long userId) {
        Response response = responseService.getResponseById(id)
                .orElseThrow(() -> new RuntimeException("Response not found"));

        User requestingUser = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!requestingUser.getRole().equals(Role.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        responseService.deleteResponse(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping
    public ResponseEntity<?> createResponse(@RequestBody Response response) {
        Discussion discussion = discussionService.getDiscussionById(response.getDiscussion().getId())
                .orElseThrow(() -> new RuntimeException("Discussion not found"));

        if (discussion.isBlocked()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Cannot respond to a blocked discussion.");
        }

        Response createdResponse = responseService.createResponse(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdResponse);
    }
}

