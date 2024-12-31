package com.mvp.tinderpet.repository;

import com.mvp.tinderpet.domain.dog.Dog;
import com.mvp.tinderpet.domain.like.Like;
import com.mvp.tinderpet.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByDogAndUser(Dog dog, User user);
}