package ch.hearc.jee2024.discussionappproject.repository;

import ch.hearc.jee2024.discussionappproject.model.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {

    List<Discussion> findByUserId(Long userId);
}