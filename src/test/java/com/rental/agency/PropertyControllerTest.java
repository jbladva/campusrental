package com.rental.agency;

import com.rental.agency.controller.PropertyController;
import com.rental.agency.controller.TenantController;
import com.rental.agency.dto.PropertyDTO;
import com.rental.agency.dto.TenantDTO;
import com.rental.agency.exception.PropertyNotFoundException;
import com.rental.agency.service.PropertyService;
import com.rental.agency.service.TenantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class PropertyControllerTest {

    @Mock
    private PropertyService propertyService;
    @Mock
    private TenantService tenantService;
    @InjectMocks
    private TenantController tenantController;
    @InjectMocks
    private PropertyController propertyController;


    @Test
    public void testGetPropertyById_ExistingId() {
        PropertyDTO mockPropertyDTO = new PropertyDTO();
        mockPropertyDTO.setId(1L);
        mockPropertyDTO.setAddress("123 Sample St");
        mockPropertyDTO.setEirCode("E12345");
        mockPropertyDTO.setCapacity(5);
        mockPropertyDTO.setRentalCost(1200.0);

        TenantDTO tenant1 = new TenantDTO();
        tenant1.setName("John Doe");
        tenant1.setEmail("john@example.com");
        tenant1.setPhoneNumber("123-456-7890");

        TenantDTO tenant2 = new TenantDTO();
        tenant2.setName("Jane Doe");
        tenant2.setEmail("jane@example.com");
        tenant2.setPhoneNumber("987-654-3210");

        mockPropertyDTO.setTenants(Arrays.asList(tenant1, tenant2));

        Mockito.when(propertyService.getPropertyByIdWithTenantCount(anyLong())).thenReturn(mockPropertyDTO);
        ResponseEntity<PropertyDTO> responseEntity = propertyController.getPropertyWithTenants(1L);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(mockPropertyDTO, responseEntity.getBody());
    }

    @Test
    public void testGetPropertyById_NonExistingId() {
        Mockito.when(propertyService.getPropertyByIdWithTenantCount(anyLong())).thenThrow(new PropertyNotFoundException("Property not found"));
        PropertyNotFoundException exception = assertThrows(PropertyNotFoundException.class, () -> {
            propertyController.getPropertyWithTenants(2L);
        });
        assertEquals("Property not found", exception.getMessage());
    }

    @Test
    public void testDeleteTenantById_AuthenticatedAndAuthorized() {
        Mockito.doNothing().when(tenantService).deleteTenant("tenantId");
        ResponseEntity<String> responseEntity = tenantController.deleteTenant("tenantId");
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Tenant successfully deleted", responseEntity.getBody());
    }

    @Test
    public void testDeletePropertyById_AuthenticatedNotAuthorized() {
        Mockito.doThrow(new AccessDeniedException("Not authorized")).when(propertyService).deleteProperty(1L);
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> propertyController.deleteProperty(1L));
        assertEquals("Not authorized", exception.getMessage());
    }

    @Test
    public void testDeleteTenantById_NotAuthenticated() {
        Mockito.doThrow(new AuthenticationCredentialsNotFoundException("Not authenticated")).when(tenantService).deleteTenant("test@gmail.com");
        AuthenticationCredentialsNotFoundException exception = assertThrows(AuthenticationCredentialsNotFoundException.class, () -> tenantController.deleteTenant("test@gmail.com"));
        assertEquals("Not authenticated", exception.getMessage());
    }
}
