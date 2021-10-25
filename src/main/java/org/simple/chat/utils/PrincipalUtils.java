package org.simple.chat.utils;

import lombok.experimental.UtilityClass;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@UtilityClass
public class PrincipalUtils {

    public static String getUserId() {
        AccessToken token = getToken(getKeycloakPrincipal());
        if (nonNull(token)) {
            return token.getSubject();
        }
        return null;
    }

    private static KeycloakAuthenticationToken getKeycloakPrincipal() {
        ServletRequestAttributes requestAttributes
            = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        var httpServletRequest = Objects.requireNonNull(requestAttributes).getRequest();
        return (KeycloakAuthenticationToken) httpServletRequest.getUserPrincipal();
    }

    private static AccessToken getToken(KeycloakAuthenticationToken principal) {
        if (isNull(principal)) {
            return null;
        }
        var context = principal.getAccount().getKeycloakSecurityContext();
        if (isNull(context)) {
            return null;
        }
        return context.getToken();
    }
}
