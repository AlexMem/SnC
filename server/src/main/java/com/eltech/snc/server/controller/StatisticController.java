package com.eltech.snc.server.controller;

import com.eltech.snc.server.entity.StatisticEntity;
import com.eltech.snc.server.jpa.entity.MazeEntity;
import com.eltech.snc.server.jpa.entity.RollingBallEntity;
import com.eltech.snc.server.jpa.entity.StencilEntity;
import com.eltech.snc.server.services.StatisticService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticController {
    StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @PostMapping(value = "/saveMazeStatistic", consumes = "application/json", produces = "application/json")
    public Integer saveStatistic(@RequestBody MazeEntity statistic) {
        return statisticService.saveMazeStatistic(statistic);
    }

    @PostMapping(value = "/saveStencilStatistic", consumes = "application/json", produces = "application/json")
    public Integer saveStatistic(@RequestBody StencilEntity statistic) {
        return statisticService.saveStencilStatistic(statistic);
    }

    @PostMapping(value = "/saveRollingBallStatistic", consumes = "application/json", produces = "application/json")
    public Integer saveStatistic(@RequestBody RollingBallEntity statistic) {
        return statisticService.saveRollingBallStatistic(statistic);
    }

    @GetMapping(value = "/getStatistic")
    public StatisticEntity getStatistic(@RequestParam Integer id) {
        return statisticService.getStatistic(id);
    }

}
