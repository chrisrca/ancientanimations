package chrisrca.ancientanimations.config;

import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;

public class Config {
    // Swing animation
    public double swingTransX = 0.240;
    public double swingTransY = 0.128;
    public double swingTransZ = -0.052;
    public double swingRotX = -65;
    public double swingRotY = 25;
    public double swingRotZ = -7.5;
    public double swingRotY2 = 50;

    // Hand item transform
    public double itemPosX = 0.0;
    public double itemPosY = 0.0;
    public double itemPosZ = 0.0;
    public double itemRotX = 0.0;
    public double itemRotY = 0.0;
    public double itemRotZ = 0.0;
    public double itemScale = 1.0;

    // General
    public double swingSpeedMultiplier = 1.0;

    public static Config load() throws IOException {
        var configFile = FabricLoader.getInstance().getConfigDir().resolve("ancientanimations.json");
        var gson = new Gson();
        if (!Files.exists(configFile)) {
            save(new Config());
        }
        return gson.fromJson(Files.newBufferedReader(configFile), Config.class);
    }

    public static void save(Config config) throws IOException {
        var configFile = FabricLoader.getInstance().getConfigDir().resolve("ancientanimations.json");
        var gson = new Gson();
        var writer = gson.newJsonWriter(Files.newBufferedWriter(configFile));
        writer.setIndent("    ");
        gson.toJson(gson.toJsonTree(config, Config.class), writer);
        writer.close();
    }
}