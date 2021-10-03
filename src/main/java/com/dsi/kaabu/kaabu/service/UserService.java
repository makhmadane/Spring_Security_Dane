package com.dsi.kaabu.kaabu.service;

import com.dsi.kaabu.kaabu.domaine.Role;
import com.dsi.kaabu.kaabu.domaine.User;


import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    List<User> getUsers();
}
