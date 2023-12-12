package com.campusrental.controller;
import com.campusrental.dto.*;
import com.campusrental.exception.CapacityExceededException;
import com.campusrental.exception.PropertyNotFoundException;
import com.campusrental.exception.PropertyValidationException;
import com.campusrental.service.PropertyService;
import com.campusrental.service.TenantService;
import graphql.GraphQLException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GraphQlController {

    private  PropertyService propertyService;
    private TenantService tenantService;

    @Autowired
    public GraphQlController(PropertyService propertyService, TenantService tenantService)
    {
        this.propertyService=propertyService;
        this.tenantService=tenantService;
    }

    @QueryMapping("allProperties")
    public List<GraphQlPropertyDto> allGetProperties()
    {
        return propertyService.getAllGraphQlProperties();
    }

    @QueryMapping("getPropertyById")
    @PreAuthorize("@graphQlPermission.hasManagerRole()")
    public GraphQlPropertyDto getPropertyById(@Argument int id )
    {
        return new ModelMapper().map(propertyService.getPropertyByIdWithTenantCount((long) id),GraphQlPropertyDto.class);
    }

    @MutationMapping("addTenant")
    @PreAuthorize("@graphQlPermission.hasManagerRole() or @graphQlPermission.hasOfficeStaffRole()")
    public GraphQlTenantDto addTenant(@Argument int id, @Argument CreateTenantGraphQlDto input)
    {
            return new ModelMapper().map(tenantService.addTenantToProperty(Long.valueOf(id), new ModelMapper().map(input, TenantDTO.class)), GraphQlTenantDto.class);

    }

    @MutationMapping("deleteProperty")
    @PreAuthorize("@graphQlPermission.hasManagerRole()")
    public String deletePropertyById(@Argument int id)
    {
        propertyService.deleteProperty(Long.valueOf(id));
        return "Property successfully deleted";
    }
}
