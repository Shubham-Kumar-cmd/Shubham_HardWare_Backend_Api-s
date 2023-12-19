package com.shubham.hardware;

import com.shubham.hardware.entities.Role;
import com.shubham.hardware.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.UUID;

@SpringBootApplication
@EnableWebMvc
public class ShubhamHardwareBackendApplication implements CommandLineRunner {

	@Value("${admin.role.id}")
	private String ROLE_ADMIN_ID;

	@Value("${normal.role.id}")
	private String ROLE_NORMAL_ID;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(ShubhamHardwareBackendApplication.class, args);
		System.out.println("creating api's");
	}

	@Override
	public void run(String... args) throws Exception {
//		System.out.println("Password: "+passwordEncoder.encode("kanksha"));


		try {
			Role admin = Role.builder()
					.roleId(ROLE_ADMIN_ID)
					.roleName("ROLE_ADMIN")
					.build();
			Role normal = Role.builder()
					.roleId(ROLE_NORMAL_ID)
					.roleName("ROLE_NORMAL")
					.build();
			roleRepository.save(admin);
			roleRepository.save(normal);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
