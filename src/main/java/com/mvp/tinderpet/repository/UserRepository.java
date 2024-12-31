package com.mvp.tinderpet.repository;

import com.mvp.tinderpet.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}