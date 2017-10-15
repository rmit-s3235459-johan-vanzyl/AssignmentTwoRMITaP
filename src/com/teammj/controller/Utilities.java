package com.teammj.controller;

import java.util.*;

/**
 * Utility class to help reduce code reuse
 *
 * @author Johan van Zyl
 * @author Michael Guida
 */
public final class Utilities {
    public static final Random random = new Random(System.nanoTime());

    /**
     * Sort Map by its numerical value
     * Credit to: stackoverflow - https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java
     *
     * @param map - input Map
     * @param <K> - input Key
     * @param <V> - input Value to sort by
     * @return - returns a sorted map
     */
    static <K, V extends Comparable<? super V>> Map<K, V>
    sortMapByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list =
                new LinkedList<>(map.entrySet());
        Collections.sort(list, Comparator.comparing(o -> (o.getValue())));

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }


    /**
     * Gets user input by prompt and with filter (Regex)
     * Keeps looping until valid input
     *
     * @param scanner - Keyboard Input
     * @param prompt  - Text prompt
     * @param REGEX   - The filter type
     * @return - Users input as a number (integer)
     */
    static int getUserInput(Scanner scanner, String prompt, String REGEX) {
        String userInput;
        do {
            System.out.println(prompt);
        } while (!(userInput = scanner.next()).matches(REGEX));

        System.out.println("\nYour input:\n" + userInput + "\n");
        return Integer.parseInt(userInput);
    }

}
