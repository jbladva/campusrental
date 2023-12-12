package com.rental.agency.service;


import java.util.List;

import com.rental.agency.dto.*;

import javax.validation.Valid;

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
