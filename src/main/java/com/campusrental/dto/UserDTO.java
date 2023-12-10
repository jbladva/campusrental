package com.campusrental.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private boolean locked;
    private String role;
    private String phoneNumber;
    private String ppsn;
}
