package com.bank.repository;

import com.bank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    // Fix the search method
    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<User> findByUsernameContainingOrEmailContaining(@Param("search") String search);

    // Add these for admin functionality
    List<User> findByUsernameContainingIgnoreCase(String username);
    List<User> findByEmailContainingIgnoreCase(String email);
}