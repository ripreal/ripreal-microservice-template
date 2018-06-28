package ru.sportmaster.esm.user.configuration;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import ru.sportmaster.esm.confirmation.domain.EmailConfirmation;
import ru.sportmaster.esm.user.dao.Profile;

@Component
public class MongoListener extends AbstractMongoEventListener<Object> {

    private final MongoOperations mongoOperations;

    public MongoListener(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    // EVENT HANDLERS

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        Object source = event.getSource();
        if (source instanceof EmailConfirmation) {
            checkUniqueConstraintNSave((EmailConfirmation) source);
        }
    }

    private void checkUniqueConstraintNSave(EmailConfirmation confirmation) {
        mongoOperations.findAllAndRemove(
            new Query(Criteria.where("profileId").is(confirmation.getProfileId()).and("confirmationType").is
                    (confirmation.getConfirmationType())), EmailConfirmation.class);
    }
}
