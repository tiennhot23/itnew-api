package com.example.itnews.security;

import com.example.itnews.entity.Role;
import com.example.itnews.service.RoleService;
import com.sun.deploy.util.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomSecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private final Authentication authentication;

    public CustomSecurityExpressionRoot(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("Authentication object cannot be null");
        }
        this.authentication = authentication;
    }

    @Override
    public Authentication getAuthentication() {
        return null;
    }

    @Override
    public final boolean hasAuthority(String authority) {
        throw new RuntimeException("method hasAuthority() not allowed");
    }

    @Override
    public boolean hasAnyAuthority(String... authorities) {
        return false;
    }

    @Override
    public boolean hasRole(String roleName) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public boolean hasAnyRole(String... roles) {
        List<SimpleGrantedAuthority> allows = new ArrayList<>();
        for (String r : roles) {
            allows.add(new SimpleGrantedAuthority(r));
        }
        return allows.containsAll(authentication.getAuthorities());
    }

    @Override
    public boolean permitAll() {
        return authentication.getAuthorities().size() > 0;
    }

    @Override
    public boolean denyAll() {
        return false;
    }

    @Override
    public boolean isAnonymous() {
        return false;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public boolean isRememberMe() {
        return false;
    }

    @Override
    public boolean isFullyAuthenticated() {
        return false;
    }

    @Override
    public boolean hasPermission(Object target, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Object targetId, String targetType, Object permission) {
        return false;
    }

    @Override
    public void setFilterObject(Object filterObject) {

    }

    @Override
    public Object getFilterObject() {
        return null;
    }

    @Override
    public void setReturnObject(Object returnObject) {

    }

    @Override
    public Object getReturnObject() {
        return null;
    }

    @Override
    public Object getThis() {
        return null;
    }
}