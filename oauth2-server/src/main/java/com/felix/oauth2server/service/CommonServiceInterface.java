package com.felix.oauth2server.service;


import com.felix.oauth2server.model.JsonObjects;
import org.springframework.dao.DuplicateKeyException;

import javax.persistence.EntityNotFoundException;

public interface CommonServiceInterface<T> {


    default JsonObjects<T> list(int pageNum,
                                int pageSize,
                                String sortField,
                                String sortOrder) {
        throw new UnsupportedOperationException();
    }

    default T create(T t) throws DuplicateKeyException {
        throw new UnsupportedOperationException();
    }

    default T retrieveById(long id) throws EntityNotFoundException {
        throw new UnsupportedOperationException();
    }

    default T updateById(T t) throws EntityNotFoundException {
        throw new UnsupportedOperationException();
    }

    default void deleteById(long id) {
        throw new UnsupportedOperationException();
    }

    default void updateRecordStatus(long id, int recordStatus) {
        throw new UnsupportedOperationException();
    }
}
