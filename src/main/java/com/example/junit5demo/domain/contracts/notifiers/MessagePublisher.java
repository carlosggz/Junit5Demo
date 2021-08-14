package com.example.junit5demo.domain.contracts.notifiers;

import com.example.junit5demo.domain.dtos.BaseEvent;

public interface MessagePublisher<T extends BaseEvent>{
     void publishEvent(T event);
     void publishEvent(T event, String routingKey);
}
