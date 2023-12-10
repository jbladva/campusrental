package com.campusrental.service.impl;
import com.campusrental.dto.CreatePropertyDTO;
import com.campusrental.dto.PropertyDTO;
import com.campusrental.dto.ResponseDTO;
import com.campusrental.dto.TenantDTO;
import com.campusrental.entity.Property;
import com.campusrental.entity.Tenant;
import com.campusrental.exception.PropertyNotFoundException;
import com.campusrental.repository.PropertyRepository;
import com.campusrental.repository.TenantRepository;
import com.campusrental.service.PropertyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final TenantRepository tenantRepository;
    private final ModelMapper modelMapper;

    @Autowired
    PropertyServiceImpl(PropertyRepository propertyRepository, TenantRepository tenantRepository, ModelMapper modelMapper){
        this.propertyRepository = propertyRepository;
        this.tenantRepository = tenantRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PropertyDTO> getAllProperties() {
        List<Property> properties = propertyRepository.findAll();
        if(ObjectUtils.isEmpty(properties)){
            throw new PropertyNotFoundException("Properties are not available");
        }
        return properties.stream()
                .map(property -> modelMapper.map(property, PropertyDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseDTO getAvailableProperties() {
        List<Property> properties = propertyRepository.findAvailableProperties();
        if(ObjectUtils.isEmpty(properties)){
            return ResponseDTO.builder().data("No properties are available").build();
        }
        return ResponseDTO.builder().data(properties.stream()
                .map(property -> modelMapper.map(property, PropertyDTO.class))
                .collect(Collectors.toList())).build();
    }

    @Override
    public PropertyDTO getPropertyByIdWithTenantCount(Long id) {
        Optional<Property> propertyOptional = propertyRepository.findById(id);
        if (propertyOptional.isPresent()) {
            Property property = propertyOptional.get();
            PropertyDTO propertyDTO = modelMapper.map(property, PropertyDTO.class);
            propertyDTO.setNumberOfTenant(property.getTenants().size());
            return propertyDTO;
        } else {
            throw new PropertyNotFoundException("Property is not available");
        }
    }

    @Override
    public double getTotalRentalIncome() {
        List<Property> occupiedProperties = propertyRepository.findByTenantsIsNotEmpty();
        if(ObjectUtils.isEmpty(occupiedProperties)){
            throw new PropertyNotFoundException("Properties are not available");
        }
        return occupiedProperties.stream()
                .mapToDouble(property -> property.getRentalCost() * property.getTenants().size())
                .sum();
    }

    @Override
    @Transactional
    public ResponseDTO addProperty(CreatePropertyDTO propertyDTO) {
        validateProperty(propertyDTO);
        Property propertyEntity = modelMapper.map(propertyDTO, Property.class);
        if (!ObjectUtils.isEmpty(propertyEntity.getTenants())){
            propertyEntity.getTenants().forEach(tenant -> tenant.setProperty(propertyEntity));
        }
        propertyRepository.save(propertyEntity);
        return ResponseDTO.builder().data("Property successfully added").build();
    }

    @Override
    @Transactional
    public void deleteProperty(Long id) {
        Optional<Property> propertyOptional = propertyRepository.findById(id);
        propertyOptional.ifPresent(property -> {
            propertyRepository.deleteById(id);
        });
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

    private void validateProperty(CreatePropertyDTO propertyDto){
        if(ObjectUtils.isEmpty(propertyDto)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Property can not be null");
        }
        if(ObjectUtils.isEmpty(propertyDto.getAddress()) || ObjectUtils.isEmpty(propertyDto.getEirCode())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address or Eircode can not be null");
        }
        if (!ObjectUtils.isEmpty(propertyDto.getTenants())){
            propertyDto.getTenants().forEach(tenantDTO -> {
                if(ObjectUtils.isEmpty(tenantDTO.getEmail()) || ObjectUtils.isEmpty(tenantDTO.getPhoneNumber())){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tenant email or mobile number should not be null");
                }
                tenantRepository.findByEmailOrPhoneNumber(tenantDTO.getEmail(),tenantDTO.getPhoneNumber())
                        .ifPresent( (property) -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            String.format("Tenant with this email :%s and phone number :%s is already exists",
                                    tenantDTO.getEmail(),tenantDTO.getPhoneNumber()));
                });
            });
        }
        propertyRepository.findByEirCodeOrAddress(propertyDto.getEirCode(), propertyDto.getAddress()).ifPresent( (property) -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Property is already exists");
        });
    }
}
