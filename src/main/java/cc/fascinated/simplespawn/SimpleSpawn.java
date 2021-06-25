package cc.fascinated.simplespawn;

import cc.fascinated.simplespawn.command.SetSpawnCommand;
import cc.fascinated.simplespawn.command.SimpleSpawnCommand;
import cc.fascinated.simplespawn.command.SpawnCommand;
import cc.fascinated.simplespawn.spawn.SpawnManager;
import cc.fascinated.simplespawn.utils.io.Config;
import cc.fascinated.simplespawn.utils.io.LangOption;
import cc.fascinated.simplespawn.utils.io.serializers.LocationSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleSpawn extends JavaPlugin {

    @Getter private static SimpleSpawn INSTANCE;
    @Getter private static Gson GSON;
    @Getter private static SpawnManager spawnManager;
    @Getter private static Config configuration;

    @Override
    public void onEnable() {
        INSTANCE = this;
        GSON = new GsonBuilder()
            .registerTypeAdapter(Location.class, new LocationSerializer())
            .setPrettyPrinting()
            .create();

        configuration = new Config(this, "config.yml", null);
        configuration.saveDefaultConfig();

        spawnManager = new SpawnManager();
        new SimpleSpawnCommand();
        new SpawnCommand();
        new SetSpawnCommand();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp()) {
                if (!LangOption.DISABLE_LOAD_MESSAGE.getAsBoolean()) {
                    player.sendMessage(LangOption.PREFIX.get() + "§aThis server is running SimpleSpawn §7v" + this.getDescription().getVersion());
                }
            }
        }
    }
}
