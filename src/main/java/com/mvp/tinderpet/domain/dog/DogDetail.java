package com.mvp.tinderpet.domain.dog;

public record DogDetail(

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
