package com.campusrental.service;

import com.campusrental.dto.ResponseDTO;
import com.campusrental.dto.TenantDTO;

import java.util.List;

public interface TenantService {

    TenantDTO getTenantById(String email);
    List<TenantDTO> getTenantsInProperty(Long propertyId);
    TenantDTO addTenantToProperty(Long propertyId, TenantDTO tenantDTO);
    void deleteTenant(String email);
    ResponseDTO moveTenantToProperty(String tenantId, Long newPropertyId);
}
