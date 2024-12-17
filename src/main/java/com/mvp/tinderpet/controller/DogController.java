package com.mvp.tinderpet.controller;

import com.mvp.tinderpet.domain.dog.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dogs")
public class DogController {

    @Autowired
    private DogService dogService;

    @Autowired
    private DogRepository dogRepository;

    @GetMapping
    public ResponseEntity<List<Dog>> getAllUsers() {
        List<Dog> dogs = dogRepository.findAll();
        return ResponseEntity.ok(dogs);
    }

    @GetMapping("/page")
    public Page<Dog> getDriversDisponiveis(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return dogService.getAllDogs(page, size);
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
    public ResponseEntity<DogDetailDTO> createDog(@Valid @RequestBody DogDTO dados) {
        var dto = dogService.createDog(dados);
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
        boolean deleted = dogService.deleteDog(id).hasBody();
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
