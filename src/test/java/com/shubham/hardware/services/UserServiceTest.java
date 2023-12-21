package com.shubham.hardware.services;

import com.shubham.hardware.dtos.UserDto;
import com.shubham.hardware.entities.Role;
import com.shubham.hardware.entities.User;
import com.shubham.hardware.repo.RoleRepository;
import com.shubham.hardware.repo.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.Set;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    private User user;
    private Role role;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void init(){
        role=Role.builder()
                .roleId("abc")
                .roleName("NORMAL")
                .build();

        user=User.builder()
                .name("Shubham Kumar")
                .email("kumar53shubham@gmail.com")
                .gender("Male")
                .password("kumar53")
                .imageName("shubham.png")
                .roles(Set.of(role))
                .build();
    }

    @Test
    public void createUserTest(){
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.when(roleRepository.findById(Mockito.any())).thenReturn(Optional.of(role));
        UserDto user1 = userService.createUser(modelMapper.map(user, UserDto.class));
        System.out.println(user1.getName());
        Assertions.assertNotNull(user1);
        Assertions.assertEquals("Shubham Kumar",user1.getName());
    }
}
