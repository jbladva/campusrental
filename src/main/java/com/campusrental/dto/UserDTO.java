package com.campusrental.dto;

import lombok.Data;

import javax.management.relation.Role;

@Data
public class UserDTO {

    private String email;
    private String password;
    private boolean locked;
    private RoleDto role;
    private String phoneNumber;
    private String ppsn;
}
