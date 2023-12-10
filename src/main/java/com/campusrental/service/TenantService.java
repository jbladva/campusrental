package com.campusrental.service;

import com.campusrental.dto.ResponseDTO;
import com.campusrental.dto.TenantDTO;

import java.util.List;

public interface TenantService {

    TenantDTO getTenantById(String id);
    List<TenantDTO> getTenantsInProperty(Long propertyId);
    ResponseDTO addTenant(TenantDTO tenantDTO);
    TenantDTO addTenantToProperty(Long propertyId, TenantDTO tenantDTO);
    void deleteTenant(String tenantId);
    ResponseDTO moveTenantToProperty(String tenantId, Long newPropertyId);
}
