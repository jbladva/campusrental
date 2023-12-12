package com.rental.agency.dto;

import lombok.Data;

@Data
public class UserDTO {

    private String email;
    private String password;
    private boolean locked;
    private RoleDto role;
    private String phoneNumber;
    private String ppsn;
}
