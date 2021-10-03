package com.dsi.kaabu.kaabu.repository;


import com.dsi.kaabu.kaabu.domaine.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String name);

}
