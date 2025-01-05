package com.mvp.tinderpet.domain.dog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mvp.tinderpet.domain.like.Like;
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

    @NotNull
    private boolean isNeutered;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "dog", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes = new HashSet<>();


    public void addLike(Like like) {
        this.likes.add(like);
    }

    public void removeLike(Like like) {
        likes.remove(like);
        like.setDog(null);
    }

    @Override
    public String toString() {
        // Retorna apenas informações simples, sem chamar o toString() do User
        return "Dog{id=" + id + ", name='" + name + "'}";
    }

}
