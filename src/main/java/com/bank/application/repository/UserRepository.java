package com.bank.application.repository;

import com.bank.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "select * from user a join notification b ON a.id = b.user_id where b.status = 'NOT_SENT'", nativeQuery = true)
    List<User> findAllUsersNotNotifiedYet();

    @Query(value = "select email from user a join person b ON a.id = b.user_id where a.id = ?1", nativeQuery = true)
    String findUserEmailById(Long userId);

    User save(User user);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserById(Long userId);

    User deleteUserByUsername(String username);
}
