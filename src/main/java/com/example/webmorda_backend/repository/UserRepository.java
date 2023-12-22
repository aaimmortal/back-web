package com.example.webmorda_backend.repository;

import com.example.webmorda_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByLogin(String login);
    Boolean existsByLogin(String login);
}
