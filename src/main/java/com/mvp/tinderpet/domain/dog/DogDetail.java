package com.mvp.tinderpet.domain.dog;

public record DogDetail(
        Long id,  // Adiciona um campo id
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

    public DogDetail(Dog dog) {
        this(
                dog.getId(),  // Mapeia o id do Dog
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
