package com.mvp.tinderpet.domain.user;

import com.mvp.tinderpet.location.GeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GeocodingService geocodingService;

    @Cacheable(value = "users")
    public Page<User> getAll(int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        return userRepository.findAll(pageRequest);
    }

    @Transactional
    public User createUser(User user) {
        User savedUser = userRepository.save(user);
        calculateCoordinates(savedUser);
        return savedUser;
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

}
