package com.mvp.tinderpet.controller;

import com.mvp.tinderpet.domain.dog.*;
import com.mvp.tinderpet.repository.DogRepository;
import com.mvp.tinderpet.service.DogService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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


}
