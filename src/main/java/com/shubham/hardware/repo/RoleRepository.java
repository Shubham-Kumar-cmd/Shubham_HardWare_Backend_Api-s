package com.shubham.hardware.repo;

import com.shubham.hardware.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,String> {
}
