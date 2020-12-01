package com.eltech.snc.utils;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RollingBallEntity implements Serializable {
    private Integer id;
    private Integer userId;
    private Double result;
    private LocalDateTime date;

    public RollingBallEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}