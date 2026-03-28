package com.platform.model.user;

import com.platform.model.core.Rateable;
import com.platform.model.enums.Gender;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Base user entity class
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements Rateable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}