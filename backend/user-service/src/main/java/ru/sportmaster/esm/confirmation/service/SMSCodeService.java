package ru.sportmaster.esm.confirmation.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import ru.sportmaster.esm.confirmation.configuration.SMSConfirmationProperties;
import ru.sportmaster.esm.confirmation.domain.AlphabetType;
import ru.sportmaster.esm.confirmation.domain.CodeState;
import ru.sportmaster.esm.confirmation.repository.CodeRepository;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Service
public class SMSCodeService implements CodeService {

    private final CodeRepository repository;
    private final SMSConfirmationProperties properties;

    public SMSCodeService(CodeRepository repository, SMSConfirmationProperties properties) {
        this.repository = Objects.requireNonNull(repository);
        this.properties = properties;
    }

    public String generate(String phoneNumber) throws GenerateCodeException {
        return generate(phoneNumber, properties.getCodeLength(),
                Duration.of(properties.getTimeToLive(), ChronoUnit.MINUTES),
                Duration.of(properties.getRetryInterval(), ChronoUnit.MINUTES),
                properties.getAlphabetType(),
                properties.getAttemptsCount()).getCode();
    }
    /**
     * Генерирует код подтверждения.
     *
     * @param key        ключ, по которому будет сгенерирован код
     * @param codeLength требуемая длина кода
     * @param timeToLive время жизни кода
     * @param retry      время ожидания между попытками подтвердить код
     * @return код подтверждения
     * @throws GenerateCodeException в случае ошибки генерации кода потверждения
     */
    CodeState generate(String key, int codeLength, Duration timeToLive, Duration retry, AlphabetType
            alphabetType, int attemptsCount)
            throws GenerateCodeException {
        ZonedDateTime currentTime = ZonedDateTime.now();
        CodeState existsCodeState = repository.get(key);
        if (existsCodeState != null && existsCodeState.getMaxAttemptsCount() > 0
                && !existsCodeState.isRetryAllowed(currentTime)) {
            throw new GenerateCodeException("Error when generating confirmation code. " +
                    "Retry interval has not expired yet. Try later");
        }

        String code = generateCodeString(codeLength, alphabetType);
        CodeState codeState = new CodeState(key, code, currentTime, retry, timeToLive, attemptsCount);

        repository.put(key, codeState, timeToLive);

        return codeState;
    }

    @Override
    public void invalidate(String key) {
        repository.remove(key);
    }

    @Override
    public boolean validate(String key, String code) {
        CodeState codeState = repository.get(key);
        if (codeState == null || !Objects.equals(codeState.getKey(), key)) {
            return false;
        }

        boolean isValid = false;

        if (codeState.getMaxAttemptsCount() == 0  || codeState.getRestAttemptsCount() > 0) { // проверяем количество
            // оставшихся попыток проверить
            // код
            isValid = Objects.equals(codeState.getCode(), code); // проверяем код
            if (!isValid) {
                // если код не прошел проверку, то увеличиваем количество попыток
                codeState.increaseAttempts();
                // вычисляем время жизни для кода
                Duration ttl = codeState.getRestTimeToLive(ZonedDateTime.now());
                // обновляем код
                repository.put(key, codeState, ttl);
            }
            else {
                invalidate(key);
            }
        } else { // превышено количество попыток
            invalidate(key);
        }

        return isValid;
    }

    private static String generateCodeString(int codeLength, AlphabetType alphabetType) {
        switch (alphabetType) {
            case NUMERIC:
                return RandomStringUtils.randomNumeric(codeLength);
            case NUMERIC_AND_CHARS:
                return RandomStringUtils.randomAlphanumeric(codeLength);
            default:
                throw new UnsupportedOperationException("Алфавит не поддерживается " + alphabetType);
        }
    }
}
