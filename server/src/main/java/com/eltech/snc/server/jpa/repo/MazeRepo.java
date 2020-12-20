package com.eltech.snc.server.jpa.repo;

import com.eltech.snc.server.jpa.entity.MazeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MazeRepo extends CrudRepository<MazeEntity, Integer> {
    List<MazeEntity> findAllByUserId(Integer id);
}
