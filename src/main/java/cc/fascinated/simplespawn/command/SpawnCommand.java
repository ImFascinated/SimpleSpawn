package cc.fascinated.simplespawn.command;

import cc.fascinated.simplespawn.SimpleSpawn;
import cc.fascinated.simplespawn.utils.Utils;
import cc.fascinated.simplespawn.utils.io.LangOption;
import com.sun.tools.javac.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Project: SimpleSpawn
 * Created by Fascinated#4735 on 25/06/2021
 */
public class SpawnCommand implements CommandExecutor {

    private final FileConfiguration config = SimpleSpawn.getConfiguration().getConfiguration();
    private final Map<UUID, Integer> timeLeft = new HashMap<>();
    private final Map<UUID, Location> locations = new HashMap<>();
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public SpawnCommand() {
        Objects.requireNonNull(SimpleSpawn.getINSTANCE().getCommand("spawn")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        Location spawn = SimpleSpawn.getSpawnManager().getSpawn();

        if (!player.hasPermission("simplespawn.command.spawn")) {
            player.sendMessage(LangOption.PREFIX.get() + LangOption.NO_PERMISSION.get());
            return true;
        }

        if (spawn == null) {
            player.sendMessage(LangOption.PREFIX.get() + LangOption.INVALID_SPAWN.get());
            return true;
        }

        if (cooldowns.get(player.getUniqueId()) != null) {
            player.sendMessage(LangOption.PREFIX.get() + LangOption.ON_COOLDOWN.get()
                    .replaceAll("%timeLeft%", Utils.formatTime(cooldowns.get(player.getUniqueId()) - System.currentTimeMillis(), true)));
            return true;
        }

        if (timeLeft.get(player.getUniqueId()) != null) {
            player.sendMessage(LangOption.PREFIX.get() + LangOption.ALREADY_TELEPORTING.get());
            return true;
        }

        AtomicInteger countdownTime = new AtomicInteger(config.getInt("config.teleport-delay"));
        if (countdownTime.get() > 0) {
            cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + (config.getInt("config.cooldown") * 1000));
            Bukkit.getScheduler().scheduleSyncDelayedTask(SimpleSpawn.getINSTANCE(),
                    () -> cooldowns.remove(player.getUniqueId()),
                    config.getInt("config.cooldown") * 20
            );
            locations.put(player.getUniqueId(), player.getLocation());
            timeLeft.put(player.getUniqueId(), Bukkit.getScheduler().scheduleSyncRepeatingTask(SimpleSpawn.getINSTANCE(), () -> {
                Location oldLoc = locations.get(player.getUniqueId());
                Location newLoc = player.getLocation();
                if (oldLoc.getBlockX() != newLoc.getBlockX() || oldLoc.getBlockY() != newLoc.getBlockY() || oldLoc.getBlockZ() != newLoc.getBlockZ()) {
                    player.sendTitle(LangOption.TITLE_HEADER.get(), LangOption.TITLE_TELEPORT_FAILED.get(), 5, 20, 5);
                    Bukkit.getScheduler().cancelTask(timeLeft.get(player.getUniqueId()));
                    timeLeft.remove(player.getUniqueId());
                    return;
                }
                player.sendTitle(LangOption.TITLE_HEADER.get(), LangOption.TITLE_TELEPORTING_IN.get()
                        .replaceAll("%time%", String.valueOf(countdownTime.getAndDecrement()))
                , 0, 30, 10);
                if (countdownTime.intValue() == -1) {
                    player.teleport(spawn);
                    player.sendTitle(LangOption.TITLE_HEADER.get(), LangOption.TITLE_TELEPORTED.get());
                    Bukkit.getScheduler().cancelTask(timeLeft.get(player.getUniqueId()));
                    timeLeft.remove(player.getUniqueId());
                }
            }, 0L, 20L));
        }

        return true;
    }
}
