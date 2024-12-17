package com.mvp.tinderpet.domain.dog;

import com.mvp.tinderpet.domain.user.User;
import com.mvp.tinderpet.domain.user.UserRepository;
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

import java.util.NoSuchElementException;

@Service
public class DogService {

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private UserRepository userRepository;



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
    public DogDetailDTO createDog(DogDTO dados) {

        User user = userRepository.findById(dados.getUserId())
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        Dog dog = new Dog();
        dog.setName(dados.getName());
        dog.setBreed(dados.getBreed());
        dog.setAge(dados.getAge());
        dog.setGender(dados.getGender());
        dog.setSize(dados.getSize());
        dog.setProfilePictureUrl(dados.getProfilePictureUrl());
        dog.setDescription(dados.getDescription());
        dog.setNeutered(dados.isNeutered());
        dog.setUser(user);
        dogRepository.save(dog);

        return new DogDetailDTO(dog);
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
