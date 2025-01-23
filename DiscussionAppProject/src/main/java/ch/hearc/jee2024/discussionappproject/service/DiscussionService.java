package ch.hearc.jee2024.discussionappproject.service;

import ch.hearc.jee2024.discussionappproject.model.Category;
import ch.hearc.jee2024.discussionappproject.model.Discussion;
import ch.hearc.jee2024.discussionappproject.model.Role;
import ch.hearc.jee2024.discussionappproject.model.User;
import ch.hearc.jee2024.discussionappproject.repository.CategoryRepository;
import ch.hearc.jee2024.discussionappproject.repository.DiscussionRepository;
import ch.hearc.jee2024.discussionappproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscussionService {
    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public DiscussionService(DiscussionRepository discussionRepository, CategoryRepository categoryRepository) {
        this.discussionRepository = discussionRepository;
        this.categoryRepository = categoryRepository;
    }

    public Discussion findDiscussionById(Long id) {
        Optional<Discussion> discussion = discussionRepository.findById(id);
        return discussion.orElse(null);
    }

    public Discussion saveDiscussion(Discussion discussion) {
        return discussionRepository.save(discussion);
    }

    public Page<Discussion> getAllDiscussionsPaged(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return discussionRepository.findAll(pageable);
    }

    public Optional<Discussion> getDiscussionById(Long id) {
        return discussionRepository.findById(id);
    }

    public Discussion createDiscussion(Discussion discussion) {
        Category category = categoryRepository.findById(discussion.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));

        User user = userRepository.findById(discussion.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        discussion.setCategory(category);
        discussion.setUser(user);
        return discussionRepository.save(discussion);
    }

    public void deleteDiscussion(Long discussionId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only administrators can delete discussions");
        }

        discussionRepository.deleteById(discussionId);
    }

    public List<Discussion> getDiscussionsByUser(Long userId) {
        return discussionRepository.findByUserId(userId);
    }

    public void blockDiscussion(Long discussionId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getRole().equals(Role.ADMIN)) {
            throw new RuntimeException("Only admins can block discussions.");
        }

        Discussion discussion = discussionRepository.findById(discussionId)
                .orElseThrow(() -> new RuntimeException("Discussion not found"));

        discussion.setBlocked(true);
        discussionRepository.save(discussion);
    }
}
