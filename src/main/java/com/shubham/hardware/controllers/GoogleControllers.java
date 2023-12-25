package com.shubham.hardware.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.shubham.hardware.dtos.JwtRequest;
import com.shubham.hardware.dtos.JwtResponse;
import com.shubham.hardware.dtos.UserDto;
import com.shubham.hardware.entities.User;
import com.shubham.hardware.exceptions.BadApiRequestException;
import com.shubham.hardware.security.JwtHelper;
import com.shubham.hardware.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "GoogleController",description = "API for login with Google Authentication")
@SecurityRequirement(name = "bearerScheme")
//@CrossOrigin(
//        origins = "http://localhost:4200",
//        allowedHeaders = {"Authorization"},
//        methods = {RequestMethod.GET,RequestMethod.POST},
//        allowCredentials = "",
//        maxAge = 3600
//        )
public class GoogleControllers {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    private Logger logger= LoggerFactory.getLogger(GoogleControllers.class);

    @Value("${googleClientId}")
    private String googleClientId;

    @Value("${google.user.password}")
    private String newPassword;

    //    login with google
    @PostMapping("/google")
    public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String,Object> data) throws IOException {

//        get the id token from request
        String idToken=data.get("idToken").toString();
        NetHttpTransport netHttpTransport = new NetHttpTransport();
        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory).setAudience(Collections.singleton(googleClientId));
        GoogleIdToken googleIdToken=GoogleIdToken.parse(verifier.getJsonFactory(),idToken);
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        logger.info("Payload : {}",payload);
        String email = payload.getEmail();
        User user = userService.findUserByEmailOptional(email).orElse(null);
        if (user==null){
//            create new User
            user = this.saveUser(email,data.get("name").toString(),data.get("photoUrl").toString());
        }else{
//            generate jwt token
        }

        ResponseEntity<JwtResponse> response = this.loginWithGoogleHelper(JwtRequest.builder().email(user.getEmail()).password(newPassword).build());
        return response;
    }

    private User saveUser(String email, String name, String photoUrl) {
        UserDto userDto = UserDto.builder()
                .name(name)
                .email(email)
                .imageName(photoUrl)
                .roles(new HashSet<>())
                .password(newPassword)
                .build();
        UserDto user = userService.createUser(userDto);
        return modelMapper.map(user,User.class);
    }

//    generating jwt token and sending as response
    public ResponseEntity<JwtResponse> loginWithGoogleHelper(@RequestBody JwtRequest request){
//        username is email
        UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword());
        try {
            Authentication authenticate = authenticationManager.authenticate(authentication);
        }catch (BadCredentialsException e){
            throw new BadApiRequestException("Invalid username or password!!");
        }
        UserDetails userDetails= userDetailsService.loadUserByUsername(request.getEmail());;
        String generatedToken = this.jwtHelper.generateToken(userDetails);
        JwtResponse response = JwtResponse.builder()
                .jwtToken(generatedToken)
                .user(modelMapper.map(userDetails, UserDto.class))
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
