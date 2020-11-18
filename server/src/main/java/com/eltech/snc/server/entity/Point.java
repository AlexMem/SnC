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
public class Point {
    Double x;
    Double y;

    public static Point sum(Point a, Point b) {
        return new Point(a.getX() + b.getX(), a.getY() + b.getY());
    }

    public static Point divide(Point a, int div) {
        return new Point(a.getX() / div, a.getY() / div);
    }

    public boolean compare(Point a, double err) {
        return Math.abs(this.getX() - a.getX()) < err && Math.abs(this.getY() - a.getY()) < err;
    }
}
