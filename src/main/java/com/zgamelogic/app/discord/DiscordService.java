package com.zgamelogic.app.discord;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class DiscordService {
    private final RestClient restClient;
    private final MultiValueMap<String, String> defaultBody;
    private final String redirectUrl;

    public DiscordService(
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

    public DiscordAuthenticationResponse authorizeWithDiscordCode(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>(defaultBody);
        form.add("grant_type",  "authorization_code");
        form.add("code", code);
        form.add("redirect_uri", redirectUrl);

        return restClient.post()
            .uri("/token")
            .body(form)
            .retrieve()
            .body(DiscordAuthenticationResponse.class);
    }

    public DiscordAuthenticationResponse authorizeWithDiscordRefreshToken(String refreshToken) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>(defaultBody);
        form.add("grant_type", "refresh_token");
        form.add("refresh_token", refreshToken);

        return restClient.post()
            .uri("/token")
            .body(form)
            .retrieve().body(DiscordAuthenticationResponse.class);
    }

    public DiscordUserResponse getDiscordUserFromToken(String token){
        RestClient restClient = RestClient.builder()
                .baseUrl("https://discord.com/api/users/@me")
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
        return restClient.get().retrieve().body(DiscordUserResponse.class);
    }
}
