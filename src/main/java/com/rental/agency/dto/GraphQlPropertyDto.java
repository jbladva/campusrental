package com.rental.agency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphQlPropertyDto {
    private Long id;
    private String address;
    private String eirCode;
    private int capacity;
    private double rentalCost;
    private List<GraphQlTenantDto> tenants;
}
