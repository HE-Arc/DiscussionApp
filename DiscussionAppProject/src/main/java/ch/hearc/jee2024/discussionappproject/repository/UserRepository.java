package ch.hearc.jee2024.discussionappproject.repository;

import ch.hearc.jee2024.discussionappproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}

