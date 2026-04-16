package com.kari.karicalender.repository;

import com.kari.karicalender.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(String userId);
    boolean existsByUserId(String userId);
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
}