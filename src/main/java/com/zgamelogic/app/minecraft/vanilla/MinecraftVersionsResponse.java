package com.zgamelogic.app.minecraft.vanilla;

import java.util.List;

public record MinecraftVersionsResponse(
        Latest latest,
        List<MinecraftVersion> versions
) {
    record Latest(String release, String snapshot){}
}
