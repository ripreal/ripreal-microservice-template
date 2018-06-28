package ru.sportmaster.esm.user.dao.favorites;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FavoriteProductRepository extends ReactiveMongoRepository<FavoriteProduct, String> {

    @Query("{ 'customerId': ?0 }")
    Flux<FavoriteProduct> findByCustomerId(String customerId);

    Mono<FavoriteProduct> findByCustomerIdAndProductId(String customerId, String productId);
}
