package com.campusrental.service;

import com.campusrental.dto.*;

import java.util.List;

import com.campusrental.dto.PropertyDTO;

import javax.validation.Valid;
import java.util.List;

public interface PropertyService {

    List<PropertyDTO> getAllProperties();

    List<GraphQlPropertyDto> getAllGraphQlProperties();

    ResponseDTO getAvailableProperties();

    PropertyDTO getPropertyByIdWithTenantCount(Long id);

    double getTotalRentalIncome();

    ResponseDTO addProperty(@Valid CreatePropertyDTO propertyDTO);

    void deleteProperty(Long propertyId);
    void changeRentalRate(Long propertyId, double newRentalRate);

    List<TenantDTO> getTenantsInProperty(Long propertyId);
}
