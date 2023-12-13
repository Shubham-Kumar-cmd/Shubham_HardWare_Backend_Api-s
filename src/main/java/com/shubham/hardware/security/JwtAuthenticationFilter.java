package com.shubham.hardware.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    private Logger logger= LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        Bearer jshfhBSKNKjnskjNgjaknknakjhnjgnaww
        String requestHeader = request.getHeader("Authorization");
        logger.info("Header : {}",requestHeader);
        String username=null;
        String token=null;

        if (requestHeader!=null && requestHeader.startsWith("Bearer")){
            token=requestHeader.substring(7);
            try {
                username = this.jwtHelper.getUsernameFromToken(token);
            }catch (IllegalArgumentException e){
                logger.info("Illegal argument while fetching the username!! : {}",e.getMessage());
            }catch (ExpiredJwtException e){
                logger.info("Given jwt token is expired!! : {}",e.getMessage());
            }catch (MalformedJwtException e){
                logger.info("Some changes has done in token!! invalid token : {}",e.getMessage());
            }catch (Exception e){
                logger.info("Exception handler invoked : {}",e.getMessage());
            }
        }else {
            logger.info("Invalid header value!!");
        }


        if (username!=null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            boolean validateToken = this.jwtHelper.validateToken(token, userDetails);

            if (validateToken){
                //set the authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                logger.info("Validation fails!!");
            }
        }
        filterChain.doFilter(request,response);
    }
}
