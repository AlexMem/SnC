package com.eltech.snc.server.jpa.repo;

import com.eltech.snc.server.jpa.entity.RollingBallEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RollingBallRepo extends CrudRepository<RollingBallEntity, Integer> {
    List<RollingBallEntity> findAllByUserId(Integer id);
}
