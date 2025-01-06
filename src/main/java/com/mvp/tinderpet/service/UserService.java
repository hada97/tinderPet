package com.mvp.tinderpet.service;


import com.mvp.tinderpet.domain.user.*;
import com.mvp.tinderpet.location.GeocodingService;
import com.mvp.tinderpet.repository.DogRepository;
import com.mvp.tinderpet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GeocodingService geocodingService;

    @Autowired
    private DogRepository dogRepository;

    @Cacheable(value = "users")
    public Page<User> getAll(int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        return userRepository.findAll(pageRequest);
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true) // Invalid cache
    public void registerNewUser(String email, String name) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        User savedUser = userRepository.save(newUser);
        calculateCoordinates(savedUser);
        new UserDetail(
                savedUser.getName(),
                savedUser.getDogs()
        );
    }


    @Async
    public void calculateCoordinates(User user) {
        double[] coordinates = geocodingService.geocode(user.getAddress());
        if (coordinates != null && coordinates.length == 2) {
            user.setLatitude(coordinates[0]);
            user.setLongitude(coordinates[1]);
            userRepository.save(user);
        } else {
            System.err.println("Erro ao obter coordenadas para o endereço: " + user.getAddress());
        }
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    public boolean deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        }
        return false;
    }


    public Long getUserIdByEmailFromGoogle() {
        OAuth2User oAuth2User = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            throw new NoSuchElementException("Email não encontrado");
        }
        User user = userRepository.findByEmail(email);
        return user.getId();
    }


    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Transactional
    public void updateUser(UserUpdateDto data){
        Long userId = getUserIdByEmailFromGoogle();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        user.setName(data.getName());
        user.setAddress(data.getAddress());
        user.setPhone(data.getPhone());
        userRepository.save(user);

    }

}
