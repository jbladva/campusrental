package com.campusrental;

import com.campusrental.controller.PropertyController;
import com.campusrental.dto.PropertyDTO;
import com.campusrental.dto.TenantDTO;
import com.campusrental.entity.Property;
import com.campusrental.entity.Tenant;
import com.campusrental.repository.PropertyRepository;
import com.campusrental.service.PropertyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CampusrentalApplicationTests {

	@Autowired
	private PropertyService propertyService;

	@MockBean
	private PropertyRepository propertyRepository;

	@Autowired
	private ModelMapper modelMapper;

	@InjectMocks
	private PropertyController propertyController;

	@Test
	void contextLoads() {
	}

	/*@Test
	void getPropertyByIdWithExistingId() {
		long existingId = 1L;
		PropertyDTO mockPropertyDto = new PropertyDTO();
		Property mockProperty = Property.builder().id(1l).address("test address").eirCode("A65F4E2").capacity(5).rentalCost(5000).tenants(List.of(Tenant.builder().name("test").email("test@gmail.com").phoneNumber("1234567890").build())).build();
		when(propertyRepository.findById(existingId)).thenReturn(Optional.ofNullable(mockProperty));
		assertEquals(mockPropertyDto, propertyService.getPropertyByIdWithTenantCount(existingId));
	}*/
	/*@Test
	void getPropertyByIdWithExistingId() {
		long existingId = 1L;
		PropertyDTO mockPropertyDTO = new PropertyDTO();
		when(propertyService.getPropertyByIdWithTenantCount(existingId)).thenReturn(mockPropertyDTO);

		ResponseEntity<PropertyDTO> responseEntity = propertyController.getPropertyWithTenants(existingId);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(mockPropertyDTO, responseEntity.getBody());

		verify(propertyService, times(1)).getPropertyByIdWithTenantCount(existingId);
	}*/


}
