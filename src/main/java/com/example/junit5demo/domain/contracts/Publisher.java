package com.example.junit5demo.domain.contracts;

import com.example.junit5demo.domain.dtos.EventInformationDto;

public interface Publisher {
    void addSubscriber(Subscriber subscriber);
    void removeSubscriber(Subscriber subscriber);
    void notifySubscribers(EventInformationDto eventInformationDto);
}
