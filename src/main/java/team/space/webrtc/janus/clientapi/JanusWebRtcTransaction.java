package team.space.webrtc.janus.clientapi;

import org.json.JSONException;
import org.json.JSONObject;
import team.space.webrtc.janus.Entities.JanusMessageType;

public class JanusWebRtcTransaction implements ITransactionCallbacks {
    private final IPluginHandleWebRTCCallbacks callbacks;
    private final JanusSupportedPluginPackages plugin;

    public JanusWebRtcTransaction(JanusSupportedPluginPackages plugin, IPluginHandleWebRTCCallbacks callbacks) {
        this.callbacks = callbacks;
        this.plugin = plugin;
    }

    public TransactionType getTransactionType() {
        return TransactionType.plugin_handle_webrtc_message;
    }

    public void reportSuccess(JSONObject obj) {
        try {
            JanusMessageType type = JanusMessageType.fromString(obj.getString("janus"));
            switch (type) {
                case success: {
                    break;
                }
                case ack: {

                }
                default: {
                    callbacks.onCallbackError(obj.getJSONObject("error").getString("reason"));
                    break;
                }
            }
        } catch (JSONException ex) {
            callbacks.onCallbackError(ex.getMessage());
        }
    }
}