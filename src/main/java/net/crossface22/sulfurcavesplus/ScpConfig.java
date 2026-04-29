package net.crossface22.sulfurcavesplus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ScpConfig {

    private static final Path PATH =
            FabricLoader.getInstance().getConfigDir().resolve("sulfurcavesplus.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static ScpConfig INSTANCE = load();

    // Corrosion Effect
    public int corrosionTickInterval = 5;
    public int durabilityDamagePerTick = 1;

    // Sulfur Spider
    public int spiderCorrosionDurationEasy = 0;
    public int spiderCorrosionDurationNormal = 140;
    public int spiderCorrosionDurationHard = 280;

    // Potent Sulfur Gas
    public int potentSulfurEffectDuration = 80;

    // Worldgen
    public boolean sulfurVeinsGenerateUnderBasaltDeltas = true;

    private static ScpConfig load() {
        if (Files.exists(PATH)) {
            try (Reader r = Files.newBufferedReader(PATH)) {
                ScpConfig loaded = GSON.fromJson(r, ScpConfig.class);
                if (loaded != null) return loaded;
            } catch (IOException e) {
                SulfurCavesPlus.LOGGER.warn("[SCP] Could not load config: {}", e.getMessage());
            }
        }
        ScpConfig defaults = new ScpConfig();
        defaults.save();
        return defaults;
    }

    public void save() {
        try (Writer w = Files.newBufferedWriter(PATH)) {
            GSON.toJson(this, w);
        } catch (IOException e) {
            SulfurCavesPlus.LOGGER.warn("[SCP] Could not save config: {}", e.getMessage());
        }
    }
}
