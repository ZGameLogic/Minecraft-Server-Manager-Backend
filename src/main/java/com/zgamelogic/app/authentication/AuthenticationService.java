package com.zgamelogic.app.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class AuthenticationService {
    private final RestClient restClient;
    private final MultiValueMap<String, String> defaultBody;
    private final String redirectUrl;

    public AuthenticationService(
            @Value("${discord.client.id}") String id,
            @Value("${discord.client.secret}") String secret,
            @Value("${discord.client.redirect-url}") String redirectUrl
    ) {
        defaultBody = new LinkedMultiValueMap<>();
        defaultBody.add("client_id", id);
        defaultBody.add("client_secret", secret);

        this.redirectUrl = redirectUrl;

        restClient = RestClient.builder()
            .baseUrl("https://discord.com/api/oauth2")
            .defaultHeader("Content-Type", "application/x-www-form-urlencoded")
            .build();
    }

    public void authorizeWithDiscordCode(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>(defaultBody);
        form.add("grant_type",  "authorization_code");
        form.add("code", code);
        form.add("redirect_uri", redirectUrl);

        System.out.println(restClient.post()
            .uri("/token")
            .body(form)
            .retrieve().body(String.class));
    }
}
