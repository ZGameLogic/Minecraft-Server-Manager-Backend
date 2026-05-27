package com.zgamelogic.app.discord;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DiscordAuthenticationResponse(
    @JsonProperty("token_type")
    String tokenType,
    @JsonProperty("access_token")
    String accessToken,
    @JsonProperty("expires_in")
    Long expiresIn,
    @JsonProperty("refreshToken")
    String refreshToken,
    String scope
) {}
