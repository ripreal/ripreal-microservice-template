package ru.sportmaster.esm.user.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@TypeAlias("ViewHistory")
@Document(collection = "view_history")
public class ViewHistory {
    /**
     * Идентификатор пользователя
     */
    @Id
    private String id;
    /**
     * Записи истории
     */
    private List<ViewHistoryEntry> entries;

    @PersistenceConstructor
    ViewHistory(String id, List<ViewHistoryEntry> entries) {
        this.id = id;
        this.entries = entries;
    }

    /**
     * Создает пустую историю просмотра
     *
     * @param id идентификатор пользователя
     */
    public ViewHistory(@NonNull String id) {
        Assert.notNull(id, "Profile Id may not be null");
        this.id = id;
        this.entries = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public List<ViewHistoryEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    /**
     * Добавляет просмотр продукта в историю. Удаляет старые просмотры, если превышено ограничение на размер истории.
     *
     * @param productId Id продукта
     * @param config    конфигурация истории просмотра
     */
    public void addView(@NonNull String productId, ViewHistoryConfig config) {
        Assert.notNull(productId, "Product Id may not be null");

        List<ViewHistoryEntry> newEntries = new ArrayList<>();
        newEntries.add(new ViewHistoryEntry(productId, ZonedDateTime.now(config.getClock())));

        List<ViewHistoryEntry> oldEntries = entries.stream()
                .filter(e -> !e.getProductId().equals(productId))
                .limit(config.getMaxEntries() - 1)
                .collect(Collectors.toList());

        newEntries.addAll(oldEntries);

        this.entries = newEntries;
    }

    /**
     * Добавляет просмотр всех продуктов в историю. Удаляет старые просмотры, если превышено ограничение на размер истории.
     *
     * @param newEntries набор просмотренных товаров с датами просмотра
     * @param config конфигурация истории просмотра
     */
    public void addViews(List<ViewHistoryEntry> newEntries, ViewHistoryConfig config) {
        List<ViewHistoryEntry> oldEntries = entries.stream()
                .filter(e -> newEntries.stream().noneMatch(ne -> e.getProductId().equals(ne.getProductId())))
                .limit(config.getMaxEntries() - newEntries.size())
                .collect(Collectors.toList());

        newEntries.addAll(oldEntries);
        newEntries.sort(Comparator.reverseOrder());

        this.entries = newEntries;
    }
}
