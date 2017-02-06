package org.circuitsoft.slackmc.plugin.bukkit.filters;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerFilter extends SlackFilter {


    public PlayerFilter(boolean blacklist) {
        super(blacklist);
    }

    @Override
    public Object getObject(JavaPlugin plugin, String data) {
        return null;
    }

    @Override
    public void setObject(Object data) {

    }

    @Override
    public boolean matches(Player player) {
        return false;
    }

    @Override
    public boolean matches(Player player, String extra) {
        return false;
    }
}
