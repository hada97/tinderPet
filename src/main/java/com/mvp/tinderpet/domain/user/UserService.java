package com.mvp.tinderpet.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Cacheable(value = "users")
    public Page<User> getAll(int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size); // Define a página e o tamanho
        return userRepository.findAll(pageRequest); // Passa o Pageable para o repositório
    }

}
