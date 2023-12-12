package com.rental.agency.controller;
import com.rental.agency.dto.TenantDTO;
import com.rental.agency.service.PropertyService;
import com.rental.agency.service.TenantService;
import com.rental.agency.dto.CreateTenantGraphQlDto;
import com.rental.agency.dto.GraphQlPropertyDto;
import com.rental.agency.dto.GraphQlTenantDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * The type Graph ql controller.
 */
@Controller
public class GraphQlController {

    private PropertyService propertyService;
    private TenantService tenantService;

    /**
     * Instantiates a new Graph ql controller.
     *
     * @param propertyService the property service
     * @param tenantService   the tenant service
     */
    @Autowired
    public GraphQlController(PropertyService propertyService, TenantService tenantService)
    {
        this.propertyService=propertyService;
        this.tenantService=tenantService;
    }

    /**
     * All get properties list.
     *
     * @return the list
     */
    @QueryMapping("allProperties")
    public List<GraphQlPropertyDto> allGetProperties()
    {
        return propertyService.getAllGraphQlProperties();
    }

    /**
     * Gets property by id.
     *
     * @param id the id
     * @return the property by id
     */
    @QueryMapping("getPropertyById")
    @PreAuthorize("@graphQlPermission.hasManagerRole()")
    public GraphQlPropertyDto getPropertyById(@Argument int id )
    {
        return new ModelMapper().map(propertyService.getPropertyByIdWithTenantCount((long) id),GraphQlPropertyDto.class);
    }

    /**
     * Add tenant graph ql tenant dto.
     *
     * @param id    the id
     * @param input the input
     * @return the graph ql tenant dto
     */
    @MutationMapping("addTenant")
    @PreAuthorize("@graphQlPermission.hasManagerRole() or @graphQlPermission.hasOfficeStaffRole()")
    public GraphQlTenantDto addTenant(@Argument int id, @Argument CreateTenantGraphQlDto input)
    {
            return new ModelMapper().map(tenantService.addTenantToProperty(Long.valueOf(id), new ModelMapper().map(input, TenantDTO.class)), GraphQlTenantDto.class);

    }

    /**
     * Delete property by id string.
     *
     * @param id the id
     * @return the string
     */
    @MutationMapping("deleteProperty")
    @PreAuthorize("@graphQlPermission.hasManagerRole()")
    public String deletePropertyById(@Argument int id)
    {
        propertyService.deleteProperty(Long.valueOf(id));
        return "Property successfully deleted";
    }
}
