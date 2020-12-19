package com.eltech.snc.server.services;

import com.eltech.snc.server.entity.CompareUnit;
import com.eltech.snc.server.jpa.entity.UnlockEntity;
import com.eltech.snc.server.jpa.repo.UnlockRepo;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
//@FieldDefaults(makeFinal = true)
public class UnlockService {
    private static final int MAX_UNLOCKS = 50;
    private double err = 200;
    UnlockRepo repo;

    public UnlockService(UnlockRepo repo) {
        this.repo = repo;
    }

    public Integer save(List<UnlockEntity> entities) {
        StopWatch stopwatch = stopwatchStart("UnlockService.save");

        StopWatch repoStopwatch = stopwatchStart("UnlockRepo.findAllByUserId");
        List<UnlockEntity> unlock = repo.findAllByUserId(entities.get(0).getUserId());
        stopwatchStop(repoStopwatch);

        boolean isValid = validate(entities, unlock);
        Integer id = -1;
        if (isValid) {
            if (unlock.size() < MAX_UNLOCKS) {
                entities.get(0).setUniqId(0);
                id = entities.size() > 0 ? repo.save(entities.get(0)).getUniqId() : null;
                if (id != null) {
                    for (UnlockEntity entity : entities) {
                        entity.setId(id);
                    }
                    repo.saveAll(entities);
                }
            } else {
                id = 0;
            }
        }
        stopwatchStop(stopwatch);
        System.out.println("------------------------\n");
        return id;
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
    public boolean validate(List<UnlockEntity> entities, List<UnlockEntity> unlock) {
        StopWatch stopwatch = stopwatchStart("UnlockService.validate");
        if (entities.size() > 0) {
            List<Integer> ids = unlock.stream()
                                      .map(UnlockEntity::getId)
                                      .distinct()
                                      .collect(Collectors.toList());

            if (ids.size() < 10) {
                stopwatchStop(stopwatch);
                return true;
            }

            Map<Integer, List<UnlockEntity>> unlockById = unlock.stream()
                                                                .filter(unlockEntity -> unlockEntity.getId() != null)
                                                                .collect(Collectors.groupingBy(UnlockEntity::getId));

            List<CompareUnit> compareUnits = unlockById.values().stream()
                                                       .map(CompareUnit::create)
                                                       .collect(Collectors.toList());

            CompareUnit average = CompareUnit.average(compareUnits);
            if (average != null) {
                CompareUnit current = CompareUnit.create(entities);
                boolean result = CompareUnit.compare(current, average, err);
                stopwatchStop(stopwatch);
                return result;
            }
        }
        stopwatchStop(stopwatch);
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

    private StopWatch stopwatchStart(String name) {
        StopWatch stopwatch = new StopWatch(name);
        stopwatch.start(name);
        return stopwatch;
    }
    private void stopwatchStop(StopWatch stopwatch) {
        stopwatch.stop();
        StringBuilder sb = new StringBuilder();
        StopWatch.TaskInfo lastTaskInfo = stopwatch.getLastTaskInfo();
        sb.append(lastTaskInfo.getTaskName()).append('\t');
        sb.append(lastTaskInfo.getTimeMillis()).append(" ms");
        System.out.println(sb.toString());
    }

    public double setErr(double err) {
        this.err = err;
        return this.err;
    }
}
