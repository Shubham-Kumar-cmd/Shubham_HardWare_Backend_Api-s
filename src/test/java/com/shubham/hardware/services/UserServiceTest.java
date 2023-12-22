package com.shubham.hardware.services;

import com.shubham.hardware.dtos.PageableResponse;
import com.shubham.hardware.dtos.UserDto;
import com.shubham.hardware.entities.Role;
import com.shubham.hardware.entities.User;
import com.shubham.hardware.repo.RoleRepository;
import com.shubham.hardware.repo.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
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

//    create user test
    @Test
    @DisplayName("Testing Create User")
    public void createUserTest(){
        Mockito.when(roleRepository.findById(Mockito.any())).thenReturn(Optional.of(role));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        UserDto user1 = userService.createUser(modelMapper.map(user, UserDto.class));
        System.out.println(user1.getName());
        System.out.println(user1.getRoles());
        System.out.println(user1.getUserId());
        Assertions.assertNotNull(user1);
        Assertions.assertEquals("Shubham Kumar",user1.getName());
    }

//    update user test
    @Test
    public void updateUserTest(){
        String userId="shnngmwhuwhKFH";
        UserDto userDto= UserDto.builder()
                .name("Shubham Kumar Gupta")
                .about("This is updating user")
                .imageName("sdferejm.png")
                .password("kumar53")
                .build();
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        UserDto updateUser = userService.updateUser(userDto, userId);
//        UserDto updateUser=modelMapper.map(user, UserDto.class);
        System.out.println(updateUser.getName());
        Assertions.assertNotNull(updateUser);
        Assertions.assertEquals(userDto.getName(),updateUser.getName(),"update user fail!!");
    }

//    delete user test
    @Test
    public void deleteUserTest(){
        String userId="nvzngfjsFJEYGJJH";
        Mockito.when(userRepository.findById("nvzngfjsFJEYGJJH")).thenReturn(Optional.of(user));
        userService.deleteUser(userId);
        Mockito.verify(userRepository,Mockito.times(1)).delete(user);
    }

//    get all user test
    @Test
    public void getAllUserTest(){

        Role roleAdmin=Role.builder()
                .roleId("pqr")
                .roleName("ADMIN")
                .build();

        User user1=User.builder()
                .name("Himanshu Kumar")
                .email("hk985252@gmail.com")
                .gender("Male")
                .password("kumar0462")
                .imageName("himanshu.png")
                .roles(Set.of(role,roleAdmin))
                .build();

        User user2=User.builder()
                .name("Jayant")
                .email("jayant@gmail.com")
                .gender("Male")
                .password("jayant")
                .imageName("jayant.png")
                .roles(Set.of(role))
                .build();

        List<User> userList=Arrays.asList(user,user1,user2);
        Page<User> page=new PageImpl<>(userList);
        Mockito.when(userRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
        PageableResponse<UserDto> allUsers = userService.getAllUsers(0, 10, "name", "asc");
        Assertions.assertEquals(3,allUsers.getContent().size());
    }

//    get User by id
    @Test
    public void getUserByIdTest(){
        String userId="sgjgjjghhhkkshnkjn";
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
        UserDto userDto = userService.getUserById(userId);
        System.out.println(userDto.getName());
        Assertions.assertEquals(user.getName(),userDto.getName());
    }

//    get user by email test
    @Test
    public void getUserByEmailTest(){
        String email="kumar53shubham@gmail.com";
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        UserDto userDto = userService.getUserByEmail(email);
        System.out.println(userDto.getEmail());
        Assertions.assertEquals(user.getEmail(),userDto.getEmail());
    }

//    search user test
    @Test
    public void searchUserTest(){
        Role roleAdmin=Role.builder()
                .roleId("pqr")
                .roleName("ADMIN")
                .build();

        User user1=User.builder()
                .name("Himanshu Kumar")
                .email("hk985252@gmail.com")
                .gender("Male")
                .password("kumar0462")
                .imageName("himanshu.png")
                .roles(Set.of(role,roleAdmin))
                .build();

        User user2=User.builder()
                .name("Jayant Kumar")
                .email("jayant@gmail.com")
                .gender("Male")
                .password("jayant")
                .imageName("jayant.png")
                .roles(Set.of(role))
                .build();

        User user3=User.builder()
                .name("Deepak kumar")
                .email("deepak@gmail.com")
                .gender("Male")
                .password("deepak")
                .imageName("deepak.png")
                .roles(Set.of(role))
                .build();

        String keyword="kumar";
        Mockito.when(userRepository.findByNameContaining(keyword)).thenReturn(Arrays.asList(user,user1,user2,user3));
        List<UserDto> list = userService.searchUser(keyword);
        System.out.println(Arrays.asList(list));
        Assertions.assertEquals(4,list.size(),"search test fail!!");
    }

//    find user by email optional test
    @Test
    public void findUserByEmailOptionalTest(){
        String email="kumar53shubham@gmail.com";
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Optional<User> userByEmailOptional = userService.findUserByEmailOptional(email);
        Assertions.assertTrue(userByEmailOptional.isPresent(),"email test fail!!");

        User user1 = userByEmailOptional.get();
        Assertions.assertEquals(user.getEmail(),user1.getEmail(),"email test fail!!");
    }
}
