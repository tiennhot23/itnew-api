package com.example.itnews.repository;

import com.example.itnews.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Integer> {
    @Query("SELECT v FROM Verification v WHERE v.account.idAccount = ?1")
    Optional<Verification> findByIdAccount(Integer idAccount);
}
