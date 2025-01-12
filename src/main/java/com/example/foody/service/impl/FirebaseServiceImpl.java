package com.example.foody.service.impl;

import com.example.foody.exceptions.firebase.FirebaseCustomTokenCreationException;
import com.example.foody.service.FirebaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.stereotype.Service;

@Service
public class FirebaseServiceImpl implements FirebaseService {
    @Override
    public String createCustomToken(String uid) {
        try {
            return FirebaseAuth.getInstance().createCustomToken(uid);
        } catch (FirebaseAuthException e) {
            throw new FirebaseCustomTokenCreationException(uid);
        }
    }
}