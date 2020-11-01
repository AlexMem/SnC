package com.eltech.snc.server.jpa.repo;

import com.eltech.snc.server.jpa.entity.RollingBallEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RollingBallRepo extends CrudRepository<RollingBallEntity, Integer> {
}
