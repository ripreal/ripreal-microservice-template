package ru.sportmaster.esm.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.sportmaster.esm.user.dao.favorites.FavoriteProduct;
import ru.sportmaster.esm.user.dto.FavoriteAddRequest;
import ru.sportmaster.esm.user.service.FavoriteProductService;

import java.util.Objects;

@RestController
@RequestMapping("/api/v2/user/favorites")
public class UserFavoritesResource {

    private final FavoriteProductService service;

    @Autowired
    public UserFavoritesResource(FavoriteProductService service) {
        this.service = Objects.requireNonNull(service);
    }

    @GetMapping("/{customerId}")
    public Flux<FavoriteProduct> find(@PathVariable String customerId) {
        System.err.println("Get favorites: " + System.currentTimeMillis());
        return service.find(customerId);
    }

    @PostMapping("/{customerId}")
    public Mono<ResponseEntity<?>> add(@PathVariable String customerId,
                                     @RequestBody FavoriteAddRequest addRequest) {
        System.err.println("Post favorites: " + System.currentTimeMillis());
        return service.addOrUpdate(customerId, addRequest.getProductId(), addRequest.getSkuId())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .onErrorResume(err -> Mono.just(ResponseEntity.status(404).body(err.getMessage())));
    }

    @DeleteMapping("/{customerId}/{favoriteId}")
    public Mono<ResponseEntity<?>> remove(@PathVariable String customerId, @PathVariable String favoriteId) {
        System.err.println("Delete favorites: " + System.currentTimeMillis());
        return service.remove(customerId, favoriteId)
                .<ResponseEntity<?>>thenReturn(ResponseEntity.status(200).build())
                .onErrorResume(err -> Mono.just(ResponseEntity.status(404).body(err.getMessage())));
    }
}
