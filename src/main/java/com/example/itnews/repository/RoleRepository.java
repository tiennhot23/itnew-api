package com.example.itnews.repository;

import com.example.itnews.dto.sqlmapping.ITagDTO;
import com.example.itnews.entity.FollowTag;
import com.example.itnews.entity.FollowTagId;
import com.example.itnews.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findRoleByName(String name);
}