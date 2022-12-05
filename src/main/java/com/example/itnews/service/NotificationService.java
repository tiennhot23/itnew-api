package com.example.itnews.service;

import com.example.itnews.entity.Notification;

import java.util.List;

public interface NotificationService {
    Notification addNotification(Integer idAccount, String content, String link);
    Notification get(Integer idNotification);
    Notification save(Notification notification);
    Boolean isExist(Integer idNotification);
    void delete(Integer idNotification);

    List<Notification> listNotification(Integer idAccount);
    List<Notification> listAllNotification(Integer idAccount);
    List<Notification> listUnreadNotification(Integer idAccount);
    void readNotification(Integer idNotification);
    void readAllNotification(Integer idAccount);
    void deleteAllNotification(Integer idAccount);


}
