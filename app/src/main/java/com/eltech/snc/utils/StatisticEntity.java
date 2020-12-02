package com.eltech.snc.utils;

import java.io.Serializable;

public class StatisticEntity implements Serializable {
    private Double bestMaze;
    private Double averageMaze;
    private Double bestBall;
    private Double averageBall;
    private Double bestStencil;
    private Double averageStencil;

    public Double getBestMaze() {
        return bestMaze;
    }

    public void setBestMaze(Double bestMaze) {
        this.bestMaze = bestMaze;
    }

    public Double getAverageMaze() {
        return averageMaze;
    }

    public void setAverageMaze(Double averageMaze) {
        this.averageMaze = averageMaze;
    }

    public Double getBestBall() {
        return bestBall;
    }

    public void setBestBall(Double bestBall) {
        this.bestBall = bestBall;
    }

    public Double getAverageBall() {
        return averageBall;
    }

    public void setAverageBall(Double averageBall) {
        this.averageBall = averageBall;
    }

    public Double getBestStencil() {
        return bestStencil;
    }

    public void setBestStencil(Double bestStencil) {
        this.bestStencil = bestStencil;
    }

    public Double getAverageStencil() {
        return averageStencil;
    }

    public void setAverageStencil(Double averageStencil) {
        this.averageStencil = averageStencil;
    }
}
