package com.example.postItBackend.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public class CrudService<T, ID>{

    private final JpaRepository<T, ID> repository;

    public CrudService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    public void save(T entity) {
        repository.save(entity);
    }

    public void deleteById(ID id) {
        repository.deleteById(id);
    }
}
