package cc.fascinated.simplespawn.command;

import cc.fascinated.simplespawn.SimpleSpawn;
import cc.fascinated.simplespawn.spawn.SpawnManager;
import cc.fascinated.simplespawn.utils.io.LangOption;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Project: SimpleSpawn
 * Created by Fascinated#4735 on 25/06/2021
 */
public class SetSpawnCommand implements CommandExecutor {

    public SetSpawnCommand() {
        Objects.requireNonNull(SimpleSpawn.getINSTANCE().getCommand("setspawn")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        if (!player.hasPermission("simplespawn.command.setspawn")) {
            player.sendMessage(LangOption.PREFIX.get() + LangOption.NO_PERMISSION.get());
            return true;
        }

        SpawnManager spawnManager = SimpleSpawn.getSpawnManager();
        Location loc = player.getLocation();

        spawnManager.setSpawn(loc);
        spawnManager.save();
        player.sendMessage(LangOption.PREFIX.get() + LangOption.SET_SPAWN.get()
                .replaceAll("%x%", String.valueOf(loc.getBlockX()))
                .replaceAll("%y%", String.valueOf(loc.getBlockY()))
                .replaceAll("%z%", String.valueOf(loc.getBlockZ())));
        return false;
    }
}
