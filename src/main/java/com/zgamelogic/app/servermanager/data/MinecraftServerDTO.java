package com.zgamelogic.app.servermanager.data;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;
import com.zgamelogic.app.servermanager.db.MinecraftServerData;
import com.zgamelogic.app.servermanager.ping.PingData;

public record MinecraftServerDTO(
        @JsonUnwrapped
        @JsonView(Views.GeneralServerView.class)
        MinecraftServerData serverData,
        @JsonView(Views.GeneralServerView.class)
        PingData pingData
) {}
