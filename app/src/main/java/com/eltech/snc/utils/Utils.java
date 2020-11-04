package com.eltech.snc.utils;

import java.util.ArrayList;
import java.util.Arrays;

public final class Utils {
    // Function to remove a specific Integer from Integer array
    // and return a new Integer array without the specified value
    public static Integer[] removeValueFromArray(Integer[] array, Integer value) {
        ArrayList<Integer> arrayList = new ArrayList<>(Arrays.asList(array));
        arrayList.remove(arrayList.indexOf(value));

        Integer[] newArray = new Integer[arrayList.size()];
        arrayList.toArray(newArray);

        return newArray;
    }
}
