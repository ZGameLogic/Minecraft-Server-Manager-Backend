package com.zgamelogic.app.servermanager;

import com.zgamelogic.app.servermanager.data.MinecraftServerDTO;
import com.zgamelogic.app.servermanager.db.MinecraftServerData;
import com.zgamelogic.app.servermanager.db.MinecraftServerDataRepository;
import com.zgamelogic.app.servermanager.ping.PingService;
import com.zgamelogic.app.servermanager.rcon.RconService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Slf4j
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

    @Scheduled(cron = "0 */5 * * * *")
    public void autoRestart(){
        minecraftServerDataRepository.findAllByAutoRestartIsTrueAndAutoStartIsTrue()
            .stream().filter(server -> pingService.pingServer(server).isEmpty())
            .forEach(this::startServer);
    }

    public void createServer(){

    }

    public void startServer(MinecraftServerData mcServer){
        if(pingService.pingServer(mcServer).isPresent()){
            log.info("{} is already running", mcServer.getName());
            return;
        } else {
            log.info("{} is starting", mcServer.getName());
        }
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

    public List<MinecraftServerDTO> getMinecraftServerData(){
        return minecraftServerDataRepository.findAll().stream().map(m ->
                new MinecraftServerDTO(m, pingService.pingServer(m).orElse(null)))
                .toList();
    }

    public void sendServerCommand(MinecraftServerData mcServer, String command){

    }
}
