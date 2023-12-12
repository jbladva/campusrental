package com.rental.agency.service;

import com.rental.agency.dto.ResponseDTO;
import com.rental.agency.dto.TenantDTO;

import java.util.List;

public interface TenantService {

    TenantDTO getTenantById(String email);
    List<TenantDTO> getTenantsInProperty(Long propertyId);
    TenantDTO addTenantToProperty(Long propertyId, TenantDTO tenantDTO);
    void deleteTenant(String email);
    ResponseDTO moveTenantToProperty(String tenantId, Long newPropertyId);
}
