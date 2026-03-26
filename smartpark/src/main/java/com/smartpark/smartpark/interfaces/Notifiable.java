package com.smartpark.smartpark.interfaces;

public interface Notifiable {
    void sendEmailAlert(String to, String subject, String body);
    void sendSMSAlert(String phone, String message);
}