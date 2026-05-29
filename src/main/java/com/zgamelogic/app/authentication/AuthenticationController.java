package com.zgamelogic.app.authentication;

import com.fasterxml.jackson.annotation.JsonView;
import com.zgamelogic.app.authentication.db.AuthenticationData;
import com.zgamelogic.app.exceptions.InvalidMsmTokenException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @JsonView(Views.AuthView.class)
    @PostMapping("/auth/code")
    public AuthenticationData authorizeWithDiscordCode(@RequestBody CodeAuthenticationRequest req, HttpServletResponse response){
        AuthenticationData authData = authenticationService.authorizeWithDiscordCode(req.code(), req.redirectUrl());
        Cookie authCookie = new Cookie("token", authData.getMsmToken());
        authCookie.setHttpOnly(true);
        authCookie.setPath("/");
        authCookie.setMaxAge(60 * 60 * 24 * 365 * 10); // 10 years
        response.addCookie(authCookie);

        return authData;
    }

    @JsonView(Views.AuthView.class)
    @PostMapping("/auth/token")
    public AuthenticationData authorizeWithMsmToken(
            @RequestBody(required = false) String bodyToken,
            @CookieValue(value = "token", required = false) String cookieToken
    ){
        String token = bodyToken != null && !bodyToken.isEmpty() ? bodyToken : cookieToken;
        if(token == null || token.isEmpty()) throw new InvalidMsmTokenException();
        return authenticationService.authorizeWithMSMToken(token, true);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(
            @RequestBody(required = false) String bodyToken,
            @CookieValue(value = "token", required = false) String cookieToken
    ){
        String token = bodyToken != null && !bodyToken.isEmpty() ? bodyToken : cookieToken;
        if(token == null || token.isEmpty()) throw new InvalidMsmTokenException();
        authenticationService.revokeWithMsmToken(token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
