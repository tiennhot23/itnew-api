package com.example.itnews.service.impl;

import com.example.itnews.entity.Account;
import com.example.itnews.entity.Notification;
import com.example.itnews.payloads.response.MException;
import com.example.itnews.repository.AccountRepository;
import com.example.itnews.repository.LockAccountRepository;
import com.example.itnews.repository.NotificationRepository;
import com.example.itnews.security.exceptions.MRuntimeException;
import com.example.itnews.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final String TAG = "NotificationServiceImpl";
    private final NotificationRepository notificationRepository;
    private final AccountRepository accountRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   AccountRepository accountRepository) {
        this.notificationRepository = notificationRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Notification addNotification(Integer idAccount, String content, String link) {
        Account account = accountRepository.findById(idAccount)
                .orElseThrow(() -> new MRuntimeException("Account  not found", HttpStatus.NOT_FOUND));
        return notificationRepository.save(
                Notification.builder()
                        .account(account)
                        .content(content)
                        .link(link).build());
    }

    @Override
    public Notification get(Integer idNotification) {
        return notificationRepository.findById(idNotification)
                .orElseThrow(() -> new MRuntimeException("Notification  not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public Boolean isExist(Integer idNotification) {
        return notificationRepository.findById(idNotification).isPresent();
    }

    @Override
    public void delete(Integer idNotification) {
        notificationRepository.deleteById(idNotification);
    }

    @Override
    public List<Notification> listNotification(Integer idAccount) {
        return notificationRepository.findAllByAccount_IdAccountAndStatusOrderByIdNotificationDesc(idAccount, 0)
                .orElseGet(ArrayList::new);
    }

    @Override
    public List<Notification> listAllNotification(Integer idAccount) {
        return notificationRepository.findAllByAccount_IdAccountOrderByIdNotificationDesc(idAccount)
                .orElseGet(ArrayList::new);
    }

    @Override
    public List<Notification> listUnreadNotification(Integer idAccount) {
        return notificationRepository.findAllByAccount_IdAccountAndStatusOrderByIdNotificationDesc(idAccount, 0)
                .orElseGet(ArrayList::new);
    }

    @Override
    public void readNotification(Integer idNotification) {
        Notification notification = notificationRepository.findById(idNotification)
                .orElseThrow(() -> new MRuntimeException("Notification  not found", HttpStatus.NOT_FOUND));
        notification.setStatus(1);
        notificationRepository.save(notification);
    }

    @Override
    public void readAllNotification(Integer idAccount) {
        List<Notification> notifications = notificationRepository.findAllByAccount_IdAccountOrderByIdNotificationDesc(idAccount)
                .orElseGet(ArrayList::new);
        for (Notification notification : notifications) {
            notification.setStatus(1);
        }
        notificationRepository.saveAll(notifications);
    }

    @Override
    public void deleteAllNotification(Integer idAccount) {
        notificationRepository.deleteAllByAccount_IdAccount(idAccount);
    }
}
