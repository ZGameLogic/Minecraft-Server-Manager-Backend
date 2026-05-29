package com.zgamelogic.app.authentication;

import com.zgamelogic.app.exceptions.InvalidMsmTokenException;
import com.zgamelogic.app.user.db.UserData;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;

@Component
@AllArgsConstructor
public class AuthUserResolver implements HandlerMethodArgumentResolver {
    private final AuthenticationService authenticationService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return UserData.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public @Nullable Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        String tokenHeader = webRequest.getHeader("Authorization");
        HttpServletRequest nativeRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String cookieToken = nativeRequest.getCookies() != null ? Arrays.stream(nativeRequest.getCookies()).filter(c -> c.getName().equals("token")).findFirst().map(Cookie::getValue).orElse(null) : "";
        String token = tokenHeader != null && !tokenHeader.isEmpty() ? tokenHeader : cookieToken;
        if(token == null || token.isEmpty()) throw new InvalidMsmTokenException();
        return authenticationService.authorizeWithMSMToken(token, false).getUser();
    }
}
