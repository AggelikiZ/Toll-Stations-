package com.payway.services;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class jwtBlackListService {
    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

    public boolean addToBlacklist(String token) {
        return blacklist.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }
}

