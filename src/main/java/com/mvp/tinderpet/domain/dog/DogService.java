package com.mvp.tinderpet.domain.dog;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DogService {

    @Autowired
    private DogRepository dogRepository;


    @Cacheable(value = "dogs")
    public Page<Dog> getAllDogs(int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        return dogRepository.findAll(pageRequest);
    }


    @Cacheable(value = "dogs", key = "#id")
    public Dog getDogById(Long id) {
        return dogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cão não encontrado com ID: " + id));
    }


    @Transactional
    @CacheEvict(value = "dogs", key = "#result.id")
    public Dog createDog(Dog dog) {
        return dogRepository.save(dog);
    }

    @Transactional
    @CachePut(value = "dogs", key = "#id")
    public Dog updateDog(Long id, Dog dog) {
        Dog dogEntity = dogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cão não encontrado com ID: " + id));
        Dog updatedDog = dogRepository.save(dogEntity);
        return updatedDog;
    }


    @Transactional
    @CacheEvict(value = "dogs", key = "#id")
    public ResponseEntity<?> deleteDog(Long id) {
        Dog dogEntity = dogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cão não encontrado com ID: " + id));
        dogRepository.delete(dogEntity);
        return ResponseEntity.noContent().build();
    }

}
