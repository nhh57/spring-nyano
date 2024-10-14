package com.springnyano.repository;

import com.springnyano.entity.user.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUserNameAndUserEmail(String userName, String userEmail);

    UserEntity findByUserName(String userName);

    /**
     * WHERE userName LIKE %?
     */
    UserEntity findByUserNameIsStartingWith(String userName);

    /**
     * WHERE userName LIKE ?%
     */
    UserEntity findByUserNameIsEndingWith(String userName);

    /**
     *  WHERE id <1
     */
    List<UserEntity> findByIdLessThan(Long id);
}
