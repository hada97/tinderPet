package com.mvp.tinderpet.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mvp.tinderpet.domain.dog.Dog;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phone;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Dog> dogs = new HashSet<>();

    private String address;

    private Double latitude;

    private Double longitude;

    @ElementCollection
    @CollectionTable(name = "user_likes", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "liked_dog_id")
    private Set<Long> likedDogsIds = new HashSet<>();

    // Métodos de hashCode e equals baseados no 'id' único
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);  // Usa o id único para comparação
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);  // Usa o id único para o cálculo do hashCode
    }

    public void addLikedDog(Long dogId) {
        likedDogsIds.add(dogId);
    }

    public void removeLikedDog(Long dogId) {
        likedDogsIds.remove(dogId);
    }
}
