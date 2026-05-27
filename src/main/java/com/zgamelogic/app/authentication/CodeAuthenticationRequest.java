package com.zgamelogic.app.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CodeAuthenticationRequest(
        String code,
        @JsonProperty("redirect_url")
        String redirectUrl
) {}
