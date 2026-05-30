package com.zgamelogic.app.servermanager;

import com.zgamelogic.app.authentication.db.Authenticated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MsmController {
    @Authenticated
    @PostMapping("/server/create")
    public void createServer(){
        System.out.println("Good");
    }
}
