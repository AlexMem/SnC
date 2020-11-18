package com.eltech.snc.server.entity;

import com.eltech.snc.server.jpa.entity.UnlockEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompareUnit {
    Integer id;
    Vector first;
    Vector last;

    public static CompareUnit create(List<UnlockEntity> entities) {
        CompareUnit compareUnit = new CompareUnit();
        compareUnit.setId(entities.get(0).getId());
        entities = entities.stream()
                .sorted(Comparator.comparing(UnlockEntity::getRowNum))
                .collect(Collectors.toList());
        int mid = entities.size() > 1 ? entities.size() / 2 : 1;
        UnlockEntity first = entities.get(0);
        UnlockEntity middle = entities.get(mid);
        UnlockEntity last = entities.get(entities.size() - 1);
        compareUnit.setFirst(new Vector(new Point(first.getPointX(), first.getPointY()), new Point(middle.getPointX(), middle.getPointY())));
        compareUnit.setLast(new Vector(new Point(middle.getPointX(), middle.getPointY()), new Point(last.getPointX(), last.getPointY())));

        return compareUnit;
    }

    public static CompareUnit average(List<CompareUnit> list){
        int count = list.size();
        Optional<CompareUnit> reduce = list.stream().reduce(CompareUnit::sum);
        if (reduce.isPresent()) {
            CompareUnit result = reduce.get();
            result.setFirst(Vector.divide(result.getFirst(), count));
            result.setLast(Vector.divide(result.getLast(), count));
            return result;
        }
        return null;
    }

    public static CompareUnit sum(CompareUnit a, CompareUnit b) {
        return new CompareUnit(a.getId(), Vector.sum(a.getFirst(), b.getFirst()), Vector.sum(a.getLast(), b.getLast()));
    }

    public static boolean compare(CompareUnit a, CompareUnit b, double err) {
        return a.getFirst().compare(b.getFirst(), err) && a.getLast().compare(b.getLast(), err);
    }



}
