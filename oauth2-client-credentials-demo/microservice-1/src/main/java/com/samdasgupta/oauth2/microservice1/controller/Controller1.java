package com.samdasgupta.oauth2.microservice1.controller;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class Controller1 {

    private final static RestTemplate restTemplate = new RestTemplateBuilder().build();
    private final static WebClient webClient = WebClient.builder().build();

    @GetMapping("/microservice1/home/rest-template")
    @ResponseStatus(HttpStatus.OK)
    public String helloRestTemplate() {

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+jwt.getTokenValue());

        ResponseEntity<String> output = restTemplate.exchange("http://localhost:8082/microservice2/home",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );
        return "I am Micro service 1 " + output.getBody();
    }

    @GetMapping("/microservice1/home/web-client")
    @ResponseStatus(HttpStatus.OK)
    public String helloWebClient() {

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String output = webClient.get()
                .uri("http://localhost:8082/microservice2/home")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(jwt.getTokenValue()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return "I am Micro service 1 " + output;
    }

}
