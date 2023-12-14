package com.shubham.hardware.config;

import com.shubham.hardware.security.JwtAuthenticationEntryPoint;
import com.shubham.hardware.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

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

    public void authenticationManager(AuthenticationManagerBuilder builder) throws Exception {
        builder.authenticationProvider(daoAuthenticationProvider()).build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
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
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                .and().httpBasic(Customizer.withDefaults());


//        jwt authentication
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->{
                    auth
                        .requestMatchers("/auth/login","/auth/google")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST,"/shubham-hardware/users")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET,"/welcome/shubham-hardware")
                        .permitAll()

                        .requestMatchers(HttpMethod.DELETE,"/shubham-hardware/users/**")
                        .hasRole("ADMIN")

                        .anyRequest()
                        .authenticated();
                })
                .authenticationProvider(daoAuthenticationProvider())
                .exceptionHandling(exception->exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
