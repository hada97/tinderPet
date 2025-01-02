package com.mvp.tinderpet.domain.user;

import com.mvp.tinderpet.domain.dog.Dog;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

public record UserDto(
        @NotBlank
        String name,

        @NotBlank
        @Email
        String email,

        String phone,

        String address,

        Double latitude,

        Double longitude,

        Set<Dog> dogs
) {

}
