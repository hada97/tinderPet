package com.mvp.tinderpet.domain.dog;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DogUpdateDto {

    private String name;

    private String breed;

    @PositiveOrZero
    private int age;

    private Size size;

    private String profilePictureUrl;

    private String description;

    private boolean isNeutered;


    public Dog toEntity() {
        return Dog.builder()
                .name(this.name)
                .breed(this.breed)
                .age(this.age)
                .size(this.size)
                .profilePictureUrl(this.profilePictureUrl)
                .description(this.description)
                .isNeutered(this.isNeutered)
                .build();
    }
}
