package com.eltech.snc.server.jpa.repo;

import com.eltech.snc.server.jpa.entity.MazeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MazeRepo extends CrudRepository<MazeEntity, Integer> {
}
