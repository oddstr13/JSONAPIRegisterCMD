package no.openshell.oddstr13.jsonapiregistercmd;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Arrays;

import org.bukkit.entity.Player;

import org.json.simpleForBukkit.JSONObject;
import com.alecgorge.java.http.HttpRequest;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {
    private JSONAPIRegisterCMD plugin;

    public CommandListener(JSONAPIRegisterCMD plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        try {
            String rawcmd = event.getMessage();
            String[] splarr = rawcmd.split("\\s");
            List<String> spl = Arrays.asList(splarr);
            String cmd = spl.get(0).substring(1);
            String[] args = new String[0];

            if (spl.size() > 1) {
                args = spl.subList(1, spl.size()).toArray(new String[0]);
            }

            List<String> urls = this.plugin.getCommands().get(cmd);
            if (urls != null) {
                this.plugin.log(player.getName() + " issued server command: " + rawcmd);
                JSONObject jsonobj = new CommandMessage(this.plugin.getStreamName(), player.getName(), cmd, args).toJSONObject();
                for (String url : urls) {
                    HttpRequest httpr = new HttpRequest(new URL(url));
                    httpr.setMethod("POST");
                    httpr.addPostValue("name", this.plugin.getStreamName());
                    httpr.addPostValue("json", jsonobj.toJSONString());
                    this.plugin.log("Posting command " + cmd + " to url: " + url);
                    httpr.request();
                }
                player.sendMessage("O.K.");
                event.setCancelled(true);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

