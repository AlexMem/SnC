package com.eltech.snc.utils;

import java.io.Serializable;
import java.util.List;

public class UnlockListEntity implements Serializable {
    private Integer userId;
    private List<Float> pointX;
    private List<Float> pointY;
    private String date;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Float> getPointX() {
        return pointX;
    }

    public void setPointX(List<Float> pointX) {
        this.pointX = pointX;
    }

    public List<Float> getPointY() {
        return pointY;
    }

    public void setPointY(List<Float> pointY) {
        this.pointY = pointY;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}