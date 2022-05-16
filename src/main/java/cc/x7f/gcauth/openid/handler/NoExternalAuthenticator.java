package cc.x7f.gcauth.openid.handler;

import emu.grasscutter.auth.AuthenticationSystem;
import emu.grasscutter.auth.ExternalAuthenticator;
import express.http.Response;
import cc.x7f.gcauth.openid.json.AuthResponseJson;

public class NoExternalAuthenticator implements ExternalAuthenticator {
    @Override
    public void handleLogin(AuthenticationSystem.AuthenticationRequest authenticationRequest) {
        AuthResponseJson authResponse = new AuthResponseJson();
        Response response = authenticationRequest.getResponse();
        assert response != null; // This should never be null.
        authResponse.success = false;
        authResponse.message = "DISABLED";
        authResponse.jwt = "";
        response.send(authResponse);
    }

    @Override
    public void handleAccountCreation(AuthenticationSystem.AuthenticationRequest authenticationRequest) {
        AuthResponseJson authResponse = new AuthResponseJson();
        Response response = authenticationRequest.getResponse();
        assert response != null; // This should never be null.
        authResponse.success = false;
        authResponse.message = "DISABLED";
        authResponse.jwt = "";
        response.send(authResponse);
    }

    @Override
    public void handlePasswordReset(AuthenticationSystem.AuthenticationRequest authenticationRequest) {
        AuthResponseJson authResponse = new AuthResponseJson();
        Response response = authenticationRequest.getResponse();
        assert response != null; // This should never be null.
        authResponse.success = false;
        authResponse.message = "DISABLED";
        authResponse.jwt = "";
        response.send(authResponse);
    }
}
