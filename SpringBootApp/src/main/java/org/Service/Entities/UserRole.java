package org.Service.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USER_ROLE")
public class UserRole implements Serializable {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;

    @Id
    @Column(name = "userId", nullable = false, unique = true)
    private int userId;

    @Column(name = "role", nullable = false)
    private String role;

}
