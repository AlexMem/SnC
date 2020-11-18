package com.eltech.snc.server.jpa.repo;

import com.eltech.snc.server.jpa.entity.UnlockEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnlockRepo extends CrudRepository<UnlockEntity, Integer> {

    List<UnlockEntity> findAllByUserId(Integer userId);
}
