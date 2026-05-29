package com.zgamelogic.app.discord;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Slf4j
@Service
public class DiscordService {
    private final RestClient restClient;
    private final MultiValueMap<String, String> defaultBody;

    public DiscordService(
            @Value("${discord.client.id}") String id,
            @Value("${discord.client.secret}") String secret
    ) {
        defaultBody = new LinkedMultiValueMap<>();
        defaultBody.add("client_id", id);
        defaultBody.add("client_secret", secret);

        restClient = RestClient.builder()
            .baseUrl("https://discord.com/api/oauth2")
            .defaultHeader("Content-Type", "application/x-www-form-urlencoded")
            .build();
    }

    public Optional<DiscordAuthenticationResponse> authorizeWithDiscordCode(String code, String redirectUrl) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>(defaultBody);
        form.add("grant_type",  "authorization_code");
        form.add("code", code);
        form.add("redirect_uri", redirectUrl);

        try {
            return Optional.ofNullable(restClient.post()
                .uri("/token")
                .body(form)
                .retrieve()
                .body(DiscordAuthenticationResponse.class));
        } catch (Exception e) {
            log.debug("Failed to authorize with discord code", e);
            return Optional.empty();
        }
    }

    public Optional<DiscordAuthenticationResponse> authorizeWithDiscordRefreshToken(String refreshToken) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>(defaultBody);
        form.add("grant_type", "refresh_token");
        form.add("refresh_token", refreshToken);

        try {
            return Optional.ofNullable(restClient.post()
                .uri("/token")
                .body(form)
                .retrieve().body(DiscordAuthenticationResponse.class));
        } catch (Exception e) {
            log.debug("Failed to authorize with discord refresh token", e);
            return Optional.empty();
        }
    }

    public Optional<DiscordUserResponse> getDiscordUserFromToken(String token){
        RestClient restClient = RestClient.builder()
            .baseUrl("https://discord.com/api/users/@me")
            .defaultHeader("Authorization", "Bearer " + token)
            .build();
        try {
            return Optional.ofNullable(restClient.get().retrieve().body(DiscordUserResponse.class));
        } catch (Exception e) {
            log.debug("Failed to get discord user from token", e);
            return Optional.empty();
        }
    }

    public void revokeToken(String discordToken) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>(defaultBody);
        form.add("token", discordToken);

        try {
            restClient.post()
                .uri("/token/revoke")
                .body(form)
                .retrieve()
                .body(String.class);
        } catch (Exception e) {
            log.debug("Failed to revoke discord token", e);
        }
    }
}
