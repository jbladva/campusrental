package com.rental.agency.controller;

import com.rental.agency.dto.CreatePropertyDTO;
import com.rental.agency.dto.PropertyDTO;
import com.rental.agency.dto.ResponseDTO;
import com.rental.agency.service.PropertyService;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * The type Property controller.
 */
@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    /**
     * Instantiates a new Property controller.
     *
     * @param propertyService the property service
     */
    @Autowired
    PropertyController(PropertyService propertyService){
        this.propertyService = propertyService;
    }

    /**
     * Gets all properties.
     * localhost:8081/api/properties/get
     * @return the all properties
     */
    @GetMapping("/get")
    public ResponseEntity<List<PropertyDTO>> getAllProperties() {
        List<PropertyDTO> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }

    /**
     * Gets available properties.
     * localhost:8081/api/properties/available
     * @return the available properties
     */
    @GetMapping("/available")
    public ResponseEntity<ResponseDTO> getAvailableProperties() {
        return ResponseEntity.ok(propertyService.getAvailableProperties());
    }

    /**
     * Gets property with tenants.
     * localhost:8081/api/properties/id/1
     * @param propertyId the property id
     * @return the property with tenants
     */
    @GetMapping("/id/{propertyId}")
    public ResponseEntity<PropertyDTO> getPropertyWithTenants(@PathVariable Long propertyId) {
        PropertyDTO propertyWithTenants = propertyService.getPropertyByIdWithTenantCount(propertyId);
        return ResponseEntity.ok(propertyWithTenants);
    }

    /**
     * Add property response entity.
     * localhost:8081/api/properties/add
     * {
     *     {
     *   "address": "12wws3 Mainw Street tesdt",
     *   "eirCode": "D120C44",
     *   "capacity": 5,
     *   "rentalCost": 1200.00,
     *   "tenants": [
     *     {
     *       "name": "John Doe",
     *       "email": "johendowe@exasmple.com",
     *       "phoneNumber": "1234007890"
     *     },
     *     {
     *       "name": "Jane Doe",
     *       "email": "janesdoe1@rtexwample.com",
     *       "phoneNumber": "9806040210"
     *     }
     *   ]
     * }
     * }
     * @param propertyDTO the property dto
     * @return the response entity
     */
    @PostMapping("/add")
    public ResponseEntity<ResponseDTO> addProperty(@RequestBody CreatePropertyDTO propertyDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(propertyService.addProperty(propertyDTO));
        }
        catch (ResponseStatusException exception)
        {
            throw  new ResponseStatusException(exception.getStatus(), exception.getMessage());
        }
    }

    /**
     * Gets total rental income.
     * localhost:8081/api/properties/total-rental-income
     * @return the total rental income
     */
    @GetMapping("/total-rental-income")
    public ResponseEntity<Double> getTotalRentalIncome() {
        double totalRentalIncome = propertyService.getTotalRentalIncome();
        return ResponseEntity.ok(totalRentalIncome);
    }

    /**
     * Delete property response entity.
     * localhost:8081/api/properties/delete/1
     * @param propertyId the property id
     * @return the response entity
     */
    @DeleteMapping("/delete/{propertyId}")
    public ResponseEntity<String> deleteProperty(@PathVariable Long propertyId) {
        propertyService.deleteProperty(propertyId);
        return ResponseEntity.ok("Property has been deleted");
    }

    /**
     * Change rental rate response entity.
     * localhost:8081/api/properties/change-rental-rate/2/2300
     * @param propertyId    the property id
     * @param newRentalRate the new rental rate
     * @return the response entity
     */
    @PatchMapping("/change-rental-rate/{propertyId}/{newRentalRate}")
    public ResponseEntity<String> changeRentalRate(
            @PathVariable Long propertyId,
            @PathVariable double newRentalRate) {
        propertyService.changeRentalRate(propertyId, newRentalRate);
        return ResponseEntity.ok("Rental has been updated");
    }
}
