package com.example.junit5demo.utils;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import lombok.val;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public abstract class WireMockBase {

    final ObjectMapper objectMapper;
    final WireMockServer wireMockServer;

    public WireMockBase() {
        this.objectMapper = new ObjectMapper();
        this.wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8081));
    }

    public void startWiremockServer() {
        wireMockServer.resetAll();
        wireMockServer.start();
    }

    public void stopWiremockServer() {
        wireMockServer.stop();
    }

    public void addStub(String url, Object givenObject) throws JsonProcessingException {
        val json = objectMapper.writeValueAsString(givenObject);
        wireMockServer
                .stubFor(WireMock.get(urlEqualTo(url))
                        .willReturn(WireMock.aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withBody(json)));
    }
}
