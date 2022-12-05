package com.example.itnews.service;

import com.example.itnews.entity.Role;

import java.util.List;

public interface RoleService {
    Role getRole(Integer idRole);
    Role getRole(String roleName);
    List<Role> getAllRoles();
    Boolean isValidRole(Integer idRole);
}
