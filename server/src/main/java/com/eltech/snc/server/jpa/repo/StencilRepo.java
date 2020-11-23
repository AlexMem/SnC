package com.eltech.snc.server.jpa.repo;

import com.eltech.snc.server.jpa.entity.StencilEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StencilRepo extends CrudRepository<StencilEntity, Integer> {
    List<StencilEntity> findAllByUserId(Integer id);
}
