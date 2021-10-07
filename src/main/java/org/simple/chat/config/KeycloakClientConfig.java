package org.simple.chat.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakClientConfig {

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String secret;

    @Bean
    public Keycloak getKeycloak() {
        return KeycloakBuilder.builder()
            .serverUrl(serverUrl)
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .realm(realm)
            .clientId(clientId)
            .username("admin")
            .password("admin")
            .clientSecret(secret)
            .build();
    }

    @Bean(name = "realm")
    public String getRealm() {
        return realm;
    }

    @Bean(name = "serverUrl")
    public String getServerUrl() {
        return serverUrl;
    }

    @Bean(name = "clientId")
    public String getClientId() {
        return clientId;
    }

    @Bean(name = "secret")
    public String getSecret() {
        return secret;
    }

}





