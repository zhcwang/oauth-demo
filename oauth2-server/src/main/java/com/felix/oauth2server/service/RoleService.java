package com.felix.oauth2server.service;

import com.felix.oauth2server.model.Role;

public interface RoleService extends CommonServiceInterface<Role> {
    default Role findByRoleName(String roleName) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
