package com.example.idlegoodsinfo.service;

import com.example.idlegoodsinfo.dao.UserDao;
import com.example.idlegoodsinfo.dao.impl.JdbcUserDao;
import com.example.idlegoodsinfo.entity.User;

import java.util.Optional;

public class UserService {
    private final UserDao userDao;

    public UserService() {
        this.userDao = new JdbcUserDao();
    }

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<User> login(String username, String password) {
        return userDao.findByUsername(username)
                .filter(user -> PasswordHasher.matches(password, user.getPasswordHash()));
    }

    public User register(String username, String rawPassword) {
        userDao.findByUsername(username).ifPresent(user -> {
            throw new IllegalArgumentException("用户名已存在");
        });
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(PasswordHasher.hash(rawPassword));
        return userDao.save(user);
    }

    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }
}

