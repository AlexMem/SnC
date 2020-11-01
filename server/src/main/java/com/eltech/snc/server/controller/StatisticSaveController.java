package com.eltech.snc.server.controller;

import com.eltech.snc.server.jpa.entity.MazeEntity;
import com.eltech.snc.server.jpa.entity.RollingBallEntity;
import com.eltech.snc.server.jpa.entity.StencilEntity;
import com.eltech.snc.server.jpa.entity.UserEntity;
import com.eltech.snc.server.services.StatisticService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticSaveController {
    StatisticService statisticService;

    public StatisticSaveController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @PostMapping(value = "/saveMazeStatistic", consumes = "application/json", produces = "application/json")
    public Integer createUser(@RequestBody MazeEntity statistic) {
        return statisticService.saveMazeStatistic(statistic);
    }

    @PostMapping(value = "/saveStencilStatistic", consumes = "application/json", produces = "application/json")
    public Integer createUser(@RequestBody StencilEntity statistic) {
        return statisticService.saveStencilStatistic(statistic);
    }

    @PostMapping(value = "/saveRollingBallStatistic", consumes = "application/json", produces = "application/json")
    public Integer createUser(@RequestBody RollingBallEntity statistic) {
        return statisticService.saveRollingBallStatistic(statistic);
    }
}
