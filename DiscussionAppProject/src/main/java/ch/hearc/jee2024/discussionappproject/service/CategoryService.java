package ch.hearc.jee2024.discussionappproject.service;

import ch.hearc.jee2024.discussionappproject.model.Category;
import ch.hearc.jee2024.discussionappproject.model.Role;
import ch.hearc.jee2024.discussionappproject.model.User;
import ch.hearc.jee2024.discussionappproject.repository.CategoryRepository;
import ch.hearc.jee2024.discussionappproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category createCategory(Category category, Long userId) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
}

