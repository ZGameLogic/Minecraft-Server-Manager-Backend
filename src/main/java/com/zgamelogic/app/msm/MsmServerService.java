package com.zgamelogic.app.msm;

import com.zgamelogic.app.msm.db.MinecraftServerData;
import com.zgamelogic.app.msm.ping.PingService;
import com.zgamelogic.app.msm.rcon.RconService;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class MsmServerService {
    private final File serverDir;
    private final RconService rconService;
    private final PingService pingService;

    public MsmServerService(RconService rconService, PingService pingService){
        serverDir = new File("servers");
        if(!serverDir.exists()) serverDir.mkdir();
        this.rconService = rconService;
        this.pingService = pingService;
//        startServer(new MinecraftServerData(null, 0, 0, null, null, null, null, "start.bat"));
    }

    public void createServer(){}

    public void startServer(MinecraftServerData mcServer){
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", mcServer.getStartFile());
        pb.directory(serverDir);
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
