package com.campusrental.dto;

import lombok.Data;

@Data
public class CreateTenantGraphQlDto {
    private String name;
    private String email;
    private String phoneNumber;
}
