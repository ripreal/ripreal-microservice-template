package ru.sportmaster.esm.confirmation.service.utils;

import org.apache.commons.lang3.RandomStringUtils;
import ru.sportmaster.esm.confirmation.domain.AlphabetType;

public class CodeUtils {

    public static String generateCodeString(int codeLength, AlphabetType alphabetType) {
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
