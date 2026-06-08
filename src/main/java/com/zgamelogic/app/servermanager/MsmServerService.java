package com.zgamelogic.app.servermanager;

import com.zgamelogic.app.servermanager.db.MinecraftServerData;
import com.zgamelogic.app.servermanager.db.MinecraftServerDataRepository;
import com.zgamelogic.app.servermanager.ping.PingService;
import com.zgamelogic.app.servermanager.rcon.RconService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class MsmServerService {
    private final File serverDir;
    private final MinecraftServerDataRepository minecraftServerDataRepository;
    private final RconService rconService;
    private final PingService pingService;

    public MsmServerService(MinecraftServerDataRepository minecraftServerDataRepository, RconService rconService, PingService pingService){
        this.minecraftServerDataRepository = minecraftServerDataRepository;
        serverDir = new File("servers");
        if(!serverDir.exists()) serverDir.mkdir();
        this.rconService = rconService;
        this.pingService = pingService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void postInit(){
        minecraftServerDataRepository.findAllByAutoStartIsTrue().forEach(this::startServer);
    }

    public void createServer(){

    }

    public void startServer(MinecraftServerData mcServer){
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", mcServer.getStartFile());
        File dir = new File(mcServer.getServerDir());
        pb.directory(dir);
        pb.redirectErrorStream(true);
        try {
            pb.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendServerCommand(MinecraftServerData mcServer, String command){

    }

    public void getPingData(MinecraftServerData mcServer){}

}
