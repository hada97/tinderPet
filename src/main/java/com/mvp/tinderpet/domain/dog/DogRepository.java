package com.mvp.tinderpet.domain.dog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DogRepository extends JpaRepository<Dog, Long> {

    Page<Dog> findAll(Pageable pageable);

    Page<Dog> findByUserId(Long userId, Pageable pageable);

    Page<Dog> findByUserIdNot(Long userId, Pageable pageable);

    Page<Dog> findByUserIdNotAndIdNotIn(Long userId, List<Long> likedDogsIds, Pageable pageable);


}