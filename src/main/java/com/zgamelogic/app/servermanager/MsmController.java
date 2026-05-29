package com.zgamelogic.app.servermanager;

import com.zgamelogic.app.user.db.UserData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MsmController {
    @PostMapping("/server/create")
    public void createServer(UserData user){
        System.out.println(user);
    }
}
