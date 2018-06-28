package ru.sportmaster.esm.user.dao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.time.ZonedDateTime;

public class ViewHistoryEntry implements Comparable<ViewHistoryEntry> {
    /**
     * Идентификатор продукта
     */
    private String productId;
    /**
     * Дата просмотра
     */
    private ZonedDateTime viewDate;

    @JsonCreator
    public ViewHistoryEntry(@NonNull @JsonProperty("productId") String productId,
                            @NonNull @JsonProperty("viewDate") ZonedDateTime viewDate) {
        Assert.notNull(productId, "Product Id may not be null");
        Assert.notNull(viewDate, "View date may not be null");
        this.productId = productId;
        this.viewDate = viewDate;
    }

    public String getProductId() {
        return productId;
    }

    public ZonedDateTime getViewDate() {
        return viewDate;
    }

    @Override
    public int compareTo(@NonNull ViewHistoryEntry other) {
        return this.viewDate.compareTo(other.getViewDate());
    }
}
