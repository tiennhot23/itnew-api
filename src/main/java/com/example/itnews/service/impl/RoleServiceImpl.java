package com.example.itnews.service.impl;

import com.example.itnews.dto.sqlmapping.IVoteDTO;
import com.example.itnews.entity.Role;
import com.example.itnews.repository.RoleRepository;
import com.example.itnews.repository.VoteRepository;
import com.example.itnews.security.exceptions.MRuntimeException;
import com.example.itnews.service.RoleService;
import com.example.itnews.service.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final String TAG = "RoleServiceImpl";
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRole(Integer idRole) {
        return roleRepository.findById(idRole)
                .orElseThrow(() -> new MRuntimeException("Role  not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public Role getRole(String roleName) {
        return roleRepository.findRoleByName(roleName)
                .orElseThrow(() -> new MRuntimeException("Role  not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Boolean isValidRole(Integer idRole) {
        return roleRepository.findById(idRole).isPresent();
    }
}
