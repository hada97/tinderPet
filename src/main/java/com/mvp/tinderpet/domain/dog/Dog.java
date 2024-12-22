package com.mvp.tinderpet.domain.dog;

import com.fasterxml.jackson.annotation.JsonIgnore;  // Importar para evitar a serialização recursiva
import com.mvp.tinderpet.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "dogs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String breed;

    @PositiveOrZero
    private int age;

    @Enumerated(EnumType.STRING)
    private Sex gender;

    @Enumerated(EnumType.STRING)
    private Size size;

    @NotBlank
    private String profilePictureUrl;

    private String description;

    @NotNull(message = "A informação de castração é obrigatória")
    private boolean isNeutered;

    @ManyToOne
    @JoinColumn(name = "user_id")  // Coluna que faz a chave estrangeira
    @JsonIgnore  // Evita que o usuário seja serializado ao serializar o Dog
    private User user;

    @ElementCollection
    @CollectionTable(name = "dog_likes", joinColumns = @JoinColumn(name = "dog_id"))
    @Column(name = "liked_user_id")
    private Set<Long> likes = new HashSet<>();


    public void addLike(Long dogId) {
        likes.add(dogId);
    }

    public void removeLike(Long dogId) {
        likes.remove(dogId);
    }

}
