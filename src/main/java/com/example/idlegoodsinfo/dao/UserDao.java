package com.example.idlegoodsinfo.dao;

import com.example.idlegoodsinfo.entity.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    User save(User user);
}

