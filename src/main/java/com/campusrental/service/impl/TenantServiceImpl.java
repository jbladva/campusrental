package com.campusrental.service.impl;

import com.campusrental.dto.ResponseDTO;
import com.campusrental.dto.TenantDTO;
import com.campusrental.entity.Property;
import com.campusrental.entity.Tenant;
import com.campusrental.exception.CapacityExceededException;
import com.campusrental.exception.PropertyNotFoundException;
import com.campusrental.exception.PropertyValidationException;
import com.campusrental.exception.TenantNotFoundException;
import com.campusrental.repository.PropertyRepository;
import com.campusrental.repository.TenantRepository;
import com.campusrental.service.TenantService;
import com.campusrental.util.Common;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;
    private final PropertyRepository propertyRepository;
    private final ModelMapper modelMapper;

    @Autowired
    TenantServiceImpl(TenantRepository tenantRepository, PropertyRepository propertyRepository, ModelMapper modelMapper){
        this.tenantRepository = tenantRepository;
        this.propertyRepository = propertyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TenantDTO getTenantById(String email) {
        Tenant tenant = tenantRepository.findByEmail(email).orElseThrow(()->new TenantNotFoundException("Tenant is not available"));
        return modelMapper.map(tenant,TenantDTO.class);
    }

    @Override
    public List<TenantDTO> getTenantsInProperty(Long propertyId) {
        Optional<Property> propertyOptional = propertyRepository.findById(propertyId);
        if (propertyOptional.isPresent()) {
            Property property = propertyOptional.get();
            List<Tenant> tenants = property.getTenants();
            if(ObjectUtils.isEmpty(tenants)){
                throw new TenantNotFoundException("Tenant not found for this property "+propertyId);
            }
            return tenants.stream()
                    .map(tenant -> modelMapper.map(tenant, TenantDTO.class))
                    .collect(Collectors.toList());
        } else {
            throw new PropertyNotFoundException("Property is not available");
        }
    }



    @Override
    public TenantDTO addTenantToProperty(Long propertyId, TenantDTO tenantDTO)  {

        if (ObjectUtils.isEmpty(tenantDTO.getEmail()) || ObjectUtils.isEmpty(tenantDTO.getPhoneNumber()))
            throw new PropertyValidationException("Tenant email and mobile number should not be empty");
        if (Boolean.FALSE.equals(Common.isValidMobileNo(tenantDTO.getPhoneNumber())))
            throw new PropertyValidationException("Please enter a valid mobile number");
        if (Boolean.FALSE.equals(Common.isValidEmail(tenantDTO.getEmail())))
            throw new PropertyValidationException("Please enter a valid email address");

        tenantRepository.findByEmailOrPhoneNumber(tenantDTO.getEmail(), tenantDTO.getPhoneNumber())
                .ifPresent((tenant -> {
                    throw new PropertyNotFoundException("Tenant already exists with email or mobile");}));
        Optional<Property> propertyOptional = propertyRepository.findById(propertyId);

        if (propertyOptional.isPresent()) {
            Property property = propertyOptional.get();
            if (property.getTenants().size() < property.getCapacity()) {
                Tenant tenant = modelMapper.map(tenantDTO, Tenant.class);
                tenant.setCreatedDate(LocalDate.now());
                tenant.setUpdatedDate(LocalDate.now());
                tenant.setProperty(property);
                Tenant savedTenant = tenantRepository.save(tenant);
                return modelMapper.map(savedTenant, TenantDTO.class);
            } else {
                throw new CapacityExceededException("property capacity is exceeded");
            }
        } else {
            throw new PropertyNotFoundException("Property not found");
        }
    }

    @Override
    @Transactional
    public void deleteTenant(String email) {
        if(!tenantRepository.existsByEmail(email))
            throw new PropertyValidationException("Tenant is not available.");
        tenantRepository.deleteByEmail(email);
    }

    @Override
    public ResponseDTO moveTenantToProperty(String tenantId, Long newPropertyId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found for email :" + tenantId));
        Property property = propertyRepository.findById(newPropertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Property not found"));
        if (property.getTenants().size() < property.getCapacity()) {
            tenant.setProperty(property);
            tenantRepository.save(tenant);
            return ResponseDTO.builder().data("Tenant has been moved").build();
        }
        throw new CapacityExceededException("property capacity is exceeded");
    }

}
