package com.eltech.snc.utils;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UnlockEntity implements Serializable {
    private Integer uniqId;
    private Integer id;
    private Integer rowNum;
    private Integer userId;
    private Double pointX;
    private Double pointY;
    private LocalDateTime date;

    public UnlockEntity() {
    }

    public Integer getUniqId() {
        return uniqId;
    }

    public void setUniqId(Integer uniqId) {
        this.uniqId = uniqId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getPointX() {
        return pointX;
    }

    public void setPointX(Double pointX) {
        this.pointX = pointX;
    }

    public Double getPointY() {
        return pointY;
    }

    public void setPointY(Double pointY) {
        this.pointY = pointY;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}