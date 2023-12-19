package com.shubham.hardware.config;

import com.shubham.hardware.security.JwtAuthenticationEntryPoint;
import com.shubham.hardware.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@Configuration
@EnableWebMvc
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final String[] PUBLIC_URLS={
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/v2/api-docs/**",
            "/v2/api-docs",
            "/swagger-ui/index.html**",
            "/actuator/health"
    };

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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
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
//                after creating corsFilter() we have to comment out the cors(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->{
                    auth
                        .requestMatchers("/auth/login","/auth/google")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET,"/welcome/shubham-hardware")
                        .permitAll()

//                        .requestMatchers("/swagger-ui/index.html","/favicon.ico")
//                        .permitAll()


                        .requestMatchers(PUBLIC_URLS)
                        .permitAll()

                        .requestMatchers(HttpMethod.POST,"/shubham-hardware/users")
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

//    CORS configuration
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("https://domain2.com","http://localhost:3000","http://localhost:8086","http://localhost:4200/"));
//        configuration.addAllowedOriginPattern("*");
        configuration.setAllowedHeaders(Arrays.asList("Authorization","Content-Type","Accept"));
//        configuration.addAllowedHeader("Authorization");
//        configuration.addAllowedHeader("Content-Type");
//        configuration.addAllowedHeader("Accept");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("OPTIONS");
        configuration.setMaxAge(3600L);

        source.registerCorsConfiguration("/**",configuration);
        return new CorsFilter(source);
    }

}
