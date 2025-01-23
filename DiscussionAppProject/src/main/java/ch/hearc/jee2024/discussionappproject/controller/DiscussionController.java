package ch.hearc.jee2024.discussionappproject.controller;

import ch.hearc.jee2024.discussionappproject.model.*;
import ch.hearc.jee2024.discussionappproject.service.CategoryService;
import ch.hearc.jee2024.discussionappproject.service.DiscussionService;
import ch.hearc.jee2024.discussionappproject.service.ResponseService;
import ch.hearc.jee2024.discussionappproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discussions")
public class DiscussionController {

    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ResponseService responseService;

    public DiscussionController(DiscussionService discussionService, ResponseService responseService, UserService userService) {
        this.discussionService = discussionService;
        this.responseService = responseService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<Discussion>> getAllDiscussions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sortBy) {

        Page<Discussion> discussions = discussionService.getAllDiscussionsPaged(page, size, sortBy);
        return ResponseEntity.ok(discussions);
    }

    @GetMapping("/{discussionId}/responses")
    public ResponseEntity<List<Response>> getResponsesByDiscussion(@PathVariable Long discussionId) {
        List<Response> responses = responseService.findResponsesByDiscussionId(discussionId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public Discussion getDiscussionById(@PathVariable Long id) {
        return discussionService.getDiscussionById(id).orElseThrow(() -> new RuntimeException("Discussion not found"));
    }

    @PostMapping
    public ResponseEntity<Discussion> createDiscussion(@RequestBody Discussion discussion) {
        if (discussion.getTitle() == null || discussion.getUser() == null || discussion.getCategory() == null) {
            throw new IllegalArgumentException("Titre, utilisateur et catégorie sont obligatoires.");
        }

        Discussion savedDiscussion = discussionService.createDiscussion(discussion);
        return ResponseEntity.ok(savedDiscussion);
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<?> blockDiscussion(@PathVariable Long id, @RequestParam Long userId) {
        try {
            discussionService.blockDiscussion(id, userId);
            return ResponseEntity.ok("Discussion blocked successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<Void> unblockDiscussion(@PathVariable Long id, @RequestParam Long userId) {
        User user = userService.getUserById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getRole().equals(Role.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Discussion discussion = discussionService.getDiscussionById(id).orElseThrow(() -> new RuntimeException("Discussion not found"));
        discussion.setBlocked(false);
        discussionService.saveDiscussion(discussion);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Discussion> updateDiscussion(
            @PathVariable Long id,
            @RequestBody Discussion updatedDiscussion) {

        Discussion existingDiscussion = discussionService.findDiscussionById(id);

        if (existingDiscussion == null) {
            return ResponseEntity.notFound().build();
        }

        if (updatedDiscussion.getTitle() == null || updatedDiscussion.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Le titre de la discussion est obligatoire.");
        }

        existingDiscussion.setTitle(updatedDiscussion.getTitle());
        existingDiscussion.setCategory(updatedDiscussion.getCategory());
        discussionService.saveDiscussion(existingDiscussion);

        return ResponseEntity.ok(existingDiscussion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscussion(@PathVariable Long id, @RequestParam Long userId) {
        try {
            discussionService.deleteDiscussion(id, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}