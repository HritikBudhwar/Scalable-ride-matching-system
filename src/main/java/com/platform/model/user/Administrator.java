package com.platform.model.user;

import com.platform.model.enums.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Platform super-user responsible for governance and operations.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "administrators")
@DiscriminatorValue("ADMIN")
public class Administrator extends User {

    @Column(nullable = false)
    private String adminId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccessLevel accessLevel = AccessLevel.SUPPORT_ADMIN;
}