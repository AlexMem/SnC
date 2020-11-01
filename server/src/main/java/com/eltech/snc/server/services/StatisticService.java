package com.eltech.snc.server.services;

import com.eltech.snc.server.jpa.entity.MazeEntity;
import com.eltech.snc.server.jpa.entity.RollingBallEntity;
import com.eltech.snc.server.jpa.entity.StencilEntity;
import com.eltech.snc.server.jpa.repo.MazeRepo;
import com.eltech.snc.server.jpa.repo.RollingBallRepo;
import com.eltech.snc.server.jpa.repo.StencilRepo;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true)
public class StatisticService {
    MazeRepo mazeRepo;
    StencilRepo stencilRepo;
    RollingBallRepo rollingBallRepo;

    public StatisticService(MazeRepo mazeRepo, StencilRepo stencilRepo, RollingBallRepo rollingBallRepo) {
        this.mazeRepo = mazeRepo;
        this.stencilRepo = stencilRepo;
        this.rollingBallRepo = rollingBallRepo;
    }

    public Integer saveMazeStatistic(MazeEntity entity) {
        MazeEntity save = mazeRepo.save(entity);
        return save.getId();
    }

    public Integer saveRollingBallStatistic(RollingBallEntity entity) {
        RollingBallEntity save = rollingBallRepo.save(entity);
        return save.getId();
    }

    public Integer saveStencilStatistic(StencilEntity entity) {
        StencilEntity save = stencilRepo.save(entity);
        return save.getId();
    }

}
