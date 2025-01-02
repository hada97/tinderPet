package com.mvp.tinderpet.domain.dog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DogDto {

    @NotBlank
    private String name;

    @NotBlank
    private String breed;

    @PositiveOrZero
    private int age;

    private Sex gender;

    private Size size;

    @NotBlank
    private String profilePictureUrl;

    private String description;

    @NotNull
    private boolean isNeutered;

}
