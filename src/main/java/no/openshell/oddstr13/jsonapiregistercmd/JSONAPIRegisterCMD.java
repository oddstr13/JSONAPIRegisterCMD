package no.openshell.oddstr13.jsonapiregistercmd;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

public class JSONAPIRegisterCMD extends JavaPlugin {
    private Logger logger = Logger.getLogger("Minecraft");
    private String streamName = "registrationverify";
    private Configuration config;
    private Map<String, List<String>> commands;

//    public JSONAPIRegisterCMD() {
//    }

    public String getStreamName() {
        return this.streamName;
    }

    public Map<String, List<String>> getCommands() {
        return this.commands;
    }

    public void log(String text) {
        logger.log(Level.INFO, text);
    }

    public void logWarning(String text) {
        logger.log(Level.WARNING, text);
    }

    public void loadConfig() {
        this.saveDefaultConfig();
        this.reloadConfig();
        Map<String, List<String>> tmpcmds = new HashMap<String, List<String>>();
        ConfigurationSection cfgsect = this.getConfig().getConfigurationSection("commands");
        if (cfgsect == null) {
            this.logWarning(this.getDescription().getName() + " Unable to load config file - Syntax error?");
        } else {
            Set<String> set = cfgsect.getKeys(false);
            for (String key : set) {
                List<String> value = this.getConfig().getConfigurationSection("commands").getStringList(key);
                tmpcmds.put(key, value);
            }
        }
        this.commands = tmpcmds;
    }

    @Override
    public void onEnable() {
        String name = this.getDescription().getName();
        String version = this.getDescription().getVersion();
        Plugin checkplugin = this.getServer().getPluginManager().getPlugin("JSONAPI");
        if (checkplugin == null) {
            logWarning(name + " cannot be loaded because JSONAPI is not enabled on the server!");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            this.loadConfig();
            this.getServer().getPluginManager().registerEvents(new CommandListener(this), this);
            log(name + " version " + version + " enabled.");
        }
    }

    @Override
    public void onDisable() {
        log("Unloading " + this.getDescription().getName() + ".");
    }
}
