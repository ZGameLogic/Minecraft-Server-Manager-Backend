package com.zgamelogic.app.servermanager;

import com.fasterxml.jackson.annotation.JsonView;
import com.zgamelogic.app.authentication.db.Authenticated;
import com.zgamelogic.app.servermanager.data.MinecraftServerDTO;
import com.zgamelogic.app.servermanager.data.Views;
import com.zgamelogic.app.servermanager.db.MinecraftServerData;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class MsmController {
    private final MsmServerService msmServerService;

    @Authenticated
    @PostMapping("/server/create")
    public void createServer(){
        System.out.println("Good");
    }

    @JsonView(Views.GeneralServerView.class)
    @GetMapping("/servers")
    public List<MinecraftServerDTO> getAllServerData(){
        return msmServerService.getMinecraftServerData();
    }
}
