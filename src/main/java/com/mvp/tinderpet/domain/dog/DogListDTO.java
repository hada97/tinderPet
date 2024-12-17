package com.mvp.tinderpet.domain.dog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DogListDTO {

    private Long id; // ID do cão, para identificá-lo
    private String name; // Nome do cão
    private String raca; // Raça do cão
    private String idade; // Idade do cão
    private String sexo; // Sexo do cão
    private String profilePictureUrl; // URL da foto do cão
    private boolean isNeutered; // castrado

}
