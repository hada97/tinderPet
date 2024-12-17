package com.mvp.tinderpet.domain.user;

import com.mvp.tinderpet.domain.dog.Dog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    private String name;

    private List<Dog> dogs;

}
