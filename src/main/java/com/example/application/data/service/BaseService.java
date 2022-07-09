package com.example.application.data.service;

import com.example.application.data.entity.AbstractEntity;
import com.example.application.data.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public abstract class BaseService<T extends AbstractEntity, ID> {

    private final JpaRepository<T, ID> repository;

    @Autowired
    public BaseService(JpaRepository repository) {
        this.repository = repository;
    }

    public Optional<T> get(ID id) {
        return repository.findById(id);
    }

    public T update(T entity) {
        return repository.save(entity);
    }

    public void delete(ID id) {
        repository.deleteById(id);
    }

    public Page<T> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
