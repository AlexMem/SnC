package com.eltech.snc.server.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticEntity {
    Double bestMaze;
    Double averageMaze;
    Double bestBall;
    Double averageBall;
    Double bestStencil;
    Double averageStencil;
}
