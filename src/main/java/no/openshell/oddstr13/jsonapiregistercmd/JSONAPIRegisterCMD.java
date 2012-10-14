package no.openshell.oddstr13.jsonapiregistercmd;

import java.io.IOException;
import java.net.URL;
import java.lang.String;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.json.simpleForBukkit.JSONObject;
import com.alecgorge.java.http.HttpRequest;
import com.alecgorge.minecraft.jsonapi.api.JSONAPIStreamMessage;


public class JSONAPIRegisterCMD extends JavaPlugin {
    private final static Logger logger = Logger.getLogger("Minecraft");
    protected String streamName = "registrationverify";
    protected String streamPushUrlString = "http://172.16.1.190:8000/jsonapi-landing-register/";
    protected URL streamPushUrl;

//    public JSONAPIRegisterCMD() {
//    }

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
        if (checkplugin == null) {
            logWarning(name + " cannot be loaded because JSONAPI is not enabled on the server!");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            log( name + " version " + version + " enabled." );
            try {
                // Get handle to JSONAPI, add&register your custom listener
            } catch (ClassCastException ex) {
                ex.printStackTrace();
                logWarning(name + " can't cast plugin handle as JSONAPI plugin!");
                getServer().getPluginManager().disablePlugin(this);
            }
        }
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
                try {
                    streamPushUrl = new URL(streamPushUrlString);
                    JSONObject jsonobj = new VerifyMessage(getStreamName(), player.getName(), cmd.getName(), args).toJSONObject();
                    HttpRequest httpr = new HttpRequest(streamPushUrl);
                    httpr.setMethod("POST");
                    httpr.addPostValue("name", streamName);
                    httpr.addPostValue("json", jsonobj.toJSONString());
                    httpr.request();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }
}
