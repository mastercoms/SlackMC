package org.circuitsoft.slackmc.plugin.bukkit.filters;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SlackFilter {

    public SlackFilter(boolean blacklist) {
    }

    /**
     * Get the object to filter against from a string.
     * @param plugin The plugin in order to interact with the server for information.
     * @param data The string representing the data that can be filtered by this filter.
     * @return The Java Object that may be used by the filter.
     */
    public abstract Object getObject(JavaPlugin plugin, String data);

    public abstract void setObject(Object data);

    /**
     * Checks to see if the player passes the filter and should have its message sent.
     * @param player The player
     * @return If the player passes the filter.
     */
    public abstract boolean matches(Player player);

    /**
     * Checks to see if the player passes the filter and should have its message sent.
     * This method also provides an opportunity to give the filter extra information to
     * make its decision. What the extra data means is determined by each filter.
     * @param player The player
     * @param player Some extra data to give context to a player's actions.
     * @return If the player passes the filter.
     */
    public abstract boolean matches(Player player, String extra);

    /**
     * Convenience method for comparing two objects of the same type, with respect to if the filter is a blacklist.
     * @param playerData The data we're filtering.
     * @param filterData The data we're filtering against.
     * @param blacklist If the filter is a blacklist.
     * @return If the player passes the filter.
     */
    public static boolean matches(Object playerData, Object filterData, boolean blacklist) {
        return playerData.equals(filterData) ^ blacklist;
    }
}
