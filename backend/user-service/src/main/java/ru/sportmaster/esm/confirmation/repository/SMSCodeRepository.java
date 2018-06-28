package ru.sportmaster.esm.confirmation.repository;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.sportmaster.esm.confirmation.configuration.SMSConfirmationProperties;
import ru.sportmaster.esm.confirmation.domain.CodeState;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Repository
@Primary
public class SMSCodeRepository implements CodeRepository {

    private IMap<String, CodeState> codes;

    @Autowired
    public SMSCodeRepository(SMSConfirmationProperties properties, HazelcastInstance hazelcastInstance) {
        codes = Objects.requireNonNull(hazelcastInstance).getMap(Objects.requireNonNull(properties).getCollectionName());
    }

    @Override
    public CodeState get(String key) {
        return codes.get(key);
    }

    @Override
    public void put(String key, CodeState newCodeState, Duration ttl) {
        codes.set(key, newCodeState, ttl.toMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void remove(String key) {
        codes.remove(key);
    }

}
