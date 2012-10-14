package no.openshell.oddstr13.jsonapiregistercmd;

import java.lang.String;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.alecgorge.minecraft.jsonapi.JSONAPI;
import com.alecgorge.minecraft.jsonapi.api.JSONAPIStream;

public class JSONAPIRegisterCMD extends JavaPlugin {
    private final static Logger logger = Logger.getLogger("Minecraft");
    protected JSONAPI JSONAPIHandle;
    protected String streamName = "registrationverify";
    private JSONAPIStream stream = new VerifyStream();

//    public JSONAPIRegisterCMD() {
//    }

    public JSONAPI getJSONAPI() {
        return JSONAPIHandle;
    }

    public String getStreamName() {
        return streamName;
    }

    public void log(String text) {
        logger.log(Level.INFO, text);
    }

    public void logWarning(String text) {
        logger.log(Level.WARNING, text);
    }

    @Override
    public void onEnable(){
        String name = this.getDescription().getName();
        String version = this.getDescription().getVersion();
        Plugin checkplugin = this.getServer().getPluginManager().getPlugin("JSONAPI");
        Plugin checkplugin2 = this.getServer().getPluginManager().getPlugin("HeroicDeath");
        if (checkplugin == null) {
            logWarning(name + " cannot be loaded because JSONAPI is not enabled on the server!");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            log( name + " version " + version + " enabled." );
            try {
                // Get handle to JSONAPI, add&register your custom listener
                this.JSONAPIHandle = (JSONAPI) checkplugin;
            } catch (ClassCastException ex) {
                ex.printStackTrace();
                logWarning(name + " can't cast plugin handle as JSONAPI plugin!");
                getServer().getPluginManager().disablePlugin(this);
            }
        }
        JSONAPI JSONAPI = this.getJSONAPI();
        JSONAPI.getStreamManager().registerStream(this.getStreamName(), stream);
    }

    @Override
    public void onDisable() {
        log( "Unloading " + this.getDescription().getName() + "." );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("verify")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be run by a player.");
            } else {
                Player player = (Player) sender;
                // do something
                stream.addMessage(new VerifyMessage(getStreamName(), player.getName(), cmd.getName(), args));
            }
            return true;
        }
        return false;
    }
}
