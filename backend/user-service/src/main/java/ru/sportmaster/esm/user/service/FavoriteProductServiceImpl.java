package ru.sportmaster.esm.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.sportmaster.esm.user.dao.ProfileRepository;
import ru.sportmaster.esm.user.dao.favorites.FavoriteProduct;
import ru.sportmaster.esm.user.dao.favorites.FavoriteProductRepository;
import ru.sportmaster.esm.user.exception.FavoriteException;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Service
public class FavoriteProductServiceImpl implements FavoriteProductService {

    private final FavoriteProductRepository repository;
    private final ProfileRepository profileRepository;

    @Autowired
    public FavoriteProductServiceImpl(FavoriteProductRepository repository, ProfileRepository profileRepository) {
        this.repository = Objects.requireNonNull(repository);
        this.profileRepository = Objects.requireNonNull(profileRepository);
    }

    @Override
    public Flux<FavoriteProduct> find(@NotNull String customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    public Mono<FavoriteProduct> addOrUpdate(@NotNull String customerId, @NotNull String productId, String skuId) {
        return profileRepository.findById(customerId)
                .flatMap(profile -> repository.findByCustomerIdAndProductId(customerId, productId)
                            .flatMap(favProduct -> {
                                favProduct.setSkuId(skuId);
                                return repository.save(favProduct);
                            })
                            .switchIfEmpty(Mono.defer(() -> {
                                FavoriteProduct favoriteProduct = new FavoriteProduct();
                                favoriteProduct.setCustomerId(profile.getId());
                                favoriteProduct.setProductId(productId);
                                favoriteProduct.setSkuId(skuId);

                                return repository.save(favoriteProduct);
                            }))
                )
                .switchIfEmpty(Mono.error(new FavoriteException("error.customerNotFound")));
    }

    @Override
    public Mono<Void> remove(@NotNull String customerId, @NotNull String favoriteId) {
        return repository
                .findById(favoriteId)
                .switchIfEmpty(Mono.error(new FavoriteException("error.favoriteNotFound")))
                .flatMap(fav -> {
                    if (fav.getCustomerId().equals(customerId)) {
                        return repository.deleteById(favoriteId);
                    }
                    return Mono.error(new FavoriteException("error.favoriteNotFound"));
                });
    }
}
