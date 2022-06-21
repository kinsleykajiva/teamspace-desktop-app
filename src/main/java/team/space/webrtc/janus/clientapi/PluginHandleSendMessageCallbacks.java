package team.space.webrtc.janus.clientapi;

import org.json.JSONObject;

public class PluginHandleSendMessageCallbacks implements IPluginHandleSendMessageCallbacks {

    private final JSONObject message;

    public PluginHandleSendMessageCallbacks(JSONObject message) {
        this.message = message;
    }

    @Override
    public void onSuccessSynchronous(JSONObject obj) {
    }

    @Override
    public void onSuccesAsynchronous() {
    }

    @Override
    public JSONObject getMessage() {
        return message;
    }

    @Override
    public void onCallbackError(String error) {
    }
}
