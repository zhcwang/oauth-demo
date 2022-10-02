package com.felix.oauth2server.service;

import com.felix.oauth2server.model.JsonObjects;
import com.felix.oauth2server.model.UserAccount;

import javax.persistence.EntityNotFoundException;

public interface UserAccountService extends CommonServiceInterface<UserAccount> {
    JsonObjects<UserAccount> listByUsername(String username,
                                            int pageNum,
                                            int pageSize,
                                            String sortField,
                                            String sortOrder);

    UserAccount findByUsername(String username) throws EntityNotFoundException;

    boolean existsByUsername(String username);

    void loginSuccess(String username) throws EntityNotFoundException;

    void loginFailure(String username);
}
