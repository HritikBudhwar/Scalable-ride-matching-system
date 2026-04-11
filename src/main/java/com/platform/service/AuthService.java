package com.platform.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple authentication service for demo purposes
 * In production, use Spring Security with JWT
 */
@Service
public class AuthService {

    private final Map<String, String> userTokens = new HashMap<>();
    private final Map<String, Long> tokenToUserId = new HashMap<>();
    private final Map<String, String> tokenToUserType = new HashMap<>();

    public String generateToken(Long userId, String userType) {
        // Simple token generation - in production use JWT
        String token = "token_" + userId + "_" + System.currentTimeMillis();
        userTokens.put(userId.toString(), token);
        tokenToUserId.put(token, userId);
        tokenToUserType.put(token, userType);
        return token;
    }

    public boolean validateToken(String token) {
        return tokenToUserId.containsKey(token);
    }

    public Long getUserIdFromToken(String token) {
        return tokenToUserId.get(token);
    }

    public void invalidateToken(String token) {
        Long userId = tokenToUserId.remove(token);
        tokenToUserType.remove(token);
        if (userId != null) {
            userTokens.remove(userId.toString());
        }
    }

    public Map<String, Object> getUserInfo(String token) {
        Long userId = getUserIdFromToken(token);
        if (userId != null) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", userId);
            userInfo.put("token", token);
            userInfo.put("userType", tokenToUserType.get(token));
            return userInfo;
        }
        return null;
    }
}
