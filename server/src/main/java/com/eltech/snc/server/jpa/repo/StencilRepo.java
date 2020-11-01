package com.eltech.snc.server.jpa.repo;

import com.eltech.snc.server.jpa.entity.StencilEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StencilRepo extends CrudRepository<StencilEntity, Integer> {
}
