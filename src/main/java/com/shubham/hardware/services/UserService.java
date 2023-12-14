package com.shubham.hardware.services;

import com.shubham.hardware.dtos.PageableResponse;
import com.shubham.hardware.dtos.UserDto;
import com.shubham.hardware.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

//    create
    UserDto createUser(UserDto userDto);

//    update
    UserDto updateUser(UserDto userDto,String userId);

//    delete
    void deleteUser(String userId);

//    get single user by id
    UserDto getUserById(String userId);

//    get single user by email
    UserDto getUserByEmail(String email);

//    get all users
    PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

//    search user
    List<UserDto> searchUser(String keyword);

//    other user specific feature

//    making any user as admin
    UserDto assignUserAsAdmin(String userId);

    Optional<User> findUserByEmailOptional(String email);

}
