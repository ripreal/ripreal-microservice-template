package ru.sportmaster.esm.user.dto;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Subscriptions {

    public enum SUBSCRIPTION_TOPICS {

        PROMOTIONS("Promotions"),
        CLUB_PROGRAM("ClubProgram");

        private final String name;

        SUBSCRIPTION_TOPICS(String s) {
            name = s;
        }

        @Override
        public final String toString() {
            return name;
        }
    }

    public enum CONTACT_POINTS {

        EMAIL("Email"),
        SMS("sms");

        private final String name;

        CONTACT_POINTS(String s) {
            name = s;
        }

        @Override
        public final String toString() {
            return name;
        }
    }

    public static Set<Subscription> registrationSubscriptions(boolean active) {
        HashSet<Subscription> subscriptions = new HashSet<>();
        subscriptions.add(clubProgramByEmail(active));
        subscriptions.add(promotionsByEmail(active));
        subscriptions.add(clubProgramBySMS(active));
        return subscriptions;
    }

    public static Subscription promotionsByEmail(boolean active) {
        Subscription subscription = new Subscription();
        subscription.setIsSubscribed(true);
        subscription.setPointOfContact(CONTACT_POINTS.EMAIL.toString());
        subscription.setTopic(SUBSCRIPTION_TOPICS.PROMOTIONS.toString());
        return subscription;
    }

    public static Subscription clubProgramByEmail(boolean active) {
        Subscription subscription = new Subscription();
        subscription.setIsSubscribed(true);
        subscription.setPointOfContact(CONTACT_POINTS.EMAIL.toString());
        subscription.setTopic(SUBSCRIPTION_TOPICS.CLUB_PROGRAM.toString());
        return subscription;
    }

    public static Subscription clubProgramBySMS(boolean active) {
        Subscription subscription = new Subscription();
        subscription.setIsSubscribed(true);
        subscription.setPointOfContact(CONTACT_POINTS.SMS.toString());
        subscription.setTopic(SUBSCRIPTION_TOPICS.CLUB_PROGRAM.toString());
        return subscription;
    }
}
