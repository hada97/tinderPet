package com.mvp.tinderpet.domain.like;

import com.mvp.tinderpet.domain.dog.Dog;
import com.mvp.tinderpet.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dog_id")
    private Dog dog;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean isMutual;

}
