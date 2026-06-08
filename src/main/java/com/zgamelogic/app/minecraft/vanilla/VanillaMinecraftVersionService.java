package com.zgamelogic.app.minecraft.vanilla;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class VanillaMinecraftVersionService {
    private final RestClient restClient;

    public VanillaMinecraftVersionService(){
        restClient = RestClient.builder().build();
    }

    @Cacheable("minecraft versions cache")
    public MinecraftVersionsResponse getVersions(){
        return restClient.get().uri("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json")
            .retrieve()
            .body(MinecraftVersionsResponse.class);
    }

    @Cacheable("minecraft versions cache")
    public MinecraftVersionResponse getVersion(MinecraftVersion version){
        return restClient.get().uri(version.url())
            .retrieve()
            .body(MinecraftVersionResponse.class);
    }
}
