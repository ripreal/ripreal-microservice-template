package ru.sportmaster.esm.confirmation.domain;

import java.io.Serializable;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Objects;

/**
 * Состояние кода подтверждения.
 */
public class CodeState implements Serializable {

    public static String getId(String userId, String key) {
        return String.format("%s_%s", userId, key);
    }

    private final String key;
    private final String code;
    private final ZonedDateTime createdOn;
    private final Duration retryInterval;
    private final ZonedDateTime retryTime;
    private final Duration timeToLive;
    private final int maxAttemptsCount;
    private int attemptsCount;

    public CodeState(String key, String code,
                     ZonedDateTime createdOn,
                     Duration retryInterval,
                     Duration timeToLive,
                     int maxAttemptsCount) {
        this.key = Objects.requireNonNull(key);
        this.code = Objects.requireNonNull(code);
        this.createdOn = Objects.requireNonNull(createdOn);
        this.retryInterval = Objects.requireNonNull(retryInterval);
        retryTime = createdOn.plus(retryInterval);
        this.timeToLive = Objects.requireNonNull(timeToLive);
        this.maxAttemptsCount = maxAttemptsCount;
    }

    /**
     * Возвращает ключ, для которого генерируется код потверждения.
     *
     * @return ключ кода потверждения
     */
    public String getKey() {
        return key;
    }

    /**
     * Возвращает код подтверждения.
     *
     * @return код подтверждения
     */
    public String getCode() {
        return code;
    }

    /**
     * Возвращает дату создания кода подтверждения.
     *
     * @return дата создания кода подтверждения
     */
    public ZonedDateTime getCreatedOn() {
        return createdOn;
    }

    /**
     * Возвращает интервал времени по истеченни которого будет доступна генераци нового кода подтверждения.
     *
     * @return интервал времени по истеченни которого будет доступна генераци нового кода подтверждения
     */
    public Duration getRetryInterval() {
        return retryInterval;
    }

    /**
     * Возвращает время жизни кода подтверждения.
     *
     * @return время жизни кода подтверждения
     */
    public Duration getTimeToLive() {
        return timeToLive;
    }

    /**
     * Возвращает максимальное количество попыток валидации кода.
     *
     * @return максимальное количество попыток валидации кода
     */
    public int getMaxAttemptsCount() {
        return maxAttemptsCount;
    }

    /**
     * Возвращает доступное количество попыток валидации кода.
     *
     * @return доступное количество попыток валидации кода
     */
    public int getRestAttemptsCount() {
        if (maxAttemptsCount < attemptsCount) {
            return 0;
        }

        return maxAttemptsCount - attemptsCount;
    }

    public void increaseAttempts() {
        if (attemptsCount < maxAttemptsCount)
            attemptsCount++;
    }

    /**
     * Проверяет возможность заново сгенерировать код подтверждения.
     *
     * @param date дата, относительно которой проверяет возможность
     * @return доступно ли заново сгенерировать код подтверждения.
     */
    public boolean isRetryAllowed(ZonedDateTime date) {
        return retryTime.compareTo(date) <= 0;
    }

    /**
     * Вернуть интервал в течении которого данный код подтверждения останется (будет продолжать быть) активным.
     * Если время жизни уже вышло, то будет возвращен нулевой Duration.
     *
     * @return интервал в течении которого данный код подтверждения останется (будет продолжать быть) активен
     * (время рассчитывается на момент вызова метода).
     */
    public Duration getRestTimeToLive(Temporal currentTime) {
        Duration currentDuration = Duration.between(createdOn, currentTime);
        Duration restDuration = timeToLive.minus(currentDuration);
        if (restDuration.toMillis() < 0) {
            return Duration.ZERO;
        }

        return restDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CodeState codeState = (CodeState) o;
        return Objects.equals(getKey(), codeState.getKey()) &&
                Objects.equals(getCode(), codeState.getCode()) &&
                Objects.equals(getCreatedOn(), codeState.getCreatedOn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey(), getCode(), getCreatedOn());
    }
}
