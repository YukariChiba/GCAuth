package cc.x7f.gcauth.openid.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import cc.x7f.gcauth.openid.utils.JWTTools;
import cc.x7f.gcauth.openid.utils.OAuth;
import emu.grasscutter.server.http.objects.LoginResultJson;
import emu.grasscutter.Grasscutter;
import emu.grasscutter.auth.AuthenticationSystem.AuthenticationRequest;
import emu.grasscutter.auth.OAuthAuthenticator;
import emu.grasscutter.game.Account;
import cc.x7f.gcauth.openid.GCAuth;
import cc.x7f.gcauth.openid.json.VerifyJson;

import emu.grasscutter.database.DatabaseHelper;

import express.http.Request;
import express.http.Response;

public final class OIDCAuthenticator implements OAuthAuthenticator {

    static void reject(Request req, Response res, String reason) {
        LoginResultJson responseData = new LoginResultJson();
        Grasscutter.getLogger().info("Client " + req.ip() + " failed to log in");
        responseData.retcode = -201;
        responseData.message = reason;
        res.send(responseData);
    }

    static void accept(Request req, Response res, Account account) {
        LoginResultJson responseData = new LoginResultJson();
        responseData.message = "OK";
        responseData.data.account.uid = account.getId();
        responseData.data.account.token = account.generateSessionKey();
        responseData.data.account.email = account.getEmail();
        responseData.data.account.twitter_name = account.getUsername();
        Grasscutter.getLogger().info(
                String.format("Client %s logged in as %s", req.ip(),
                        responseData.data.account.uid));
        res.send(responseData);
    }


    @Override
    public void handleLogin(AuthenticationRequest authenticationRequest) {
        Request req = authenticationRequest.getRequest();
        Response res = authenticationRequest.getResponse();
        VerifyJson verifyjson = req.body(VerifyJson.class);
        String accessCode = verifyjson.access_token;
        String authresult = null;
        try {
            authresult = OAuth.auth(accessCode);
        } catch (Exception err) {
            reject(req, res, "Server error.");
            return;
        }
        if (authresult == null) {
            reject(req, res, "Invalid credential.");
            return;
        }
        String id_token = new Gson().fromJson(authresult, JsonObject.class).get("id_token").getAsString();
        String username = JWTTools.getClaim(id_token, GCAuth.getConfigStatic().username_claim);
        Account account = DatabaseHelper.getAccountByName(username);
        if (account == null) {
            account = DatabaseHelper.createAccount(username);
            Grasscutter.getLogger()
                    .info(String.format("Client %s registered as %s", req.ip(), account.getId()));
        }
        accept(req, res, account);
    }

    @Override
    public void handleDesktopRedirection(AuthenticationRequest authenticationRequest) {
        String Login_Url = (GCAuth.getConfigStatic().auth_endpoint + "?response_type=code&scope=openid&redirect_uri="
                + GCAuth.getConfigStatic().redirect_uri + "&client_id=" + GCAuth.getConfigStatic().client_id);
        Response res = authenticationRequest.getResponse();
        res.set("server", "tsa_m");
        res.set("Content-Type", "application/json; charset=utf-8");
        res.set("access-control-allow-credentials", "true");
        res.set("access-control-allow-origin", "https://account.hoyoverse.com");
        res.send(String.format(
                "{\"code\":200,\"data\":{\"auth_url\":\"%s\",\"info\":\"\",\"msg\":\"Success\",\"status\":1}}",
                Login_Url));
    }

    @Override
    public void handleMobileRedirection(AuthenticationRequest authenticationRequest) {
        String Login_Url = (GCAuth.getConfigStatic().auth_endpoint + "?response_type=code&scope=openid&redirect_uri="
                + GCAuth.getConfigStatic().redirect_uri + "&client_id=" + GCAuth.getConfigStatic().client_id);
        authenticationRequest.getResponse().send(String.format("<meta http-equiv=\"refresh\" content=\"0;url=%s\">", Login_Url));
    }

    @Override
    public void handleTokenProcess(AuthenticationRequest authenticationRequest) {
        authenticationRequest.getResponse().send(
            String.format(
                    "<meta http-equiv=\"refresh\" content=\"0;url=uniwebview://sdkThirdLogin?accessToken=%s\">",
                    authenticationRequest.getRequest().query("code")));
        
    }
}