package com.mvp.tinderpet.domain.dog;

import com.mvp.tinderpet.domain.user.Sex;
import com.mvp.tinderpet.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "dogs")  // Especifica o nome da tabela
@NoArgsConstructor  // Gera um construtor sem argumentos
@AllArgsConstructor  // Gera um construtor com todos os argumentos
@Builder  // Gera o padrão de construção com o builder
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String breed;  // A palavra "raca" pode ser trocada para "breed", mais comum no inglês

    @Positive
    private int age;  // Melhor usar um inteiro para idade, que permite cálculos e comparações

    @Enumerated(EnumType.STRING)
    private Sex gender;  // Sexo pode ser um Enum

    @Lob  // Se for armazenar imagens, considere usar @Lob ou uma URL
    private String profilePictureUrl;  // URL para a imagem do cão

    private String description;  // Uma descrição curta do cão (personalidade, comportamento, etc.)

    private String location;  // Localização (cidade, estado, ou coordenadas geográficas)

    @Enumerated(EnumType.STRING)
    private Size size;  // Tamanho do cão (Pequeno, Médio, Grande)

    private boolean isNeutered;  // Informação se o cão é castrado

    @ManyToOne  // Cada Dog pertence a um único User
    @JoinColumn(name = "user_id")  // Coluna que faz a chave estrangeira para a tabela User
    private User user;  // O dono do cão

}
