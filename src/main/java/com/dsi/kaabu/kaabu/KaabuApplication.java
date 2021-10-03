package com.dsi.kaabu.kaabu;

import com.dsi.kaabu.kaabu.domaine.Role;
import com.dsi.kaabu.kaabu.domaine.User;
import com.dsi.kaabu.kaabu.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class KaabuApplication {

    public static void main(String[] args) {
        SpringApplication.run(KaabuApplication.class, args);
    }
    /*@Bean
    CommandLineRunner run(UserService userService){
        return  args -> {
            userService.saveRole(new Role(null,"ROLE_USER"));
            userService.saveRole(new Role(null,"ROLE_ADMIN"));

            userService.saveUser(new User(null,"dane","passer","aaa","aaa",new ArrayList<>()));

            userService.addRoleToUser("dane","ROLE_USER");
            userService.addRoleToUser("dane","ROLE_ADMIN");
        };
    }*/

}
