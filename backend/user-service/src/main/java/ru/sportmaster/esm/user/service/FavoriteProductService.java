package ru.sportmaster.esm.user.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.sportmaster.esm.user.dao.favorites.FavoriteProduct;

public interface FavoriteProductService {

    Flux<FavoriteProduct> find(String customerId);

    Mono<FavoriteProduct> addOrUpdate(String customerId, String productId, String skuId);

    Mono<Void> remove(String customerId, String favoriteId);
}
