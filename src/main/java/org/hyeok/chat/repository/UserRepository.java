package org.hyeok.chat.repository;

import java.util.Optional;

import org.hyeok.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}