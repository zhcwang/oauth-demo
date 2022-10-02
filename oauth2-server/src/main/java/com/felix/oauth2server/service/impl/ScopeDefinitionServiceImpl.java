package com.felix.oauth2server.service.impl;

import com.felix.oauth2server.repository.entity.ScopeDefinitionEntity;
import com.felix.oauth2server.model.ScopeDefinition;
import com.felix.oauth2server.repository.ScopeDefinitionRepository;
import com.felix.oauth2server.service.ScopeDefinitionService;
import com.github.dozermapper.core.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScopeDefinitionServiceImpl implements ScopeDefinitionService {

    @Autowired
    private ScopeDefinitionRepository scopeDefinitionRepository;

    @Autowired
    private Mapper dozerMapper;

    @Override
    public ScopeDefinition findByScope(String scope) {
        ScopeDefinitionEntity scopeDefinitionEntity = scopeDefinitionRepository.findByScope(scope);
        if (scopeDefinitionEntity != null) {
            return dozerMapper.map(scopeDefinitionEntity, ScopeDefinition.class);
        } else {
            return null;
        }
    }

}
