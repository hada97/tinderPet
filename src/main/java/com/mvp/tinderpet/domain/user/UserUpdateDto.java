package com.mvp.tinderpet.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDto {

    String name;

    String address;

    String phone;


    public User toEntity() {
        return User.builder()
                .name(this.name)
                .address(this.address)
                .phone(this.phone)
                .build();
    }

}
