package ru.sportmaster.esm.confirmation.repository;

import ru.sportmaster.esm.confirmation.domain.CodeState;

import java.time.Duration;

/**
 * Хранилище кодов подтверждения.
 */
public interface CodeRepository {

    /**
     * Получить код подтверждения по ключу
     *
     * @param key ключ идентификатор
     * @return состояние кода подтверждения
     */
    CodeState get(String key);

    /**
     * Сохранить код подтверждения в хранилище
     *
     * @param key          ключ идентфикатор
     * @param newCodeState состояние кода подтверждения, которое необходимо сохранить
     * @param ttl          время жизни кода подвтерждения в хранилище (в мс.)
     */
    void put(String key, CodeState newCodeState, Duration ttl);

    /**
     * Удалить состояние кода подтверждения из хранилища
     *
     * @param key ключ идентификатор
     */
    void remove(String key);

}
