package com.mvp.tinderpet.controller;

import com.mvp.tinderpet.repository.DogRepository;
import com.mvp.tinderpet.service.DogService;
import com.mvp.tinderpet.service.LikeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
@SecurityRequirement(name = "bearer-key")
public class LikeController {

    @Autowired
    private DogService dogService;

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private LikeService likeService;

    @PostMapping("/{dogId}/{userId}")
    public ResponseEntity<String> likeDog(@PathVariable Long dogId, @PathVariable Long userId) {
        boolean liked = likeService.addLike(dogId, userId);
        if (liked) {
            return ResponseEntity.ok("Cão com ID " + dogId + " curtiu com sucesso!");
        } else {
            return ResponseEntity.badRequest().body("Falha ao curtir o cão com ID " + dogId);
        }
    }

    @DeleteMapping("/{dogId}/{userId}")
    public ResponseEntity<String> removeLike(@PathVariable Long dogId, @PathVariable Long userId) {
        boolean removed = likeService.removeLike(dogId, userId);
        if (removed) {
            return ResponseEntity.ok("A curtida foi removida com sucesso!");
        } else {
            return ResponseEntity.badRequest().body("Falha ao remover a curtida.");
        }
    }




}
