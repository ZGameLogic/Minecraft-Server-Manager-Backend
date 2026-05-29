package com.zgamelogic.app.minecraft.vanilla;

public record MinecraftVersionResponse(
        String type,
        Integer complianceLevel,
        Downloads downloads
) {
    record Downloads(Download server){
        record Download(String url){}
    }
}
