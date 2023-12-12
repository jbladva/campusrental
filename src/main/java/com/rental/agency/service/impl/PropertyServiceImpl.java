package com.rental.agency.service.impl;

import com.rental.agency.dto.*;
import com.rental.agency.entity.Property;
import com.rental.agency.entity.Tenant;
import com.rental.agency.exception.PropertyNotFoundException;
import com.rental.agency.exception.PropertyValidationException;
import com.rental.agency.repository.PropertyRepository;
import com.rental.agency.repository.TenantRepository;
import com.rental.agency.service.PropertyService;
import com.rental.agency.util.Common;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final TenantRepository tenantRepository;
    private final ModelMapper modelMapper;

    @Autowired
    PropertyServiceImpl(PropertyRepository propertyRepository, TenantRepository tenantRepository, ModelMapper modelMapper) {
        this.propertyRepository = propertyRepository;
        this.tenantRepository = tenantRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PropertyDTO> getAllProperties() {
        List<Property> properties = propertyRepository.findAll();
        if (ObjectUtils.isEmpty(properties)) {
            throw new PropertyNotFoundException("Properties are not available");
        }
        List<PropertyDTO> propertyDTOS= properties.stream()
                .map(property -> {PropertyDTO propertyDTO = modelMapper.map(property, PropertyDTO.class);
                propertyDTO.setTenants(null);
                return propertyDTO;
                }).collect(Collectors.toList());
        return propertyDTOS;
    }

    @Override
    public List<GraphQlPropertyDto> getAllGraphQlProperties() {
        List<Property> properties = propertyRepository.findAll();
        if (ObjectUtils.isEmpty(properties)) {
            throw new PropertyNotFoundException("Properties are not available");
        }
        List<GraphQlPropertyDto> propertyDTOS= properties.stream()
                .map(property -> {GraphQlPropertyDto propertyDTO = modelMapper.map(property, GraphQlPropertyDto.class);
                    return propertyDTO;
                }).collect(Collectors.toList());
        return propertyDTOS;
    }

    @Override
    public ResponseDTO getAvailableProperties() {
        List<Property> properties = propertyRepository.findAvailableProperties();
        if (ObjectUtils.isEmpty(properties)) {
            return ResponseDTO.builder().data("No properties are available").build();
        }
        List<PropertyDTO> propertyDTOList =properties.stream()
                .map(property -> {PropertyDTO propertyDTO = modelMapper.map(property, PropertyDTO.class);
                    propertyDTO.setTenants(null);
                    return propertyDTO;}).collect(Collectors.toList());
        return ResponseDTO.builder().data(propertyDTOList).build();
    }

    @Override
    public PropertyDTO getPropertyByIdWithTenantCount(Long id) {
        Optional<Property> propertyOptional = propertyRepository.findById(id);
        if (propertyOptional.isPresent()) {
            Property property = propertyOptional.get();
            PropertyDTO propertyDTO = modelMapper.map(property, PropertyDTO.class);
            return propertyDTO;
        } else {
            throw new PropertyNotFoundException("Property is not available");
        }
    }

    @Override
    public double getTotalRentalIncome() {
        List<Property> occupiedProperties = propertyRepository.findByTenantsIsNotEmpty();
        if (ObjectUtils.isEmpty(occupiedProperties)) {
            throw new PropertyNotFoundException("Properties are not available");
        }
        return occupiedProperties.stream()
                .mapToDouble(property -> property.getRentalCost() * property.getTenants().size())
                .sum();
    }

    @Override
    @Transactional
    public ResponseDTO addProperty(CreatePropertyDTO propertyDTO) throws ResponseStatusException {
        validateProperty(propertyDTO);
        Property propertyEntity = modelMapper.map(propertyDTO, Property.class);
        if (!ObjectUtils.isEmpty(propertyEntity.getTenants())) {
            propertyEntity.getTenants().forEach(tenant -> {
                tenant.setProperty(propertyEntity);
                tenant.setCreatedDate(LocalDate.now());
                tenant.setUpdatedDate(LocalDate.now());
            });
        }
        propertyEntity.setCreatedDate(LocalDate.now());
        propertyEntity.setUpdatedDate(LocalDate.now());
        propertyRepository.save(propertyEntity);
        return ResponseDTO.builder().data("Property successfully added").build();
    }

    @Override
    @Transactional
    public void deleteProperty(Long id) {
        Property property = propertyRepository.findById(id).orElseThrow(()-> new PropertyNotFoundException("property is not available "));
        propertyRepository.deleteById(id);
    }

    @Override
    public void changeRentalRate(Long propertyId, double newRentalRate) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Properties are not available"));
        property.setRentalCost(newRentalRate);
        propertyRepository.save(property);
    }

    @Override
    public List<TenantDTO> getTenantsInProperty(Long propertyId) {
        Optional<Property> propertyOptional = propertyRepository.findById(propertyId);
        if (propertyOptional.isPresent()) {
            Property property = propertyOptional.get();
            List<Tenant> tenants = property.getTenants();
            return tenants.stream()
                    .map(tenant -> modelMapper.map(tenant, TenantDTO.class))
                    .collect(Collectors.toList());
        } else {
            // Handle property not found
            return null;
        }
    }

    private void validateProperty(CreatePropertyDTO propertyDto) throws ResponseStatusException {

        if (ObjectUtils.isEmpty(propertyDto))
            throw new PropertyValidationException("Property details cannot be empty");
        if (ObjectUtils.isEmpty(propertyDto.getAddress()) || ObjectUtils.isEmpty(propertyDto.getEirCode()))
            throw new PropertyValidationException("Address and EirCode are required");
        if (Boolean.FALSE.equals(Common.isValidEirCode(propertyDto.getEirCode())))
            throw new PropertyValidationException("Please enter a valid EirCode");

        propertyRepository.findByEirCodeOrAddress(propertyDto.getEirCode(), propertyDto.getAddress()).ifPresent((property) -> {
            throw new PropertyValidationException("Property with the same EirCode or Address already exists");
        });

        if (!ObjectUtils.isEmpty(propertyDto.getTenants())) {
            Set<String> emails = new HashSet<>();
            Set<String> mobileNumbers = new HashSet<>();

            propertyDto.getTenants().forEach(tenantDTO -> {

                if (ObjectUtils.isEmpty(tenantDTO.getEmail()) || ObjectUtils.isEmpty(tenantDTO.getPhoneNumber()))
                    throw new PropertyValidationException("Tenant email and mobile number should not be empty");
                if (Boolean.FALSE.equals(Common.isValidMobileNo(tenantDTO.getPhoneNumber())))
                    throw new PropertyValidationException("Please enter a valid mobile number");
                if (Boolean.FALSE.equals(Common.isValidEmail(tenantDTO.getEmail())))
                    throw new PropertyValidationException("Please enter a valid email address");
                tenantRepository.findByEmailOrPhoneNumber(tenantDTO.getEmail(), tenantDTO.getPhoneNumber())
                        .ifPresent((property) -> {
                            throw new PropertyValidationException("Email or PhoneNumber already exists");
                        });
                emails.add(tenantDTO.getEmail());
                mobileNumbers.add(tenantDTO.getPhoneNumber());

            });
            if (emails.size()< propertyDto.getTenants().size() || mobileNumbers.size() < propertyDto.getTenants().size())
            {
                throw new PropertyValidationException("Yoo cannot use the same Email or PhoneNumber for two different Tenants");
            }
        }
    }


}
