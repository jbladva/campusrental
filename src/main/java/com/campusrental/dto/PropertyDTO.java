package com.campusrental.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDTO {
    private Long id;
    private String address;
    private String eirCode;
    private int capacity;
    private double rentalCost;
    private List<TenantDTO> tenants;
}
