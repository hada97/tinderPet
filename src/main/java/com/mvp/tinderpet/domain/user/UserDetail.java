package com.mvp.tinderpet.domain.user;

import com.mvp.tinderpet.domain.dog.Dog;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record UserDetail(
        Long id,
        @NotBlank String name,
        List<Dog> dogs
) {

}