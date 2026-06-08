package com.zgamelogic.app.minecraft.vanilla;

public record MinecraftVersion(
        String id,
        String type,
        String url,
        String time,
        String releaseTime,
        Integer complianceLevel
){}