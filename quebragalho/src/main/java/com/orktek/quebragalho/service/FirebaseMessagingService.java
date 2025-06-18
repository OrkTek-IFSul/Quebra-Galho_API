package com.orktek.quebragalho.service;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FirebaseMessagingService {

    private final FirebaseMessaging firebaseMessaging;

    public FirebaseMessagingService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    public String sendNotification(String title, String body, String token, Map<String, String> data) throws FirebaseMessagingException {

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(token) // Token FCM do dispositivo de destino
                .setNotification(notification)
                .putAllData(data) // Dados customizados para o app (ex: para deep linking)
                .build();

        return firebaseMessaging.send(message);
    }
}