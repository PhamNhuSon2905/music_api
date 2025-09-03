package com.music_backend_api.music_api.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    private final Map<String, Long> tokenBlacklist = new ConcurrentHashMap<>();

    public void blacklistToken(String token, long expiry) {
        tokenBlacklist.put(token, expiry);
    }

    public boolean isTokenBlacklisted(String token) {
        Long expiry = tokenBlacklist.get(token);
        if (expiry == null) return false;
        if (expiry < System.currentTimeMillis()) {
            tokenBlacklist.remove(token); // dọn rác
            return false;
        }
        return true;
    }
}
