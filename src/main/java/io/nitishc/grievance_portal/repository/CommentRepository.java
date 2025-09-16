package io.nitishc.grievance_portal.repository;

import io.nitishc.grievance_portal.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
