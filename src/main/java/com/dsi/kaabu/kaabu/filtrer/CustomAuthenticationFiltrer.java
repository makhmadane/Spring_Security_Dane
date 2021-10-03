package com.dsi.kaabu.kaabu.filtrer;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.dsi.kaabu.kaabu.repository.RoleRepository;
import com.dsi.kaabu.kaabu.repository.UserRepository;
import com.dsi.kaabu.kaabu.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class CustomAuthenticationFiltrer extends UsernamePasswordAuthenticationFilter {
    private  final AuthenticationManager authenticationManager;
    private final UserServiceImpl userDetailsService;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    public CustomAuthenticationFiltrer(AuthenticationManager authenticationManager, UserRepository userRepo, RoleRepository roleRepo){
        this.authenticationManager=authenticationManager;
        this.userRepo = userRepo;

        this.roleRepo = roleRepo;
        userDetailsService = new UserServiceImpl(userRepo,roleRepo);
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
       String username =request.getParameter("username");
        String password =request.getParameter("password");
        UsernamePasswordAuthenticationToken  authenticationToken=new UsernamePasswordAuthenticationToken(username,password);
        return  authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentification) throws IOException, ServletException {
        User user= (User) authentification.getPrincipal();


        Algorithm algorithm= Algorithm.HMAC256("secret".getBytes());

        String access_token= JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).
                sign(algorithm);
        String refresh_token= JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).
                        sign(algorithm);
        Map<Object,Object> tokens= new HashMap<>();


        System.out.println(userDetailsService.getUser(user.getUsername()));

        tokens.put("access_token",access_token);
        tokens.put("refresh_token",refresh_token);

        tokens.put("user",userDetailsService.getUser(user.getUsername()));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);


    }
}
