package org.simple.chat.service;

import lombok.AllArgsConstructor;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.util.HttpResponseException;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.simple.chat.model.dto.NewUser;
import org.simple.chat.model.dto.request.AuthenticationRequest;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Service
@AllArgsConstructor
public class UserService {

    private static final String SECRET_PARAMETER = "secret";
    private static final String GRANT_TYPE_PARAMETER = "grant_type";
    private static final String PASSWORD = "password";

    private final Keycloak keycloak;

    private final String serverUrl;
    private final String realm;
    private final String clientId;
    private final String secret;

    public void createUser(NewUser newUser) {

        UserRepresentation keycloakUser = prepareUserRepresentation(newUser);
        keycloak.tokenManager().getAccessToken();
        Response response = keycloak.realm(realm).users().create(keycloakUser);

    }

    public AccessTokenResponse auth(AuthenticationRequest auth) throws HttpResponseException {

        Map<String, Object> clientCredentials = new HashMap<>();
        clientCredentials.put(SECRET_PARAMETER, secret);
        clientCredentials.put(GRANT_TYPE_PARAMETER, PASSWORD);

        var keycloakClientConfiguration = new Configuration(serverUrl, realm, clientId, clientCredentials, null);
        var authClient = AuthzClient.create(keycloakClientConfiguration);

        return authClient.obtainAccessToken(auth.getUsername(), auth.getPassword());
    }

    private UserRepresentation prepareUserRepresentation(NewUser newUser) {
        var password = preparePasswordRepresentation(newUser.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(newUser.getUsername());
        user.setCredentials(Collections.singletonList(password));
        return user;
    }

    private CredentialRepresentation preparePasswordRepresentation(String userPassword) {
        var passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(userPassword);
        return passwordCred;
    }
}