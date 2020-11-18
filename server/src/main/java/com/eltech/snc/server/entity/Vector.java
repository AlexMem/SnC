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
public class Vector {
    Point x;
    Point y;

    public static Vector sum(Vector a, Vector b) {
        return new Vector(Point.sum(a.getX(), b.getX()), Point.sum(a.getY(), b.getY()));
    }

    public static Vector divide(Vector a, int div) {
        return new Vector(Point.divide(a.getX(), div), Point.divide(a.getY(), div));
    }

    public boolean compare(Vector a, double err) {
        return this.getX().compare(a.getX(), err) && this.getY().compare(a.getY(), err);
    }
}
