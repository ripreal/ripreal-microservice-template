package ru.sportmaster.esm.confirmation.configuration;

import ru.sportmaster.esm.confirmation.domain.AlphabetType;

public class SMSConfirmationProperties {

    private int codeLength = 6;
    private int attemptsCount = 3;
    private long timeToLive = 10;
    private long retryInterval = 3;
    private AlphabetType alphabetType = AlphabetType.NUMERIC;

    private String collectionName;

    /**
     * Возвращает длину генерируемого кода подтверждения.
     *
     * @return длина кода подтверждения
     */
    public int getCodeLength() {
        return codeLength;
    }

    /**
     * Устанавливает длину генерируемого кода подтверждения.
     *
     * @param codeLength длина кода подтверждения
     */
    public void setCodeLength(int codeLength) {
        this.codeLength = codeLength;
    }

    /**
     * Возвращает время жизни кода подтверждения (в мс.).
     *
     * @return время жизни
     */
    public long getTimeToLive() {
        return timeToLive;
    }

    /**
     * Устанавливает время жизни кода подтверждения (в мс.).
     *
     * @param timeToLive время жизни
     */
    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    /**
     * Возвращает интервал времени, после которого код может быть сгенерирован повторно (в мс.).
     *
     * @return интервал времени, для повторной генерации кода
     */
    public long getRetryInterval() {
        return retryInterval;
    }

    /**
     * Устанавливает интервал времени, после которого код может быть сгенерирован повторно (в мс.)
     *
     * @param retryInterval интервал времени, для повторной генерации кода (в мс.)
     */
    public void setRetryInterval(long retryInterval) {
        this.retryInterval = retryInterval;
    }

    /**
     * Возвращает максимальное количество попыток проверки кода подтверждения.
     *
     * @return максимальное количество попыток проверки кода подтверждения
     */
    public int getAttemptsCount() {
        return attemptsCount;
    }

    /**
     * Устанавливает максимальное количество попыток проверки кода подтверждения.
     *
     * @param attemptsCount максимальное количество попыток проверки кода подтверждения
     */
    public void setAttemptsCount(int attemptsCount) {
        this.attemptsCount = attemptsCount;
    }

    /**
     * Возвращает набор символов для генерируемых кодов.
     *
     * @return набор символов
     */
    public AlphabetType getAlphabetType() {
        return alphabetType;
    }

    /**
     * Устанавливает набор символов для генерируемых кодов.
     *
     * @param alphabetType набор символов
     */
    public void setAlphabetType(AlphabetType alphabetType) {
        this.alphabetType = alphabetType;
    }

    /**
     * Возвращает название коллекции, в которой хранятся коды подтверждения.
     *
     * @return название коллекции, в которой хранятся коды подтверждений.
     */
    public String getCollectionName() {
        return collectionName;
    }

    /**
     * Устанавливает название коллекции, в которой хранятся коды подтверждений.
     *
     * @param collectionName название коллекций, в которой хранятся коды подтверждений
     */
    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
}
