package ru.sportmaster.esm.user.web;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.sportmaster.esm.user.dao.ViewHistoryEntry;
import ru.sportmaster.esm.user.service.ViewHistoryService;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * REST API для работы с историй просмотра продуктов.
 */
@RestController
@RequestMapping("/api/v2/user/account/{customerId}/viewHistory")
public class ViewHistoryResource {
    private final ViewHistoryService historyService;
    private final Validator validator;

    public ViewHistoryResource(ViewHistoryService historyService, Validator validator) {
        this.historyService = historyService;
        this.validator = validator;
    }

    /**
     * Добавляет запись о просмотре одного товара, заменяя уже существующую
     * @param customerId
     * @param productId
     * @return полный список просмотренных товаров
     */
    @PostMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ViewHistoryEntry>>> addView(@PathVariable String customerId,
                                                                @PathVariable String productId) {
        return historyService.addView(customerId, productId).map(ResponseEntity::ok);
    }

    /**
     * Добавляет все записи о просмотре товаров, заменяя уже существующие
     * @param customerId
     * @param views список просмотренных товаров с датами просмотра
     * @return
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<?>> addViews(@PathVariable String customerId,
                                            @RequestBody List<ViewHistoryEntry> views) {
        List<Set<ConstraintViolation<ViewHistoryEntry>>> constraintViolations = views.stream()
                .map(validator::<ViewHistoryEntry>validate)
                .filter(x -> !x.isEmpty())
                .collect(Collectors.toList());

        if (constraintViolations.isEmpty()) {
            return historyService.addViews(customerId, views).map(ResponseEntity::ok);
        } else {
            List<List<String>> messages = constraintViolations.stream()
                    .map(viols -> viols.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()))
                    .collect(Collectors.toList());
            return Mono.just(ResponseEntity.badRequest().body(messages));
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ViewHistoryEntry>>> getViewEntries(@PathVariable String customerId) {
        return historyService.getViews(customerId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.ok(new ArrayList<>())));
    }
}
