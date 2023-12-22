package com.shubham.hardware.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shubham.hardware.dtos.PageableResponse;
import com.shubham.hardware.dtos.RoleDto;
import com.shubham.hardware.dtos.UserDto;
import com.shubham.hardware.entities.Role;
import com.shubham.hardware.entities.User;
import com.shubham.hardware.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;

    private User user;
    private Role role;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

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

//    create user controller test
    @Test
    public void createUserTest() throws Exception {
//        /shubham-hardware/users + POST + user data as Json
//        data as Json + status created

        UserDto userDto=modelMapper.map(user, UserDto.class);

        Mockito.when(userService.createUser(Mockito.any())).thenReturn(userDto);

//        actual request for url
        this.mockMvc.perform(MockMvcRequestBuilders.post("/shubham-hardware/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(convertObjectToJsonString(user)))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());
    }


//    update user controller test
    @Test
    public void updateUserTest() throws Exception {
//        /shubham-hardware/users/{userId} + PUT + user data as Json
//        data as Json + status ok

        String userId="userIdabc";
//        UserDto userDto=modelMapper.map(user, UserDto.class);
        UserDto userDto=UserDto.builder()
                .name("Shubham Kumar Gupta")
                .email("kumar53shubham@gmail.com")
                .gender("Male")
                .password("kumar53")
                .imageName("shubham.png")
                .roles(Set.of(modelMapper.map(role, RoleDto.class)))
                .build();

        Mockito.when(userService.updateUser(Mockito.any(),Mockito.anyString())).thenReturn(userDto);

        //        actual request for url
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shubham-hardware/users/"+userId)
                        .header(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrdW1hcjUzc2h1YmhhbUBnbWFpbC5jb20iLCJpc3MiOiJTaHViaGFtIEhhcmR3YXJlIiwiaWF0IjoxNzAzMjY2MDE2LCJleHAiOjE3MDMyODQwMTZ9.r3sMLCA7haVGd4IThPVGN9DH3GDe9rLbgDMAcot-myi6eMlyuEsv0ycVhUtq0NEkY1jwl8R2G0qvi0TldeCyHQ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(convertObjectToJsonString(user)))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());
    }


//    get all user controller test
    @Test
    public void getAllUserTest() throws Exception {
//        /shubham-hardware/users/ + GET
//        data as Json + status ok

        UserDto user1=UserDto.builder()
                .name("Shubham Kumar")
                .email("kumar53shubham@gmail.com")
                .gender("Male")
                .password("kumar53")
                .imageName("shubham.png")
                .roles(Set.of(modelMapper.map(role, RoleDto.class)))
                .build();

        UserDto user2=UserDto.builder()
                .name("Himanshu Kumar")
                .email("hk985252@gmail.com")
                .gender("Male")
                .password("kumar0462")
                .imageName("himanshu.png")
                .roles(Set.of(modelMapper.map(role, RoleDto.class)))
                .build();

        List<UserDto> userDtoContent= Arrays.asList(user1,user2);
        PageableResponse<UserDto> pageableResponse=new PageableResponse<UserDto>(userDtoContent,100,1000,1000,15,false);

//        PageableResponse<UserDto> pageableResponse=new PageableResponse<UserDto>();
//        pageableResponse.setContent(
//                Arrays.asList(user1,user2)
//        );
//        pageableResponse.setPageNumber(100);
//        pageableResponse.setPageSize(1000);
//        pageableResponse.setLastPage(false);
//        pageableResponse.setTotalElements(1000);
//        pageableResponse.setTotalPages(15);

        Mockito.when(userService.getAllUsers(Mockito.anyInt(),Mockito.anyInt(), Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);

//        actual request for url
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shubham-hardware/users")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrdW1hcjUzc2h1YmhhbUBnbWFpbC5jb20iLCJpc3MiOiJTaHViaGFtIEhhcmR3YXJlIiwiaWF0IjoxNzAzMjY2MDE2LCJleHAiOjE3MDMyODQwMTZ9.r3sMLCA7haVGd4IThPVGN9DH3GDe9rLbgDMAcot-myi6eMlyuEsv0ycVhUtq0NEkY1jwl8R2G0qvi0TldeCyHQ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(convertObjectToJsonString(user)))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

//    convert Object to Json String
    private String convertObjectToJsonString(User user) {
        try {
            return new ObjectMapper().writeValueAsString(user);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
