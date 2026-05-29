package com.zgamelogic.app.configs;

import com.zgamelogic.app.authentication.AuthUserResolver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Component
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AuthUserResolver authUserResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authUserResolver);
    }
}
