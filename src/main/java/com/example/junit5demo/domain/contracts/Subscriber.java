package com.example.junit5demo.domain.contracts;

import com.example.junit5demo.domain.dtos.EventInformationDto;
import lombok.NonNull;

public interface Subscriber {
    void update(@NonNull EventInformationDto eventInformationDto);
}
