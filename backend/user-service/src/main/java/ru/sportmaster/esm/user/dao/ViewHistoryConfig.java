package ru.sportmaster.esm.user.dao;

import java.time.Clock;

public class ViewHistoryConfig {
    /**
     * Максимальное количество записей истории
     */
    private final int maxEntries;
    /**
     * Используемые часы
     */
    private final Clock clock;

    public ViewHistoryConfig(int maxEntries, Clock clock) {
        this.maxEntries = maxEntries;
        this.clock = clock;
    }

    public int getMaxEntries() {
        return maxEntries;
    }

    public Clock getClock() {
        return clock;
    }
}
