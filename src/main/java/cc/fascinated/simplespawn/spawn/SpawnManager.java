package cc.fascinated.simplespawn.spawn;

import cc.fascinated.simplespawn.SimpleSpawn;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Project: SimpleSpawn
 * Created by Fascinated#4735 on 25/06/2021
 */
public class SpawnManager implements Listener {

    FileConfiguration config = SimpleSpawn.getConfiguration().getConfiguration();
    private Location spawn;

    public SpawnManager() {
        SimpleSpawn.getINSTANCE().getServer().getPluginManager().registerEvents(this, SimpleSpawn.getINSTANCE());

        String loc = config.getString("config.location");
        if (loc != null && !loc.equals("{}")) {
            spawn = SimpleSpawn.getGSON().fromJson(loc, Location.class);
        }
    }

    public void setSpawn(Location location) {
        spawn = location;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void save() {
        config.set("config.location", SimpleSpawn.getGSON().toJson(spawn));
        SimpleSpawn.getConfiguration().saveConfig();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPlayedBefore()) {
            if (spawn != null) {
                event.getPlayer().teleport(spawn);
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (spawn != null) {
            event.setRespawnLocation(spawn);
        }
    }
}
