package ru.sportmaster.esm.loyalty;


import ru.sportmaster.esm.user.dto.Subscription;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class UpdateSubscriptionsRequest {

    @NotNull(message = "error.requiredField.customer")
    private Map<String, Object> customer;

    public UpdateSubscriptionsRequest(String firstname,
                                      String email,
                                      String mobilePhone,
                                      String clubproid,
                                      String customFields,
                                      Set<Subscription> subscriptions) {

        this.customer = new HashMap<>();
        this.customer.put("firstName", firstname);
        this.customer.put("email", email);
        this.customer.put("mobilePhone", mobilePhone);

        Map<String, String> customFieldsMap = new HashMap<>();
        customFieldsMap.put("subscribePage", customFields);
        this.customer.put("customFields", customFieldsMap);

        Map<String, String> ids = new HashMap<>();
        ids.put("clubproid", clubproid);
        this.customer.put("ids", ids);

        this.customer.put("subscriptions", subscriptions);
    }

    public UpdateSubscriptionsRequest() {
    }

    public Map<String, Object> getCustomer() {
        return customer;
    }

    public void setCustomer(Map<String, Object> customer) {
        this.customer = customer;
    }
}
