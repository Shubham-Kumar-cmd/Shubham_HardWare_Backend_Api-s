package com.shubham.hardware.services.impl;

import com.shubham.hardware.dtos.UserDto;
import com.shubham.hardware.entities.User;
import com.shubham.hardware.exceptions.ResourceNotFoundException;
import com.shubham.hardware.repo.UserRepository;
import com.shubham.hardware.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
//        generate unique id in string format
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
//        dto-->entity
        User user = dtoToEntity(userDto);
        User savedUser = userRepository.save(user);
//        entity-->dto
        UserDto newDto = entityToDto(savedUser);
        return newDto;
    }


    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found exception"));
        user.setName(userDto.getName());
//        we don't want to update the email
//        user.setEmail(userDto.getEmail());
        user.setAbout(userDto.getAbout());
        user.setImageName(userDto.getImageName());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());

        //update user
        User updatedUser = userRepository.save(user);
        UserDto updatedDto = entityToDto(updatedUser);
        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found exception"));

//        delete user
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found"));
        UserDto userById = entityToDto(user);
        return userById;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found with given email"));
        UserDto userDto = entityToDto(user);
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers(int pageNumber,int pageSize,String sortBy,String sortDir) {
//        ternary operator
        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page = userRepository.findAll(pageable);
        List<User> users = page.getContent();
//        using stream api
//        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());

//        using method reference
        List<UserDto> dtoList = users.stream().map(this::entityToDto).toList();
        return dtoList;
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
//        using stream api
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }

    private User dtoToEntity(UserDto userDto) {
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName())
//                .build();
//        return user;

        return modelMapper.map(userDto,User.class);

    }
    private UserDto entityToDto(User savedUser) {
//        UserDto user = UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .about(savedUser.getAbout())
//                .gender(savedUser.getGender())
//                .imageName(savedUser.getImageName())
//                .build();
//        return user;

        return modelMapper.map(savedUser, UserDto.class);
    }

}
