package com.bank.repository;

import com.bank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);


    // Add these for admin search functionality
    List<User> findByUsernameContainingIgnoreCase(String username);
    List<User> findByEmailContainingIgnoreCase(String email);
    List<User> findByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCase(String username, String email);
}