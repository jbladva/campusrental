package com.rental.agency.dto;

import lombok.Data;

@Data
public class CreateTenantGraphQlDto {
    private String name;
    private String email;
    private String phoneNumber;
}
