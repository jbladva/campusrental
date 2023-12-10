package com.campusrental.controller;

import com.campusrental.dto.ResponseDTO;
import com.campusrental.dto.TenantDTO;
import com.campusrental.service.TenantService;
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

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    @Autowired
    TenantController(TenantService tenantService){
        this.tenantService = tenantService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantDTO> getTenantById(@PathVariable String id) {
        return ResponseEntity.ok(tenantService.getTenantById(id));
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<TenantDTO>> getTenantsInProperty(@PathVariable Long propertyId) {
        return ResponseEntity.ok(tenantService.getTenantsInProperty(propertyId));
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseDTO> addNewTenant(@RequestBody TenantDTO tenantDTO) {
        return ResponseEntity.ok(tenantService.addTenant(tenantDTO));
    }

    @PostMapping("/add-to-property/{propertyId}")
    public ResponseEntity<TenantDTO> addNewTenantToProperty(
            @PathVariable Long propertyId,
            @RequestBody TenantDTO tenantDTO) {
        return ResponseEntity.ok(tenantService.addTenantToProperty(propertyId, tenantDTO));
    }

    @PatchMapping("/move-to-property/{tenantId}/{newPropertyId}")
    public ResponseEntity<ResponseDTO> moveTenantToProperty(
            @PathVariable String tenantId,
            @PathVariable Long newPropertyId) {
        return ResponseEntity.ok(tenantService.moveTenantToProperty(tenantId, newPropertyId));
    }

    @DeleteMapping("/{tenantId}")
    public ResponseEntity<String> deleteTenant(@PathVariable String tenantId) {
        tenantService.deleteTenant(tenantId);
        return ResponseEntity.ok("Tenant successfully deleted");
    }
}
