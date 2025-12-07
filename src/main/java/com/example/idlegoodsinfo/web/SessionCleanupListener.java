package com.example.idlegoodsinfo.web;

import com.example.idlegoodsinfo.entity.User;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class SessionCleanupListener implements HttpSessionListener {
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        Object userObj = se.getSession().getAttribute("currentUser");
        if (userObj instanceof User user) {
            SessionRegistry.removeIfMatching(user.getId(), se.getSession());
        }
    }
}

