package org.circuitsoft.slackmc.plugin.bukkit;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.circuitsoft.slackmc.api.message.SlackMessageTemplate;
import org.circuitsoft.slackmc.api.outgoing.SlackSender;
import org.circuitsoft.slackmc.plugin.bukkit.broadcaster.ChatRuleHandler;
import org.circuitsoft.slackmc.plugin.bukkit.broadcaster.SlackRuleHandler;
import org.circuitsoft.slackmc.plugin.bukkit.filters.SlackFilter;
import org.circuitsoft.slackmc.plugin.bukkit.filters.WorldFilter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class Slack extends JavaPlugin {

    Hashtable<String, Class<? extends SlackRuleHandler>> handlers = new Hashtable<>();
    Hashtable<String, Class<? extends SlackFilter>> filters = new Hashtable<>();
    SlackSender sender = new SlackSender();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        initializeHandlers();
        initializeFilters();
        System.out.println("started");
        try {
            initializeRules();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            getLogger().log(Level.WARNING, "Error initializing rules.", e);
        }
    }

    @Override
    public void onDisable() {
        sender.shutdown();
    }

    public void addRuleHandler(String event, Class<? extends SlackRuleHandler> handler) {
        handlers.put(event, handler);
    }

    public void addFilter(String key, Class<? extends SlackFilter> filter) {
        filters.put(key, filter);
    }

    private void initializeHandlers() {
        addRuleHandler("chat", ChatRuleHandler.class);
    }

    private void initializeFilters() {
        addFilter("world", WorldFilter.class);
        //addFilter("player", PlayerFilter.class);
    }

    private void initializeRules() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        ConfigurationSection ruleConfig;
        Set<String> filterKeys;
        List<SlackFilter> ruleFilters;
        Constructor<? extends SlackFilter> filterConst;
        SlackFilter filter;
        Constructor<? extends SlackRuleHandler> handlerConst;
        Set<String> keys = getConfig().getConfigurationSection("outgoing").getKeys(false);
        for (String key : keys) {
            ruleConfig = getConfig().getConfigurationSection("outgoing." + key);
            filterKeys = ruleConfig.getConfigurationSection("filters").getKeys(false);
            ruleFilters = new ArrayList<>(filterKeys.size());
            for (String filterKey : filterKeys) {
                filterConst = filters.get(filterKey).getConstructor(Boolean.TYPE);
                filter = filterConst.newInstance(ruleConfig.getBoolean("filters." + filterKey + ".blacklist"));
                filter.setObject(filter.getObject(this, ruleConfig.getString("filters." + filterKey + ".filter")));
                ruleFilters.add(filter);
            }
            System.out.println(ruleConfig.getString("event"));
            handlerConst = handlers.get(ruleConfig.getString("event")).getConstructor(String.class, SlackMessageTemplate.class, SlackSender.class, SlackFilter[].class);
            getServer().getPluginManager().registerEvents(
                    handlerConst.newInstance(
                            ruleConfig.getString("webhook"),
                            new SlackMessageTemplate(ruleConfig.getString("channel"), ruleConfig.getBoolean("unfurlLinks"), ruleConfig.getString("format")),
                            sender,
                            ruleFilters.toArray(new SlackFilter[ruleFilters.size()])),
                    this);
        }
    }
}
