package com.example.foody.service;

public interface FirebaseService {
    String createCustomToken(String uid);
    boolean verifyToken(String token);
}