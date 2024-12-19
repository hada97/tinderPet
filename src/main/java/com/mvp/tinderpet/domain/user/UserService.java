package com.mvp.tinderpet.domain.user;


import com.mvp.tinderpet.location.GeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GeocodingService geocodingService;

    @Cacheable(value = "users")
    public Page<User> getAll(int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        return userRepository.findAll(pageRequest);
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true) // Invalid cache
    public UserDetail createUser(UserDto userDTO) {

        User user = new User();
        user.setName(userDTO.name());
        user.setEmail(userDTO.email());
        user.setPhone(userDTO.phone());
        user.setAddress(userDTO.address());

        User savedUser = userRepository.save(user);

        calculateCoordinates(savedUser);

        return new UserDetail(
                savedUser.getId(),  // id
                savedUser.getName(), // name
                savedUser.getDogs()  // dogs
        );
    }

    //calcular as coordenadas assíncrono
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
    @CacheEvict(value = "users", allEntries = true) // Invalid cache
    public boolean deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        }
        return false;
    }

}
