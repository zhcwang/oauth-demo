package com.felix.oauth2server.service.impl;

import com.felix.oauth2server.model.JsonObjects;
import com.felix.oauth2server.model.OauthClient;
import com.felix.oauth2server.repository.OauthClientRepository;
import com.felix.oauth2server.repository.entity.OauthClientEntity;
import com.felix.oauth2server.service.OauthClientService;
import com.github.dozermapper.core.Mapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class OauthClientServiceImpl implements OauthClientService {

    @Autowired
    OauthClientRepository oauthClientRepository;

    @Autowired
    Mapper dozerMapper;

    @Override
    public OauthClient findByClientId(String clientId) {
        OauthClientEntity oauthClientEntity = oauthClientRepository.findByClientId(clientId);
        if (oauthClientEntity != null) {
            return dozerMapper.map(oauthClientEntity, OauthClient.class);
        } else {
            return null;
        }
    }

    @Override
    public JsonObjects<OauthClient> list(int pageNum, int pageSize, String sortField, String sortOrder) {
        JsonObjects<OauthClient> jsonObjects = new JsonObjects<>();
        Sort sort;
        if (StringUtils.equalsIgnoreCase(sortOrder, "asc")) {
            sort = Sort.by(Sort.Direction.ASC, sortField);
        } else {
            sort = Sort.by(Sort.Direction.DESC, sortField);
        }
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<OauthClientEntity> page = oauthClientRepository.findAll(pageable);
        page.getContent();
        if (page.getContent().size() > 0) {
            jsonObjects.setRecordsTotal(page.getTotalElements());
            jsonObjects.setRecordsFiltered(page.getTotalElements());
            page.getContent().forEach(u -> jsonObjects.getData().add(dozerMapper.map(u, OauthClient.class)));
        }
        return jsonObjects;
    }

    @Override
    public OauthClient create(OauthClient oauthClient) {
        OauthClientEntity exist = oauthClientRepository.findByClientId(oauthClient.getClientId());
        if (exist != null) {
            throw new DuplicateKeyException(oauthClient.getClientId() + " already exists!");
        }
        OauthClientEntity oauthClientEntity = dozerMapper.map(oauthClient, OauthClientEntity.class);
        oauthClientRepository.save(oauthClientEntity);
        return dozerMapper.map(oauthClientEntity, OauthClient.class);
    }

    @Override
    public OauthClient retrieveById(long id) throws EntityNotFoundException {
        Optional<OauthClientEntity> entityOptional = oauthClientRepository.findById(id);
        return dozerMapper.map(entityOptional.orElseThrow(EntityNotFoundException::new), OauthClient.class);
    }

    @Override
    public OauthClient updateById(OauthClient oauthClient) throws EntityNotFoundException {
        Optional<OauthClientEntity> entityOptional = oauthClientRepository.findById(Long.parseLong(oauthClient.getId()));
        OauthClientEntity e = entityOptional.orElseThrow(EntityNotFoundException::new);
        if (StringUtils.isNotEmpty(oauthClient.getClientSecret())) {
            e.setClientSecret(oauthClient.getClientSecret());
        }
        e.setAuthorities(oauthClient.getAuthorities());
        e.setScope(oauthClient.getScope());
        e.setAuthorizedGrantTypes(oauthClient.getAuthorizedGrantTypes());
        e.setWebServerRedirectUri(oauthClient.getWebServerRedirectUri());

        if (StringUtils.isNotEmpty(oauthClient.getRemarks())) {
            e.setRemarks(oauthClient.getRemarks());
        }

        oauthClientRepository.save(e);
        return dozerMapper.map(e, OauthClient.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRecordStatus(long id, int recordStatus) {
        Optional<OauthClientEntity> entityOptional = oauthClientRepository.findById(id);
        OauthClientEntity e = entityOptional.orElseThrow(EntityNotFoundException::new);
        e.setRecordStatus(recordStatus);
        oauthClientRepository.save(e);
    }
}
