package cc.x7f.gcauth.openid.routes;

import emu.grasscutter.Grasscutter;
import emu.grasscutter.server.http.Router;
import static emu.grasscutter.Configuration.*;

import cc.x7f.gcauth.openid.GCAuth;
import express.Express;
import express.http.Request;
import express.http.Response;

import io.javalin.Javalin;

public final class RedirectHandler implements Router {

    @Override
    public void applyRoutes(Express express, Javalin javalin) {
        express.get("/Api/twitter_login", RedirectHandler::handleJson);
    }

    public static void handleJson(Request req, Response res) {
        String Login_Url = (GCAuth.getConfigStatic().auth_endpoint + "?response_type=code&scope=openid&redirect_uri="
                + GCAuth.getConfigStatic().redirect_uri + "&client_id=" + GCAuth.getConfigStatic().client_id);
        res.set("server", "tsa_m");
        res.set("Content-Type", "application/json; charset=utf-8");
        res.set("access-control-allow-credentials", "true");
        res.set("access-control-allow-origin", "https://account.hoyoverse.com");
        res.send(String.format(
                "{\"code\":200,\"data\":{\"auth_url\":\"%s\",\"info\":\"\",\"msg\":\"Success\",\"status\":1}}",
                Login_Url));
    }
}