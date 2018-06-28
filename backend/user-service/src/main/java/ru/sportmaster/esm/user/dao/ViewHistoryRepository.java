package ru.sportmaster.esm.user.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewHistoryRepository extends ReactiveMongoRepository<ViewHistory, String> {
}
