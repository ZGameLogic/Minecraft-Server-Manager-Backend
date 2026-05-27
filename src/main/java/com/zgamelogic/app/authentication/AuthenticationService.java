package com.zgamelogic.app.authentication;

import com.zgamelogic.app.authentication.db.AuthenticationDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final AuthenticationDataRepository authenticationDataRepository;

    public void authorizeWithDiscordCode(String code){

    }

    public void authorizeWithMSMToken(String token){

    }

    @Scheduled(cron = "0 */5 * * * *")
    private void refreshDiscordTokens(){

    }
}
