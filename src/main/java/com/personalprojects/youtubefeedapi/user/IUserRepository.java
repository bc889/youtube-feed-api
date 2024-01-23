package com.personalprojects.youtubefeedapi.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserId(String userId);

}
