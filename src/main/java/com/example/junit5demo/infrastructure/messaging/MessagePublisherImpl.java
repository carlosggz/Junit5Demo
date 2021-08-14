package com.example.junit5demo.infrastructure.messaging;

import com.example.junit5demo.domain.contracts.notifiers.MessagePublisher;
import com.example.junit5demo.domain.dtos.BaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableBinding(JokesChannel.class)
@RequiredArgsConstructor
public class MessagePublisherImpl<T extends BaseEvent> implements MessagePublisher<T> {

    public static final String ROUTING_KEY = "myRoutingKey";
    private final JokesChannel source;

    @Override
    public  void publishEvent(T event) {
        publishEvent(event, null);
    }

    @Override
    public void publishEvent(T event, String routingKey) {

        MessageBuilder<T> builder = MessageBuilder.withPayload(event);

        if (!StringUtils.isBlank(routingKey)) {
            builder.setHeader(ROUTING_KEY, routingKey);
        }

        source.output().send(builder.build());
    }
}
