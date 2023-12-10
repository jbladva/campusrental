package com.campusrental.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyDTO {
    private Long id;
    private String address;
    private String eirCode;
    private int capacity;
    private double rentalCost;
    private long numberOfTenant;
    @JsonIgnore
    private List<TenantDTO> tenants;
}
