package com.shubham.hardware.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

//    ctrl+alt+c shortcut to assign in variable
//    public static final String ADMIN = "ADMIN";
//    public static final String NORMAL = "NORMAL";

//    @Bean
//    public UserDetailsService userDetailsService(){
////    InMemoryAuthentication
////        create users
//        UserDetails normal = User.builder()
//                .username("Shubham")
//                .password(passwordEncoder().encode("shubham"))
//                .roles(NORMAL)
//                .build();
//        UserDetails admin = User.builder()
//                .username("Himanshu")
//                .password(passwordEncoder().encode("himanshu"))
//                .roles(ADMIN)
//                .build();
//
//        return new InMemoryUserDetailsManager(normal,admin);
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    form based authentication not required in this project
//        http
//                .authorizeHttpRequests()
//                .requestMatchers("/admin/**")
//                .hasRole("ADMIN")
//                .requestMatchers("/user/**")
//                .hasRole("USER")
//                .requestMatchers("/**")
//                .permitAll()
//                .and()
//                .formLogin()
//                .loginPage("/signin")
//                .loginProcessingUrl("/dologin")
//                .defaultSuccessUrl("/user/index")
//                //.failureUrl("/error")
//                .and()
//                .csrf()
//                .disable();


//        basic authentication
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and().httpBasic(Customizer.withDefaults());
        return http.build();
    }

}
