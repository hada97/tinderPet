package com.mvp.tinderpet.domain.user;

import com.mvp.tinderpet.domain.dog.Dog;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

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

    @NotBlank
    @Column(unique = true)
    private String phone;

    @OneToMany(mappedBy = "user")  // Carregar todos os cachorros imediatamente
    private List<Dog> dogs;

    @NotBlank
    private String address;  // Renomeei para seguir convenções de nomenclatura em Java

    private Double latitude;  // Latitude do usuário

    private Double longitude; // Longitude do usuário

}
