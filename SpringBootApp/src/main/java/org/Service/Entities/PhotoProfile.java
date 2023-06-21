package org.Service.Entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PHOTO_PROFILE")
public class PhotoProfile {
    @Id
    @Column(name = "userId", nullable = false, unique = true)
    private int userId;

    @Column(name = "path", nullable = false)
    private String path;
}
