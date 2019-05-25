package com.bank.application.repository;

import com.bank.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User save(User user);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserById(Long userId);

    User deleteUserByUsername(String username);
}
