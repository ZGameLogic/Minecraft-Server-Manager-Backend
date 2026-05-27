package com.zgamelogic.app.discord;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DiscordUserResponse(
        Long id,
        String username,
        String avatar,
        String banner,
        @JsonProperty("accent_color")
        Long accentColor
) {}
