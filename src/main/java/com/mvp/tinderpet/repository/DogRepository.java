package com.mvp.tinderpet.repository;

import com.mvp.tinderpet.domain.dog.Dog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface DogRepository extends JpaRepository<Dog, Long> {

    Page<Dog> findAll(Pageable pageable);

    Page<Dog> findByUserId(Long userId, Pageable pageable);

    Page<Dog> findByUserIdNot(Long userId, Pageable pageable);

    @Query("SELECT d FROM Dog d WHERE d.user.id != :userId AND d.id NOT IN :likedDogsIds")
    Page<Dog> findByUserIdNotAndIdNotIn(@Param("userId") Long userId, @Param("likedDogsIds") Set<Long> likedDogsIds, Pageable pageable);


}