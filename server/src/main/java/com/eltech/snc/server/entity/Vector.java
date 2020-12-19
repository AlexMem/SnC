package com.eltech.snc.server.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

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

    public static List<Vector> divide(List<Vector> a, int div) {
        List<Vector> res = new ArrayList<>(a);
        for (int i = 0; i < a.size(); i++) {
            res.get(i).setX(Point.divide(a.get(i).getX(), div));
            res.get(i).setY(Point.divide(a.get(i).getY(), div));
        }
        return res;
    }

    public boolean compare(Vector a, double err) {
        return this.getX().compare(a.getX(), err) && this.getY().compare(a.getY(), err);
    }
}
