package ru.sportmaster.esm.user.service;

import reactor.core.publisher.Mono;
import ru.sportmaster.esm.user.dao.ViewHistoryEntry;

import java.util.List;

public interface ViewHistoryService {
    Mono<List<ViewHistoryEntry>> getViews(String profileId);

    Mono<List<ViewHistoryEntry>> addView(String profileId, String productId);

    Mono<List<ViewHistoryEntry>> addViews(String profileId, List<ViewHistoryEntry> views);
}
