package com.eltech.snc.server.services;

import com.eltech.snc.server.entity.CompareUnit;
import com.eltech.snc.server.jpa.entity.UnlockEntity;
import com.eltech.snc.server.jpa.repo.UnlockRepo;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true)
public class UnlockService {
    UnlockRepo repo;

    public UnlockService(UnlockRepo repo) {
        this.repo = repo;
    }

    public Integer save(List<UnlockEntity> entities) {
        boolean isValid = validate(entities);
        if (isValid) {
            entities.get(0).setUniqId(0);
            Integer id = entities.size() > 0 ? repo.save(entities.get(0)).getUniqId() : null;
            if (id != null) {
//                repo.deleteByUniqId(entities.get(0).getUniqId());
                entities.get(0).setId(id);
                repo.save(entities.get(0));
                for (int i = 1; i < entities.size(); i++) {
                    entities.get(i).setId(id);
                    repo.save(entities.get(i));
                }
            }
            return id;
        }
        return -1;
    }

    /**
     * Получить все записи по пользователю
     * Получить список разных операций
     * Сделать из них фигуру из двух векторов.
     * Усреднить
     * Сравнить с допуском
     *
     * @param entities
     * @return
     */
    public boolean validate(List<UnlockEntity> entities) {
        if (entities.size() > 0) {
            List<UnlockEntity> unlock = repo.findAllByUserId(entities.get(0).getUserId());
            List<Integer> ids = unlock.stream()
                                      .map(UnlockEntity::getId)
                                      .distinct()
                                      .collect(Collectors.toList());

            if (ids.size() < 10) return true;

            Map<Integer, List<UnlockEntity>> unlockById = unlock.stream()
                                                                .filter(unlockEntity -> unlockEntity.getId() != null)
                                                                .collect(Collectors.groupingBy(UnlockEntity::getId));

            List<CompareUnit> compareUnits = unlockById.values().stream()
                                                       .map(CompareUnit::create)
                                                       .collect(Collectors.toList());

            CompareUnit average = CompareUnit.average(compareUnits);
            if (average != null) {
                CompareUnit current = CompareUnit.create(entities);
                return CompareUnit.compare(current, average, 100);
            }
        }
        return false;
    }

    public Integer getUnlockRegs(Integer userId) {
        List<UnlockEntity> unlock = repo.findAllByUserId(userId);
        List<Integer> ids = unlock.stream()
                                  .map(UnlockEntity::getId)
                                  .distinct()
                                  .collect(Collectors.toList());
        return ids.size();
    }
}
