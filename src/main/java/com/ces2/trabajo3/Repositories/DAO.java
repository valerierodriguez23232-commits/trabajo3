package com.ces2.trabajo3.Repositories;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    T create(T t);
    T update(T t);
    Optional<T> findById(Integer id);
    List<T> findAll(int page, int size);
    boolean delete(Integer id);

    long count();
}
