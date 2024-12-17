package com.mvp.tinderpet.domain.dog;

import com.mvp.tinderpet.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Data
@Table(name = "dogs")  // Especifica o nome da tabela
@NoArgsConstructor  // Gera um construtor sem argumentos
@AllArgsConstructor  // Gera um construtor com todos os argumentos
@Builder  // Gera o padrão de construção com o builder
@Getter
@Setter
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String breed;  // A palavra "raca"

    @Positive
    private int age;  // Melhor usar um inteiro para idade, que permite cálculos e comparações

    @Enumerated(EnumType.STRING)
    private Sex gender;

    @Enumerated(EnumType.STRING)
    private Size size;

    private String profilePictureUrl;  // URL para a imagem

    private String description;

    @NotBlank
    private String location;

    @NotNull(message = "A informação de castração é obrigatória")
    private boolean isNeutered;  // Informação se o cão é castrado

    @ManyToOne  // Cada Dog único User
    @JoinColumn(name = "user_id")  // Coluna que faz a chave estrangeira para a tabela User
    private User user;

}
