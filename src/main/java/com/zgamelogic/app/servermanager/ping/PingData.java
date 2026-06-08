package com.zgamelogic.app.servermanager.ping;

public record PingData(
        String favicon,
        Boolean isModded,
        String description,
        Players players,
        Version version
) {
    record Players(int max, int online, Sample[] samples){}
    record Sample(String id, String name){}
    record Version(String name, int protocol){}
}
