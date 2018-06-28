package ru.sportmaster.esm.confirmation.service;

/**
 * Сервис кодов подтверждения.
 */
public interface CodeService {

    /**
     * Сбрасывает текущее состояние кода подтверждения по ключу.
     *
     * @param key ключ
     */
    void invalidate(String key);

    /**
     * Проверяет переданный код на валидность с учетом количества попыток и времени жизни кода подтверждения.
     * Каждый вызов этого метода засчитывается как попытка проверки кода.
     *
     * @param key  ключ (идентификатор сессии/пользователя либо другая строка идентификации)
     * @param code код который необходимо проверить на валидность
     * @return результат проверки валидации
     */
    boolean validate(String key, String code);

    String generate(String key) throws GenerateCodeException;
}
