package com.reactapp.core.services;

import java.util.List;
import java.util.Map;

public interface DefaultService<T> {

    T find(Integer id);

    List<T> list(Map<String, Object> params);

    T save(T model);

    T update(Integer id, T model);

    void delete(Integer id);

}
