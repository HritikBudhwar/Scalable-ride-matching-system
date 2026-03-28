package com.platform.model.user;

import com.platform.model.enums.AccessLevel;

/**
 * Administrator entity class
 */
public class Administrator extends User {
    private String adminCode;
    private AccessLevel accessLevel;
    private String department;
}