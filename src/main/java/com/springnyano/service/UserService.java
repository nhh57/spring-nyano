package com.springnyano.service;

import com.springnyano.entity.user.UserEntity;

import java.util.List;

public interface UserService {

    UserEntity createUser(UserEntity user);

    List<UserEntity> getAllUsers();
}
