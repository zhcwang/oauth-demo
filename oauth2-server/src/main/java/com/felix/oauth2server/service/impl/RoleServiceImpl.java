package com.felix.oauth2server.service.impl;

import com.felix.oauth2server.model.Role;
import com.felix.oauth2server.repository.RoleRepository;
import com.felix.oauth2server.repository.entity.RoleEntity;
import com.felix.oauth2server.service.RoleService;
import com.github.dozermapper.core.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Mapper dozerMapper;

    @Override
    public Role findByRoleName(String roleName) {
        RoleEntity roleEntity = roleRepository.findByRoleName(roleName);
        if (roleEntity != null) {
            return dozerMapper.map(roleEntity, Role.class);
        } else {
            return null;
        }
    }

}
