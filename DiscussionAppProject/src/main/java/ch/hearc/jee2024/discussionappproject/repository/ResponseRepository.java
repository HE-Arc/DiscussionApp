package ch.hearc.jee2024.discussionappproject.repository;

import ch.hearc.jee2024.discussionappproject.model.Response;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findByDiscussionId(Long discussionId);
}