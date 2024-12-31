package com.mvp.tinderpet.service;

import com.mvp.tinderpet.domain.dog.Dog;
import com.mvp.tinderpet.repository.DogRepository;
import com.mvp.tinderpet.domain.like.Like;
import com.mvp.tinderpet.repository.LikeRepository;
import com.mvp.tinderpet.domain.user.User;
import com.mvp.tinderpet.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private LikeRepository likeRepository;


    @Transactional
    public boolean addLike(Long dogId, Long userId) {
        Optional<Dog> dogOptional = dogRepository.findById(dogId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (dogOptional.isPresent() && userOptional.isPresent()) {
            Dog dog = dogOptional.get();
            User user = userOptional.get();

            if (!user.getLikedDogsIds().contains(dogId)) {

                user.addLikedDog(dogId);

                Like like = new Like();
                like.setDog(dog);
                like.setUser(user);

                dog.addLike(like);

                likeRepository.save(like);

                dogRepository.save(dog);
                userRepository.save(user);

                return true;
            }
        }
        return false;
    }


    @Transactional
    public boolean removeLike(Long dogId, Long userId) {
        Optional<Dog> dogOptional = dogRepository.findById(dogId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (dogOptional.isPresent() && userOptional.isPresent()) {
            Dog dog = dogOptional.get();
            User user = userOptional.get();

            if (user.getLikedDogsIds().contains(dogId)) {
                // Remove o ID do cão da lista de cães curtidos do usuário
                user.removeLikedDog(dogId);

                Optional<Like> existingLike = likeRepository.findByDogAndUser(dog, user);
                if (existingLike.isPresent()) {
                    Like like = existingLike.get();

                    dog.removeLike(like);

                    likeRepository.delete(like);

                    dogRepository.save(dog);
                    userRepository.save(user);

                    return true;
                }
            }
        }
        return false;
    }
}