package com.dsi.kaabu.kaabu.security;


import com.dsi.kaabu.kaabu.domaine.User;
import com.dsi.kaabu.kaabu.filtrer.CustomAuthenticationFiltrer;
import com.dsi.kaabu.kaabu.repository.RoleRepository;
import com.dsi.kaabu.kaabu.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;

@Configuration @EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final UserDetailsService userDetailsService;

    private final  BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(UserRepository userRepo, RoleRepository roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userDetailsService = new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = SecurityConfig.this.userRepo.findByUsername((username));
                if(user == null) {
                    throw new UsernameNotFoundException("User not found in the database");
                }
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                user.getRoles().forEach(role ->{
                    authorities.add(new SimpleGrantedAuthority(role.getName()));
                });
                return  new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
            }
        };
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().anyRequest().permitAll();
        http.addFilter(new CustomAuthenticationFiltrer(authenticationManagerBean(), userRepo, roleRepo));
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean()throws Exception{
        return super.authenticationManagerBean();
    }
}
