package org.hw1;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class WorkArrays {

    public static List<Integer> squareAndAddTenWithout56End(List<Integer> list) {
        return list.stream()
                .map(a -> a * a)
                .map(a -> a + 10)
                .filter(num -> num % 10 != 5 && num % 10 != 6)
                .collect(Collectors.toList());
    }

    public static Map<Integer, Long> countDuplicates(List<Integer> list) {
        return list.stream()
                .collect(Collectors.groupingBy(a -> a, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(a -> a.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
