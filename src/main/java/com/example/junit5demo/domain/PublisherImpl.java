package com.example.junit5demo.domain;

import com.example.junit5demo.domain.contracts.Publisher;
import com.example.junit5demo.domain.contracts.Subscriber;
import com.example.junit5demo.domain.dtos.EventInformationDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PublisherImpl implements Publisher {
    private final Set<Subscriber> subscribers = new HashSet<>();

    @Override
    public void addSubscriber(Subscriber subscriber) {
        if (!Objects.isNull(subscriber)) {
            subscribers.add(subscriber);
        }
    }

    @Override
    public void removeSubscriber(Subscriber subscriber) {
        if (!Objects.isNull(subscriber)) {
            subscribers.remove(subscriber);
        }
    }

    @Override
    public void notifySubscribers(EventInformationDto eventInformationDto) {
        subscribers.forEach(x -> x.update(eventInformationDto));
    }

    public int totalOfSubscribers() {
        return subscribers.size();
    }

    public List<Subscriber> getSubscribers() {
        return new ArrayList<>(subscribers);
    }
}
