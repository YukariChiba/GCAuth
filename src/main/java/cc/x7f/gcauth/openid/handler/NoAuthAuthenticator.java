package cc.x7f.gcauth.openid.handler;

import emu.grasscutter.auth.AuthenticationSystem;
import emu.grasscutter.auth.Authenticator;
import emu.grasscutter.server.http.objects.LoginResultJson;

public class NoAuthAuthenticator implements Authenticator<LoginResultJson> {

    @Override
    public LoginResultJson authenticate(AuthenticationSystem.AuthenticationRequest authenticationRequest) {
        var response = new LoginResultJson();
        response.retcode = -201;
        response.message = "Password login is forbidden.";
        return response;
    }
}
