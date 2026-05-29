package com.zgamelogic.app.authentication;

import com.zgamelogic.app.authentication.db.Authenticated;
import com.zgamelogic.app.exceptions.InvalidMsmTokenException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
@AllArgsConstructor
public class AuthenticationAdvice {
    private final AuthenticationService authenticationService;

    @Around("@annotation(authenticated)")
    public Object authenticate(ProceedingJoinPoint joinPoint, Authenticated authenticated) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) throw new InvalidMsmTokenException();
        HttpServletRequest request = attributes.getRequest();
        String tokenHeader = request.getHeader("Authorization");
        String cookieToken = request.getCookies() != null ? Arrays.stream(request.getCookies()).filter(c -> c.getName().equals("token")).findFirst().map(Cookie::getValue).orElse(null) : "";
        String token = tokenHeader != null && !tokenHeader.isEmpty() ? tokenHeader : cookieToken;
        if(token == null || token.isEmpty()) throw new InvalidMsmTokenException();
        authenticationService.authorizeWithMSMToken(token, false);

        return joinPoint.proceed();
    }

}
