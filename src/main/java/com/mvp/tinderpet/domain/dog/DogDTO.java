package com.mvp.tinderpet.domain.dog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DogDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String breed;

    @Positive
    private int age;

    private Sex gender;

    private Size size;

    private String profilePictureUrl;

    private String description;

    @NotNull(message = "A informação de castração é obrigatória")
    private boolean isNeutered;

    private Long userId;  // Aqui, apenas o ID do User é passado, não o objeto completo de User
}
