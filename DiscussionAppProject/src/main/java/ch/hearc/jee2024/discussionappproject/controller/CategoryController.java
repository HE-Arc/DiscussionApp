package ch.hearc.jee2024.discussionappproject.controller;

import ch.hearc.jee2024.discussionappproject.model.Category;
import ch.hearc.jee2024.discussionappproject.model.Discussion;
import ch.hearc.jee2024.discussionappproject.model.Role;
import ch.hearc.jee2024.discussionappproject.model.User;
import ch.hearc.jee2024.discussionappproject.service.CategoryService;
import ch.hearc.jee2024.discussionappproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category, @RequestParam Long userId) {
        try {
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!user.getRole().equals(Role.ADMIN)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            Category createdCategory = categoryService.createCategory(category, userId);
            return ResponseEntity.ok(createdCategory);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

    @GetMapping("/{id}/discussions")
    public ResponseEntity<List<Discussion>> getDiscussionsByCategory(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));


        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        List<Discussion> discussions = category.getDiscussions();

        return ResponseEntity.ok(discussions);
    }
}

