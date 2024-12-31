package com.mvp.tinderpet.controller;

import com.mvp.tinderpet.domain.dog.*;
import com.mvp.tinderpet.service.DogService;
import com.mvp.tinderpet.service.LikeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dogs")
@SecurityRequirement(name = "bearer-key")
public class DogController {

    @Autowired
    private DogService dogService;

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private LikeService likeService;

    @GetMapping
    public Page<Dog> getAllDogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return dogService.getDogs(page, size);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Dog> getDogById(@PathVariable Long id) {
        Dog dog = dogService.getDogById(id);
        if (dog != null) {
            return new ResponseEntity<>(dog, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping
    public ResponseEntity<DogDetail> createDog(@Valid @RequestBody DogDto dogDto) {
        var dto = dogService.createDog(dogDto);
        return ResponseEntity.ok(dto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Dog> updateDog(@Valid @PathVariable Long id, @RequestBody Dog dog) {
        Dog updatedDog = dogService.updateDog(id, dog);
        if (updatedDog != null) {
            return new ResponseEntity<>(updatedDog, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDog(@PathVariable Long id) {
        dogService.deleteDog(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/{dogId}/like/{userId}")
    public ResponseEntity<String> likeDog(@PathVariable Long dogId, @PathVariable Long userId) {
        boolean liked = likeService.addLike(dogId, userId);
        if (liked) {
            return ResponseEntity.ok("Cão com ID " + dogId + " curtiu com sucesso!");
        } else {
            return ResponseEntity.badRequest().body("Falha ao curtir o cão com ID " + dogId);
        }
    }

    @DeleteMapping("/{dogId}/like/{userId}")
    public ResponseEntity<String> removeLike(@PathVariable Long dogId, @PathVariable Long userId) {
        boolean removed = likeService.removeLike(dogId, userId);
        if (removed) {
            return ResponseEntity.ok("A curtida foi removida com sucesso!");
        } else {
            return ResponseEntity.badRequest().body("Falha ao remover a curtida.");
        }
    }



}
