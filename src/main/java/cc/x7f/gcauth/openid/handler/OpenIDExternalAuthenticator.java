package cc.x7f.gcauth.openid.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import cc.x7f.gcauth.openid.utils.JWTTools;
import cc.x7f.gcauth.openid.utils.OAuth;
import emu.grasscutter.server.http.Router;
import emu.grasscutter.server.http.objects.LoginResultJson;
import emu.grasscutter.Grasscutter;
import emu.grasscutter.game.Account;
import cc.x7f.gcauth.openid.json.VerifyJson;

import emu.grasscutter.database.DatabaseHelper;

import express.Express;
import express.http.Request;
import express.http.Response;

import io.javalin.Javalin;

public final class OpenIDExternalAuthenticator implements Router {

    @Override
    public void applyRoutes(Express express, Javalin javalin) {
        express.post("/hk4e_global/mdk/shield/api/loginByThirdparty", OpenIDExternalAuthenticator::handle);
        express.get("/authentication/openid/redirect", OpenIDExternalAuthenticator::redirect);
    }

    public static void redirect(Request req, Response res) {
        res.send(
                String.format(
                        "<meta http-equiv=\"refresh\" content=\"0;url=uniwebview://sdkThirdLogin?accessToken=%s\">",
                        req.query("code")));
    }

    static void reject(Request req, Response res) {
        LoginResultJson responseData = new LoginResultJson();
        Grasscutter.getLogger().info("[GCAuth] Client " + req.ip() + " failed to log in");
        responseData.retcode = -201;
        responseData.message = "Invalid credential";
        res.send(responseData);
    }

    public static void handle(Request req, Response res) {
        LoginResultJson responseData = new LoginResultJson();
        VerifyJson verifyjson = req.body(VerifyJson.class);
        String accessCode = verifyjson.access_token;
        String authresult = null;
        try {
            authresult = OAuth.auth(accessCode);
        } catch (Exception err) {
            Grasscutter.getLogger().info(err.getMessage());
            reject(req, res);
            return;
        }
        if (authresult == null) {
            reject(req, res);
            return;
        }
        String id_token = new Gson().fromJson(authresult, JsonObject.class).get("id_token").getAsString();
        String email = JWTTools.decode(id_token);
        Account account = DatabaseHelper.getAccountByName(email);
        if (account == null) {
            account = DatabaseHelper.createAccountWithPassword(email, "");
            Grasscutter.getLogger()
                    .info(String.format("[GCAuth-OpenID] Client %s registered as %s", req.ip(), account.getId()));
        }
        responseData.message = "OK";
        responseData.data.account.uid = account.getId();
        responseData.data.account.token = account.generateSessionKey();
        responseData.data.account.email = account.getEmail();
        responseData.data.account.twitter_name = account.getUsername();
        Grasscutter.getLogger().info(
                String.format("[GCAuth-OpenID] Client %s logged in as %s", req.ip(),
                        responseData.data.account.uid));
        res.send(responseData);

    }
}