package com.kari.karicalender.repository;

import com.kari.karicalender.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //로그인
    Optional<User> findByUserId(String userId);

    // 회원가입 시 중복 체크: 아이디가 이미 있는지 확인
    boolean existsByUserId(String userId);

    // 회원가입 시 중복 체크: 닉네임이 이미 있는지 확인
    boolean existsByNickname(String nickname);
}