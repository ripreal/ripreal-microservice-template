package ru.sportmaster.esm.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.sportmaster.esm.user.dao.ViewHistory;
import ru.sportmaster.esm.user.dao.ViewHistoryConfig;
import ru.sportmaster.esm.user.dao.ViewHistoryEntry;
import ru.sportmaster.esm.user.dao.ViewHistoryRepository;

import java.time.Clock;
import java.util.List;

@Service
public class ViewHistoryServiceImpl implements ViewHistoryService {
    private final ViewHistoryRepository historyRepository;
    private final ViewHistoryConfig historyConfig;

    public ViewHistoryServiceImpl(ViewHistoryRepository historyRepository,
                                  @Value("${sm.user.viewHistory.maxEntries}") int maxEntries) {
        this.historyRepository = historyRepository;
        this.historyConfig = new ViewHistoryConfig(maxEntries, Clock.systemDefaultZone());
    }

    @Override
    public Mono<List<ViewHistoryEntry>> getViews(String profileId) {
        return historyRepository.findById(profileId)
                .map(ViewHistory::getEntries);
    }

    @Override
    public Mono<List<ViewHistoryEntry>> addView(String profileId, String productId) {
        return historyRepository.findById(profileId)
                .switchIfEmpty(Mono.fromSupplier(() -> new ViewHistory(profileId)))
                .flatMap(h -> {
                    h.addView(productId, historyConfig);
                    return historyRepository.save(h);
                })
                .map(ViewHistory::getEntries);
    }

    @Override
    public Mono<List<ViewHistoryEntry>> addViews(String profileId, List<ViewHistoryEntry> views) {
        return historyRepository.findById(profileId)
                .switchIfEmpty(Mono.fromSupplier(() -> new ViewHistory(profileId)))
                .flatMap(h -> {
                    h.addViews(views, historyConfig);
                    return historyRepository.save(h);
                })
                .map(ViewHistory::getEntries);
    }
}
