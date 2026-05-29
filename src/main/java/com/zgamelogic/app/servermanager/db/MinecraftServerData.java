package com.zgamelogic.app.servermanager.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "minecraft_server_data", schema = "minecraft_server_manager")
@NoArgsConstructor
@Getter
@Setter
public class MinecraftServerData {
    @Id
    private UUID id;
    private String name;
    private int port;
    private int rconPort;
    private String rconPass;
    private String type;
    private String version;
    private String domain;
    private String startFile;

    public MinecraftServerData(String name, int port, int rconPort, String rconPass, String type, String version, String domain, String startFile) {
        id = UUID.randomUUID();
        this.name = name;
        this.port = port;
        this.rconPort = rconPort;
        this.rconPass = rconPass;
        this.type = type;
        this.version = version;
        this.domain = domain;
        this.startFile = startFile;
    }
}
