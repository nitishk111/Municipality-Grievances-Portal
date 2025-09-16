package io.nitishc.grievance_portal.repository;

import io.nitishc.grievance_portal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByPhone(String phone);
}
