package cc.x7f.gcauth.openid.handler;

import emu.grasscutter.game.Account;
import emu.grasscutter.auth.*;
import emu.grasscutter.auth.DefaultAuthenticators;
import emu.grasscutter.server.http.objects.ComboTokenResJson;
import emu.grasscutter.server.http.objects.LoginResultJson;

public class AuthenticationHandler implements AuthenticationSystem {
    private final Authenticator<LoginResultJson> noAuthAuthenticator = new NoAuthAuthenticator();
    private final Authenticator<LoginResultJson> tokenAuthenticator = new DefaultAuthenticators.TokenAuthenticator();
    private final Authenticator<ComboTokenResJson> sessionKeyAuthenticator = new DefaultAuthenticators.SessionKeyAuthenticator();
    private final ExternalAuthenticator externalAuthenticator = new DefaultAuthenticators.ExternalAuthentication();
    private final OAuthAuthenticator oAuthAuthenticator = new OIDCAuthenticator();

    @Override
    public void createAccount(String username, String password) {
        // Unhandled.
    }

    @Override
    public void resetPassword(String username) {
        // Unhandled.
    }

    @Override
    public Account verifyUser(String s) {
        return null;
    }

    @Override
    public Authenticator<LoginResultJson> getPasswordAuthenticator() {
        return noAuthAuthenticator;
    }

    @Override
    public Authenticator<LoginResultJson> getTokenAuthenticator() {
        return tokenAuthenticator;
    }

    @Override
    public Authenticator<ComboTokenResJson> getSessionKeyAuthenticator() {
        return sessionKeyAuthenticator;
    }

    @Override
    public ExternalAuthenticator getExternalAuthenticator() {
        return externalAuthenticator;
    }

    @Override
    public OAuthAuthenticator getOAuthAuthenticator() {
        return oAuthAuthenticator;
    }
}
