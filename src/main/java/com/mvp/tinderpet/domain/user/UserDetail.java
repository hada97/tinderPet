package com.mvp.tinderpet.domain.user;

import com.mvp.tinderpet.domain.dog.Dog;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

public record UserDetail(
        @NotBlank String name,

        Set<Dog> dogs
) {

}