package com.dss.repository;

import com.dss.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findOneByEmail(String email);
    Optional<User> findOneByPhone(String phone);

}
