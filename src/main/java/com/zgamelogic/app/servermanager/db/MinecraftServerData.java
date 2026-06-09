package com.zgamelogic.app.servermanager.db;

import com.fasterxml.jackson.annotation.JsonView;
import com.zgamelogic.app.servermanager.data.Views;
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
    @JsonView(Views.GeneralServerView.class)
    private UUID id;
    @JsonView(Views.GeneralServerView.class)
    private String name;
    @JsonView(Views.GeneralServerView.class)
    private int port;
    private int rconPort;
    private String rconPass;
    @JsonView(Views.GeneralServerView.class)
    private boolean autoStart;
    @JsonView(Views.GeneralServerView.class)
    private String type;
    @JsonView(Views.GeneralServerView.class)
    private String version;
    @JsonView(Views.GeneralServerView.class)
    private String domain;
    @JsonView(Views.GeneralServerView.class)
    private String startFile;
    @JsonView(Views.GeneralServerView.class)
    private String serverDir;

    public MinecraftServerData(String serverDir, String startFile) {
        this.serverDir = serverDir;
        this.startFile = startFile;
    }

    public MinecraftServerData(String name, int port, int rconPort, String rconPass, boolean autoStart, String type, String version, String domain, String startFile, String serverDir) {
        id = UUID.randomUUID();
        this.name = name;
        this.port = port;
        this.rconPort = rconPort;
        this.rconPass = rconPass;
        this.type = type;
        this.version = version;
        this.domain = domain;
        this.startFile = startFile;
        this.autoStart = autoStart;
        this.serverDir = serverDir;
    }
}
