package com.mvp.tinderpet.domain.user;

import com.mvp.tinderpet.domain.dog.Dog;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

public record UserDto(
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ser válido")
        String email,

        String phone,

        String address,

        Double latitude,

        Double longitude,

        Set<Dog> dogs
) {

}
