package com.campusrental;

import com.campusrental.entity.User;
import com.campusrental.entity.UserRoles;
import com.campusrental.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class CampusrentalApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CampusrentalApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
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
}
