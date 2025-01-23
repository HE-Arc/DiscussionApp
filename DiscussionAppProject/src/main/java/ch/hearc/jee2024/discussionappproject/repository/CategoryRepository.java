package ch.hearc.jee2024.discussionappproject.repository;

import ch.hearc.jee2024.discussionappproject.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
