package com.shubham.hardware.controllers;

import com.shubham.hardware.dtos.ApiResponseMessage;
import com.shubham.hardware.dtos.ImageResponse;
import com.shubham.hardware.dtos.PageableResponse;
import com.shubham.hardware.dtos.UserDto;
import com.shubham.hardware.services.FileService;
import com.shubham.hardware.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/shubham-hardware/users")
@Tag(name = "UserController",description = "REST APIS related to perform user operations!!")
public class UserControllers {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    private Logger logger= LoggerFactory.getLogger(UserControllers.class);

//    create
    @PostMapping
    @Operation(summary = "create new user!!",description = "this is create user api!!")
    @ApiResponses(value={
            @ApiResponse(responseCode  = "200",description = "Success | Ok"),
            @ApiResponse(responseCode  = "401",description = "not authorized!!"),
            @ApiResponse(responseCode  = "201",description = "new user created!!")
    })
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        UserDto user = userService.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

//    update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto,@PathVariable("userId") String id){
        UserDto updateUser = userService.updateUser(userDto,id);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

//    delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String userId){
        userService.deleteUser(userId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                .message("User is deleted successfully!!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

//    get single user
    @GetMapping("/{userId}")
    @Operation(summary = "get single user by userId!!")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") String userId){
        UserDto user = userService.getUserById(userId);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

//    get all user
//    http://localhost:8086/shubham-hardware/users?pageNumber=0&pageSize=2&sortBy=[anyAttributeName like name,email,gender]&sortDir=desc
    @GetMapping
    @Operation(summary = "get all users")
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
            ){
        PageableResponse<UserDto> users= userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

//    get user by email
    @GetMapping("/email/{userId}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("userId") String userId){
        UserDto user = userService.getUserByEmail(userId);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

//    search user
//    http://localhost:8086/shubham-hardware/users/search/u
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> searchUsers(@PathVariable("keyword") String keyword){
        List<UserDto> users= userService.searchUser(keyword);
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

//    upload user image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @RequestParam("imageName") MultipartFile image,
            @PathVariable("userId") String userId
            ) throws IOException {
        String imageName = fileService.uploadFile(image,imageUploadPath);

//        get user by id to update user image
        UserDto userDto = userService.getUserById(userId);

//        delete user image first before updating user image if exist
        String imageNameToRemoveFromFolder=userDto.getImageName();
        logger.info("Image name to remove from folder : {}",imageNameToRemoveFromFolder);

        String fullPathWithImageName=imageUploadPath+imageNameToRemoveFromFolder;// images/users/5a1012eb-fa97-4479-bcca-e5b66d70ef98.png
        Path path= Paths.get(fullPathWithImageName);
//        logger.info("Path : {}",path);
        try {
            Files.delete(path);
        } catch (NoSuchFileException e) {
            logger.info("User image not found in folder!! : {}",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
//        update user image
            userDto.setImageName(imageName);
            userService.updateUser(userDto, userId);
        }
        ImageResponse response = ImageResponse.builder()
                .imageName(imageName)
                .message("Image updated successfully!!")
                .success(true)
                .status(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

//    serve user image
    @GetMapping("/image/{userId}")
    public void serveUserImage(
            @PathVariable("userId") String userId,
            HttpServletResponse response
            ) throws FileNotFoundException,IOException {
        UserDto userDto = userService.getUserById(userId);
        logger.info("User image name : {}",userDto.getImageName());
        InputStream resource = fileService.getResource(imageUploadPath,userDto.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

//    making the user as admin
    @PutMapping("admin/{userId}")
    public ResponseEntity<UserDto> makingUserAsAdmin(@PathVariable("userId") String userId){
        UserDto userDto = userService.assignUserAsAdmin(userId);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }
}
