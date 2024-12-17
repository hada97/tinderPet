package com.mvp.tinderpet.domain.dog;

public record DogDetailDTO(
        Long id,
        String name,
        String breed,
        int age,
        Sex gender,
        Size size,
        String profilePictureUrl,
        String description,
        boolean isNeutered,
        Long userId
) {

    public DogDetailDTO(Dog dog) {
        this(
                dog.getId(),
                dog.getName(),
                dog.getBreed(),
                dog.getAge(),
                dog.getGender(),
                dog.getSize(),
                dog.getProfilePictureUrl(),
                dog.getDescription(),
                dog.isNeutered(),
                dog.getUser() != null ? dog.getUser().getId() : null
        );
    }
}
