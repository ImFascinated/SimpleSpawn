package cc.fascinated.simplespawn.command;

import cc.fascinated.simplespawn.SimpleSpawn;
import cc.fascinated.simplespawn.utils.io.LangOption;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Project: SimpleSpawn
 * Created by Fascinated#4735 on 25/06/2021
 */
public class SimpleSpawnCommand implements CommandExecutor {

    public SimpleSpawnCommand() {
        Objects.requireNonNull(SimpleSpawn.getINSTANCE().getCommand("simplespawn")).setExecutor(this);
        Objects.requireNonNull(SimpleSpawn.getINSTANCE().getCommand("simplespawn")).setTabCompleter(new CommandTabCompleter());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        if (!player.hasPermission("simplespawn.command.simplespawn")) {
            player.sendMessage(LangOption.PREFIX.get() + LangOption.NO_PERMISSION.get());
            return true;
        }

        if (args.length < 1) {
            List<String> messages = LangOption.SIMPLESPAWN_HELP.getAsList();
            for (String message : messages) {
                player.sendMessage(message);
            }
            return true;
        }

        String arg = args[0].toLowerCase();
        switch (arg) {
            case "reload": {
                SimpleSpawn.getConfiguration().reloadConfig();
                LangOption.clear();
                player.sendMessage(LangOption.PREFIX.get() + LangOption.SIMPLESPAWN_RELOAD.get());
                break;
            }
        }
        return true;
    }

    public static class CommandTabCompleter implements TabCompleter {

        private final String[] COMMANDS = { "reload" };

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
            if (args.length <= 1) {
                final List<String> completions = new ArrayList<>();
                for (String arg : COMMANDS) {
                    completions.add(arg);
                    if (arg.toLowerCase().startsWith(args[0].toLowerCase())) {
                        completions.add(arg);
                    }
                }
                Collections.sort(completions);
                return completions;
            }
            return null;
        }
    }
}
