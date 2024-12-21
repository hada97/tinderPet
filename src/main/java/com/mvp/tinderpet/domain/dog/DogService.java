package com.mvp.tinderpet.domain.dog;

import com.mvp.tinderpet.domain.user.User;
import com.mvp.tinderpet.domain.user.UserRepository;
import com.mvp.tinderpet.domain.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Service
public class DogService {

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @Cacheable(value = "dogs")
    public Page<Dog> getDogs(int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        return dogRepository.findAll(pageRequest);
    }

    public Dog getDogById(Long id) {
        return dogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cão não encontrado com ID: " + id));
    }

    @Transactional
    @CacheEvict(value = "dogs")
    public DogDetail createDog(DogDto dados) {

        Long userId = userService.getUserIdByEmailFromGoogle();

        User user = userRepository.findById(userId)
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
        return new DogDetail(dog);
    }

    @Transactional
    @CachePut(value = "dogs", key = "#id")
    public Dog updateDog(Long id, Dog dog) {
        Dog dogEntity = dogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cão não encontrado com ID: " + id));
        return dogRepository.save(dogEntity);
    }


    @Transactional
    @CacheEvict(value = "dogs")
    public void deleteDog(Long id) {
        Dog dogEntity = dogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cão não encontrado com ID: " + id));
        dogRepository.delete(dogEntity);
        ResponseEntity.noContent().build();
    }

    @Transactional
    @CacheEvict(value = "dogs")
    public ResponseEntity<?> deleteDog(Long id, OAuth2AuthenticationToken authentication) {
        OAuth2User oAuth2User = authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        Dog dogEntity = dogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cão não encontrado com ID: " + id));

        if (!dogEntity.getUser().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para excluir este cão.");
        }
        dogRepository.delete(dogEntity);
        return ResponseEntity.noContent().build();
    }


}
