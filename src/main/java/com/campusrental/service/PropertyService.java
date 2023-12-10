package com.campusrental.service;

import com.campusrental.dto.CreatePropertyDTO;
import com.campusrental.dto.PropertyDTO;

import java.util.List;

import com.campusrental.dto.PropertyDTO;
import com.campusrental.dto.ResponseDTO;
import com.campusrental.dto.TenantDTO;

import javax.validation.Valid;
import java.util.List;

public interface PropertyService {

    List<PropertyDTO> getAllProperties();

    ResponseDTO getAvailableProperties();

    PropertyDTO getPropertyByIdWithTenantCount(Long id);

    double getTotalRentalIncome();

    ResponseDTO addProperty(@Valid CreatePropertyDTO propertyDTO);

    void deleteProperty(Long propertyId);
    void changeRentalRate(Long propertyId, double newRentalRate);

    List<TenantDTO> getTenantsInProperty(Long propertyId);
}
