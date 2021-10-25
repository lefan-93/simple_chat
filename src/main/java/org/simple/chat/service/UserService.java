package org.simple.chat.service;

import lombok.AllArgsConstructor;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.util.HttpResponseException;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.simple.chat.controller.exception.ConflictException;
import org.simple.chat.UserDao;
import org.simple.chat.model.dto.request.NewUser;
import org.simple.chat.model.dto.request.AuthenticationRequest;
import org.simple.chat.model.dto.response.FriendDto;
import org.simple.chat.model.dto.response.ProfileDto;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

    private final UserDao userDao;

    public void createUser(NewUser newUser) throws BadRequestException {

        var keycloakUser = prepareUserRepresentation(newUser);
        keycloak.tokenManager().getAccessToken();
        try (var response = keycloak.realm(realm).users().create(keycloakUser)) {
            if (response.getStatus() == 409) {
                if (!keycloak.realm(realm).users().search(newUser.getUsername()).isEmpty()) {
                    throw new ConflictException("user with username " + newUser.getUsername() + " already exist",
                        Response.Status.CONFLICT);
                }
                if (!keycloak.realm(realm).users().search(null,
                    null, null,
                    newUser.getEmail(),
                    0,
                    1).isEmpty()) {
                    throw new ConflictException("user with email " + newUser.getEmail() + " already exist",
                        Response.Status.CONFLICT);
                }
            }
        }

    }

    public List<FriendDto> findUser(String name) {
        return userDao.findUserByName(name);
    }

    public ProfileDto getMeProfile(String userId) {
        return userDao.findUserById(userId);
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
        var user = new UserRepresentation();
        user.setEnabled(true);
        user.setEmail(newUser.getEmail());
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