package com.rental.agency;

import com.rental.agency.entity.Property;
import com.rental.agency.entity.Tenant;
import com.rental.agency.entity.User;
import com.rental.agency.entity.UserRoles;
import com.rental.agency.repository.PropertyRepository;
import com.rental.agency.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class RentalAgencyApplication{

	public static void main(String[] args) {
		SpringApplication.run(RentalAgencyApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PropertyRepository propertyRepository;

	@PostConstruct
    public void addUserDemo(){
		User user = new User();
		user.setEmail("test@gmail.com");
		user.setPassword(passwordEncoder.encode("test"));
		user.setPhoneNumber("123456789");
		user.setPpsn("ujhgujg");
		user.setLocked(false);
		UserRoles userRoles  = new UserRoles();
		userRoles.setRoleName("MANAGER");
		user.setRole(userRoles);

		userRepository.save(user);

		User user1 = new User();
		user1.setEmail("office@gmail.com");
		user1.setPassword(passwordEncoder.encode("test123"));
		user1.setPhoneNumber("123456789");		user.setPpsn("ujhgujg");
		user1.setLocked(false);
		UserRoles userRoles1  = new UserRoles();
		userRoles1.setRoleName("OFFICE_STAFF");
		user1.setRole(userRoles1);
		userRepository.save(user1);
	}
	@PostConstruct
	public void createProperty() {

		// Create and save 5 properties
		for (int i = 1; i <= 5; i++) {
			Property property = Property.builder()
					.address("Property Address " + i)
					.eirCode("EIR" + i)
					.capacity(10)
					.rentalCost(1000.0 * i)
					.createdDate(LocalDate.now())
					.updatedDate(LocalDate.now())
					.build();

			List<Tenant> tenants= new ArrayList<>();
			for (int j = 1; j <= 4; j++) {
				Tenant tenant = Tenant.builder()
						.name("Tenant " + j)
						.email("tenant" +j+i + "@example.com")
						.phoneNumber("12345678" + i + j)
						.createdDate(LocalDate.now())
						.updatedDate(LocalDate.now())
						.build();
				tenant.setProperty(property);
				tenants.add(tenant);
			}
			property.setTenants(tenants);
			propertyRepository.save(property);
		}
	}
}
