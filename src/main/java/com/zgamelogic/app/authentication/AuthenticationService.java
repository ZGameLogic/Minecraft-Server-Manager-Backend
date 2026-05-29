package com.zgamelogic.app.authentication;

import com.zgamelogic.app.authentication.db.AuthenticationData;
import com.zgamelogic.app.authentication.db.AuthenticationDataRepository;
import com.zgamelogic.app.discord.DiscordAuthenticationResponse;
import com.zgamelogic.app.discord.DiscordService;
import com.zgamelogic.app.discord.DiscordUserResponse;
import com.zgamelogic.app.exceptions.InvalidDiscordCodeException;
import com.zgamelogic.app.exceptions.InvalidDiscordTokenException;
import com.zgamelogic.app.exceptions.InvalidMsmTokenException;
import com.zgamelogic.app.user.db.UserEntity;
import com.zgamelogic.app.user.db.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class AuthenticationService {
    private final DiscordService discordService;
    private final AuthenticationDataRepository authenticationDataRepository;
    private final UserRepository userRepository;

    public AuthenticationData authorizeWithDiscordCode(String code, String redirectUrl) {
        DiscordAuthenticationResponse authRes = discordService.authorizeWithDiscordCode(code, redirectUrl).orElseThrow(InvalidDiscordCodeException::new);
        DiscordUserResponse userRes = discordService.getDiscordUserFromToken(authRes.accessToken()).orElseThrow(InvalidDiscordTokenException::new);
        UserEntity user = new UserEntity(userRes.id(), userRes.username(), userRes.avatar());
        if(userRepository.findById(userRes.id()).isEmpty()) user = userRepository.save(user);
        AuthenticationData authData = new AuthenticationData(
            generateToken(),
            authRes.accessToken(),
            authRes.refreshToken(),
            Instant.now().plusSeconds(authRes.expiresIn()),
            user
        );
        return authenticationDataRepository.save(authData);
    }

    public AuthenticationData authorizeWithMSMToken(String token, boolean updateAuthData){
        AuthenticationData authData = authenticationDataRepository.findByMsmToken(token).orElseThrow(InvalidMsmTokenException::new);
        if(updateAuthData) {
            DiscordUserResponse userRes = discordService.getDiscordUserFromToken(authData.getDiscordToken()).orElseThrow(InvalidDiscordTokenException::new);
            UserEntity user = new UserEntity(userRes.id(), userRes.username(), userRes.avatar());
            user = userRepository.save(user);
            authData.setUser(user);
        }
        return authData;
    }

    @Scheduled(cron = "0 */5 * * * *")
    @EventListener(ApplicationReadyEvent.class)
    private void refreshDiscordTokens(){
        authenticationDataRepository.findAllExpiringBetweenNowAndSixMinutes(Instant.now().plusSeconds(360)).forEach(authData ->
            discordService.authorizeWithDiscordRefreshToken(authData.getDiscordRefreshToken()).ifPresentOrElse(authRes -> {
                authData.setDiscordRefreshToken(authRes.refreshToken());
                authData.setDiscordToken(authRes.accessToken());
                authData.setDiscordTokenExpiration(Instant.now().plusSeconds(authRes.expiresIn()));
                authenticationDataRepository.save(authData);
            }, () -> authenticationDataRepository.delete(authData))
        );
    }

    private String generateToken(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    public void revokeWithMsmToken(String token) {
        AuthenticationData authData = authenticationDataRepository.findByMsmToken(token).orElseThrow(InvalidMsmTokenException::new);
        discordService.revokeToken(authData.getDiscordToken());
        authenticationDataRepository.delete(authData);
    }
}
