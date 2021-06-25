package cc.fascinated.simplespawn.utils.io;

import cc.fascinated.simplespawn.SimpleSpawn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/*
 * Created by NoneTaken on 24/11/2020 17:14
 */
@Getter
@AllArgsConstructor
public enum LangOption {

    PREFIX("messages.prefix"),
    SIMPLESPAWN_HELP("messages.simplespawn.help"),
    SIMPLESPAWN_RELOAD("messages.simplespawn.reload"),
    INVALID_SPAWN("messages.invalid-spawn"),
    ALREADY_TELEPORTING("messages.already-teleporting"),
    ON_COOLDOWN("messages.on-cooldown"),
    SET_SPAWN("messages.set-spawn"),
    NO_PERMISSION("messages.no-permission"),
    TITLE_HEADER("titles.header"),
    TITLE_TELEPORTING_IN("titles.teleporting-in"),
    TITLE_TELEPORTED("titles.teleporting"),
    TITLE_TELEPORT_FAILED("titles.teleport-failed");

    private final String path;

    private static final HashMap<LangOption, String> resultCache = new HashMap<>();
    private static final HashMap<LangOption, Boolean> booleanResultCache = new HashMap<>();
    private static final HashMap<LangOption, List<String>> resultListCache = new HashMap<>();

    /**
     * Get the setting in the config file for {@link LangOption#path}
     * This can also be used to refresh the {@link LangOption#resultCache} map
     *
     * @return - The string corresponding to this setting in the config file
     */
    public String get() {
        return resultCache.computeIfAbsent(this, val -> ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(SimpleSpawn.getConfiguration().getConfiguration().getString(this.path))));
    }

    /**
     * Get the setting in the config file for {@link LangOption#path}
     * This can also be used to refresh the {@link LangOption#booleanResultCache} map
     *
     * @return - The boolean corresponding to this setting in the config file
     */
    public Boolean getAsBoolean() {
        return booleanResultCache.computeIfAbsent(this, val -> SimpleSpawn.getConfiguration().getConfiguration().getBoolean(this.path));
    }

    public List<String> getAsList() {
        return resultListCache.computeIfAbsent(this, val -> {
            List<String> newList = new ArrayList<>();
            for (String line : SimpleSpawn.getConfiguration().getConfiguration().getStringList(this.path)) {
                newList.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            return newList;
        });
    }

    public static String getValue(LangOption option) {
        return option.get();
    }

    public static void clear() {
        resultCache.clear();
        booleanResultCache.clear();
        resultListCache.clear();
    }
}
