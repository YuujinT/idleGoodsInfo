package com.example.idlegoodsinfo.web;

import jakarta.servlet.http.HttpSession;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class SessionRegistry {
    private static final ConcurrentMap<Long, HttpSession> ACTIVE_SESSIONS = new ConcurrentHashMap<>();

    private SessionRegistry() {
    }

    public static void register(Long userId, HttpSession session) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(session, "session must not be null");
        HttpSession previous = ACTIVE_SESSIONS.put(userId, session);
        session.setAttribute("sessionUserId", userId);
        if (previous != null && previous != session) {
            try {
                previous.invalidate();
            } catch (IllegalStateException ignored) {
                // 已失效会触发监听器清理
            }
        }
    }

    public static void removeIfMatching(Long userId, HttpSession session) {
        if (userId == null || session == null) {
            return;
        }
        ACTIVE_SESSIONS.computeIfPresent(userId, (id, existing) -> existing == session ? null : existing);
    }
}

