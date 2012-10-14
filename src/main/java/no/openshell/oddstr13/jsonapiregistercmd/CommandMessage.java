package no.openshell.oddstr13.jsonapiregistercmd;

import org.json.simpleForBukkit.JSONObject;
import com.alecgorge.minecraft.jsonapi.api.JSONAPIStreamMessage;

public class CommandMessage extends JSONAPIStreamMessage {
    private String streamName;
    private String player;
    private String cmd;
    private String[] args;

    public CommandMessage(String streamName, String playername, String cmdname, String[] cmdargs) {
//    public CommandMessage(String streamName, String playername, String cmdname) {
        this.streamName = streamName;
        this.player = playername;
        this.cmd = cmdname;
        this.args = cmdargs;
        setTime();
    }

    @Override
    public String streamName() {
        return streamName;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject o = new JSONObject();
        o.put("time", getTime());
        o.put("player", player);
        o.put("cmd", cmd);
        o.put("args", args);
        return o;
    }
}

