package com.zgamelogic.app.servermanager.ping;

import com.fasterxml.jackson.annotation.JsonView;
import com.zgamelogic.app.servermanager.data.Views;

@JsonView(Views.GeneralServerView.class)
public record PingData(
        String favicon,
        Boolean isModded,
        String description,
        Players players,
        Version version
) {
    @JsonView(Views.GeneralServerView.class)
    record Players(int max, int online, Sample[] sample){}
    @JsonView(Views.GeneralServerView.class)
    record Sample(String id, String name){}
    @JsonView(Views.GeneralServerView.class)
    record Version(String name, int protocol){}
}
