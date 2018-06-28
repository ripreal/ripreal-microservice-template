package ru.sportmaster.esm.user.service.utils;

import java.util.HashSet;
import java.util.Set;

public class CollectionsUtils {
    /**
     * Produce new set containing merged result
     * @param existSet - set to be updated
     * @param newSet - updating set
     * @param <T>
     * @return
     */
    public static <T> Set<T> mergeSets(Set<T> existSet, Set<T> newSet) {
        Set<T> resultSet = new HashSet<>();
        resultSet.addAll(newSet);
        resultSet.addAll(existSet);
        return resultSet;
    }
}
