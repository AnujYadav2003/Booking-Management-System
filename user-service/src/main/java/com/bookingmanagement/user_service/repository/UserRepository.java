package com.bookingmanagement.user_service.repository;

import com.bookingmanagement.user_service.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel,Integer> {

    Optional<UserModel> findByEmail(String email);
}
