package com.zgamelogic.app.authentication;

import com.fasterxml.jackson.annotation.JsonView;
import com.zgamelogic.app.authentication.db.AuthenticationData;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @JsonView(Views.AuthView.class)
    @PostMapping("/auth/code")
    public AuthenticationData authorizeWithDiscordCode(@RequestBody CodeAuthenticationRequest req){
        return authenticationService.authorizeWithDiscordCode(req.code(), req.redirectUrl());
    }

    @JsonView(Views.AuthView.class)
    @PostMapping("/auth/token")
    public AuthenticationData authorizeWithMsmToken(@RequestBody String token){
        return authenticationService.authorizeWithMSMToken(token, true);
    }
}
