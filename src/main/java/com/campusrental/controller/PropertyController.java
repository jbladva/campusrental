package com.campusrental.controller;

import com.campusrental.dto.CreatePropertyDTO;
import com.campusrental.dto.PropertyDTO;
import com.campusrental.dto.ResponseDTO;
import com.campusrental.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    @Autowired
    PropertyController(PropertyService propertyService){
        this.propertyService = propertyService;
    }

    @GetMapping
    public ResponseEntity<List<PropertyDTO>> getAllProperties() {
        List<PropertyDTO> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/available")
    public ResponseEntity<ResponseDTO> getAvailableProperties() {
        return ResponseEntity.ok(propertyService.getAvailableProperties());
    }

    @GetMapping("/{propertyId}")
    public ResponseEntity<PropertyDTO> getPropertyWithTenants(@PathVariable Long propertyId) {
        PropertyDTO propertyWithTenants = propertyService.getPropertyByIdWithTenantCount(propertyId);
        return ResponseEntity.ok(propertyWithTenants);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> addProperty(@RequestBody CreatePropertyDTO propertyDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(propertyService.addProperty(propertyDTO));
    }

    @GetMapping("/total-rental-income")
    public ResponseEntity<Double> getTotalRentalIncome() {
        double totalRentalIncome = propertyService.getTotalRentalIncome();
        return ResponseEntity.ok(totalRentalIncome);
    }

    @DeleteMapping("/{propertyId}")
    public ResponseEntity<String> deleteProperty(@PathVariable Long propertyId) {
        propertyService.deleteProperty(propertyId);
        return ResponseEntity.ok("Property has been deleted");
    }

    @PatchMapping("/change-rental-rate/{propertyId}/{newRentalRate}")
    public ResponseEntity<String> changeRentalRate(
            @PathVariable Long propertyId,
            @PathVariable double newRentalRate) {
        propertyService.changeRentalRate(propertyId, newRentalRate);
        return ResponseEntity.ok("Rental has been updated");
    }
}
