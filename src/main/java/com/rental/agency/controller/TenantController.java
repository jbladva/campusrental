package com.rental.agency.controller;

import com.rental.agency.dto.ResponseDTO;
import com.rental.agency.dto.TenantDTO;
import com.rental.agency.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The type Tenant controller.
 */
@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    /**
     * Instantiates a new Tenant controller.
     *
     * @param tenantService the tenant service
     */
    @Autowired
    public TenantController(TenantService tenantService){
        this.tenantService = tenantService;
    }

    /**
     * Gets tenant by id.
     * localhost:8081/api/tenants/johendowe@exasmple.com
     * @param email the email
     * @return the tenant by id
     */
    @GetMapping("/{email}")
    public ResponseEntity<TenantDTO> getTenantById(@PathVariable String email) {
        return ResponseEntity.ok(tenantService.getTenantById(email));
    }

    /**
     * Gets tenants in property.
     * localhost:8081/api/tenants/property/1
     * @param propertyId the property id
     * @return the tenants in property
     */
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<TenantDTO>> getTenantsInProperty(@PathVariable Long propertyId) {
        return ResponseEntity.ok(tenantService.getTenantsInProperty(propertyId));
    }


    /**
     * Add new tenant to property response entity.
     * localhost:8081/api/tenants/add/1
     * {
     *     "name": "Jane Doe",
     *     "email": "s@example.com",
     *     "phoneNumber": "9800543210"
     * }
     * @param propertyId the property id
     * @param tenantDTO  the tenant dto
     * @return the response entity
     */
    @PostMapping("/add/{propertyId}")
    public ResponseEntity<TenantDTO> addNewTenantToProperty(
            @PathVariable Long propertyId,
            @RequestBody TenantDTO tenantDTO) {
        return ResponseEntity.ok(tenantService.addTenantToProperty(propertyId, tenantDTO));
    }

    /**
     * Move tenant to property response entity.
     * localhost:8081/api/tenants/move-to-property/johndoe@exasmple.com/2
     * @param tenantId      the tenant id
     * @param newPropertyId the new property id
     * @return the response entity
     */
    @PatchMapping("/move-to-property/{tenantId}/{newPropertyId}")
    public ResponseEntity<ResponseDTO> moveTenantToProperty(
            @PathVariable String tenantId,
            @PathVariable Long newPropertyId) {
        return ResponseEntity.ok(tenantService.moveTenantToProperty(tenantId, newPropertyId));
    }

    /**
     * Delete tenant response entity.
     * localhost:8081/api/tenants/johndoe@exasmple.com
     * @param email the email
     * @return the response entity
     */
    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteTenant(@PathVariable String email) {
        tenantService.deleteTenant(email);
        return ResponseEntity.ok("Tenant successfully deleted");
    }
}
