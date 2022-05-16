package cc.x7f.gcauth.openid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import emu.grasscutter.Grasscutter;
import emu.grasscutter.auth.DefaultAuthentication;
import emu.grasscutter.plugin.Plugin;
import emu.grasscutter.server.http.HttpServer;

import cc.x7f.gcauth.openid.handler.*;
import cc.x7f.gcauth.openid.routes.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;


public class GCAuth extends Plugin {
    private static Config config;
    private File configFile;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onEnable() {
        configFile = new File(getDataFolder().toPath() + "/config.json");
        if (!configFile.exists()) {
            try {
                Files.createDirectories(configFile.toPath().getParent());
            } catch (IOException e) {
                getLogger().error("Failed to create config.json");
            }
        }
        loadConfig();
        Grasscutter.setAuthenticationSystem(new AuthenticationHandler());
        loadTwitterLogin();
        getLogger().info("GCAuth-OpenID Enabled!");
        saveConfig();
    }

    @Override
    public void onDisable() {
        Grasscutter.setAuthenticationSystem(new DefaultAuthentication());
    }

    public void loadConfig() {
        try (FileReader file = new FileReader(configFile)) {
            config = gson.fromJson(file, Config.class);
            saveConfig();
        } catch (Exception e) {
            config = new Config();
            saveConfig();
        }
    }

    public void saveConfig() {
        try (FileWriter file = new FileWriter(configFile)) {
            file.write(gson.toJson(config));
        } catch (Exception e) {
            getLogger().error("Unable to save config file.");
        }
    }

    public static Config getConfigStatic() {
        return config;
    }

    public Config getConfig() {
        return config;
    }

    public void loadTwitterLogin() {
        HttpServer app = Grasscutter.getHttpServer();
        app.addRouter(RedirectHandler.class);
        app.addRouter(OpenIDExternalAuthenticator.class);
    }
}
