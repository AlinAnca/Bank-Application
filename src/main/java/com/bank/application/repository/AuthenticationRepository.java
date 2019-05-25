package com.bank.application.repository;

import com.bank.application.model.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {

    Integer deleteAuthenticationByToken(String token);

    Optional<Authentication> findAuthenticationByReference(Long reference);

    Optional<Authentication> findAuthenticationByToken(String token);

    Authentication save(Authentication authentication);
}
