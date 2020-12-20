package com.eltech.snc.server.entity;

import com.eltech.snc.server.jpa.entity.UnlockEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompareUnit {
    Integer id;
    List<Vector> vectors = new ArrayList<>();

    public static CompareUnit create(List<UnlockEntity> entities) {
        CompareUnit compareUnit = new CompareUnit();
        compareUnit.setId(entities.get(0).getId());
        entities = entities.stream()
                           .sorted(Comparator.comparing(UnlockEntity::getRowNum))
                           .collect(Collectors.toList());
        for (int i = 0; i < entities.size() - 1; i++) {
            compareUnit.vectors.add(new Vector(new Point(entities.get(i).getPointX(), entities.get(i).getPointY()),
                                               new Point(entities.get(i + 1).getPointX(), entities.get(i + 1).getPointY())));
        }

        return compareUnit;
    }

    public static CompareUnit average(List<CompareUnit> list) {
        int count = list.size();
        Optional<CompareUnit> reduce = list.stream().reduce(CompareUnit::sum);
        if (reduce.isPresent()) {
            CompareUnit result = reduce.get();
            result.setVectors(Vector.divide(result.getVectors(), count));
            return result;
        }
        return null;
    }

    public static CompareUnit sum(CompareUnit a, CompareUnit b) {
        CompareUnit res = new CompareUnit(a.getId(), a.getVectors());
        for (int i = 0; i < b.getVectors().size(); i++) {
            res.getVectors().set(i, Vector.sum(a.getVectors().get(i), b.getVectors().get(i)));
        }
        return res;
    }

    public static boolean compare(CompareUnit a, CompareUnit b, double err) {
        for (int i = 0; i < a.getVectors().size(); i++) {
            if (!a.getVectors().get(i).compare(b.getVectors().get(i), err)) {
                return false;
            }
        }
        return true;
    }
}
