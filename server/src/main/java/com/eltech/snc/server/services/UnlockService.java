package com.eltech.snc.server.services;

import com.eltech.snc.server.jpa.entity.UnlockEntity;
import com.eltech.snc.server.jpa.repo.UnlockRepo;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true)
public class UnlockService {
    UnlockRepo repo;

    public UnlockService(UnlockRepo repo) {
        this.repo = repo;
    }

    public Integer save(List<UnlockEntity> entities) {
        Integer id = entities.size() > 0 ? repo.save(entities.get(0)).getUniqId() : null;
        if (id != null) {
            entities.get(0).setId(id);
            repo.save(entities.get(0));
            for (int i = 1; i < entities.size(); i++) {
                entities.get(i).setId(id);
                repo.save(entities.get(i));
            }
        }
        return id;
    }

    /**
     * Получить все записи по пользователю
     * Получить список разных операций
     * Сделать из них фигуру из двух векторов.
     *
     * @param entities
     */
    public void validate(List<UnlockEntity> entities) {
        if (entities.size() > 0) {
            List<UnlockEntity> allByUserId = repo.findAllByUserId(entities.get(0).getUserId());
            List<Integer> ids = allByUserId.stream()
                    .map(UnlockEntity::getId)
                    .distinct()
                    .collect(Collectors.toList());



        }
    }

}
