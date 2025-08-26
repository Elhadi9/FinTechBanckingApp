package com.bank.repository;

import com.bank.model.Account;
import com.bank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUser(User user);
    Optional<Account> findByAccountNumber(String accountNumber);
    Boolean existsByAccountNumber(String accountNumber);
    void deleteByAccountNumber(String accountNumber);

    // Add these methods for admin dashboard
    List<Account> findByStatus(Account.AccountStatus status);

    @Query("SELECT a FROM Account a WHERE " +
            "(:accountNumber IS NULL OR a.accountNumber LIKE CONCAT('%', :accountNumber, '%')) AND " +
            "(:status IS NULL OR a.status = :status)")
    List<Account> findAccountsByFilters(@Param("accountNumber") String accountNumber,
                                        @Param("status") Account.AccountStatus status);
}