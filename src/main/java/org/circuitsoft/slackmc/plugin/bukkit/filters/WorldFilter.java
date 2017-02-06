package org.circuitsoft.slackmc.plugin.bukkit.filters;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldFilter extends SlackFilter {

    private boolean blacklist;
    private World world;

    public WorldFilter(boolean blacklist) {
        super(blacklist);
        this.blacklist = blacklist;
    }

    public Object getObject(JavaPlugin plugin, String data) {
        return Bukkit.getWorld(data);
    }

    @Override
    public void setObject(Object data) {
        world = (World) data;
    }

    @Override
    public boolean matches(Player player) {
        return matches(player.getWorld(), world, blacklist);
    }

    @Override
    public boolean matches(Player player, String extra) {
        return matches(player);
    }
}
