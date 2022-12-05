package com.example.itnews.repository;

import com.example.itnews.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Optional<List<Notification>> findAllByAccount_IdAccountOrderByIdNotificationDesc(Integer idAccount);
    Optional<List<Notification>> findAllByAccount_IdAccountAndStatusOrderByIdNotificationDesc(Integer idAccount, Integer status);
    void deleteAllByAccount_IdAccount(Integer idAccount);
}
