package com.eltech.snc.server.services;

import com.eltech.snc.server.entity.StatisticEntity;
import com.eltech.snc.server.jpa.entity.MazeEntity;
import com.eltech.snc.server.jpa.entity.RollingBallEntity;
import com.eltech.snc.server.jpa.entity.StencilEntity;
import com.eltech.snc.server.jpa.repo.MazeRepo;
import com.eltech.snc.server.jpa.repo.RollingBallRepo;
import com.eltech.snc.server.jpa.repo.StencilRepo;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

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

    public StatisticEntity getStatistic(Integer id) {
        StatisticEntity statistic = new StatisticEntity();

        List<MazeEntity> maze = mazeRepo.findAllByUserId(id);
        if (!maze.isEmpty()) {
            statistic.setBestMaze(maze.stream().map(MazeEntity::getResult).min(Comparator.naturalOrder()).orElse(0.0));
            statistic.setAverageMaze(maze.stream().map(MazeEntity::getResult).reduce(Double::sum).orElse(0.0) / maze.size());
        } else {
            statistic.setAverageMaze(0.0);
            statistic.setBestMaze(0.0);
        }

        List<RollingBallEntity> ball = rollingBallRepo.findAllByUserId(id);
        if (!ball.isEmpty()) {
            statistic.setBestBall(ball.stream().map(RollingBallEntity::getResult).min(Comparator.naturalOrder()).orElse(0.0));
            statistic.setAverageBall(ball.stream().map(RollingBallEntity::getResult).reduce(Double::sum).orElse(0.0) / ball.size());
        } else {
            statistic.setAverageBall(0.0);
            statistic.setBestBall(0.0);
        }

        List<StencilEntity> stencil = stencilRepo.findAllByUserId(id);
        if (!stencil.isEmpty()) {
            statistic.setBestStencil(stencil.stream().map(StencilEntity::getResult).max(Comparator.naturalOrder()).orElse(0.0));
            statistic.setAverageStencil(stencil.stream().map(StencilEntity::getResult).reduce(Double::sum).orElse(0.0) / stencil.size());
        } else {
            statistic.setAverageStencil(0.0);
            statistic.setBestStencil(0.0);
        }

        return statistic;
    }

}
