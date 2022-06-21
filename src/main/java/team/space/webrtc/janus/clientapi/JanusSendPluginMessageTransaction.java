package team.space.webrtc.janus.clientapi;

import org.json.JSONException;
import org.json.JSONObject;
import team.space.webrtc.janus.Entities.JanusMessageType;

public class JanusSendPluginMessageTransaction implements ITransactionCallbacks {
    private final IPluginHandleSendMessageCallbacks callbacks;

    public JanusSendPluginMessageTransaction(JanusSupportedPluginPackages plugin, IPluginHandleSendMessageCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    public TransactionType getTransactionType() {
        return TransactionType.plugin_handle_message;
    }

    public void reportSuccess(JSONObject obj) {
        try {
            JanusMessageType type = JanusMessageType.fromString(obj.getString("janus"));
            switch (type) {
                case success: {
                    JSONObject plugindata = obj.getJSONObject("plugindata");
                    JanusSupportedPluginPackages plugin = JanusSupportedPluginPackages.fromString(plugindata.getString("plugin"));
                    JSONObject data = plugindata.getJSONObject("data");
                    if (plugin == JanusSupportedPluginPackages.JANUS_NONE) {
                        callbacks.onCallbackError("unexpected message: \n\t" + obj.toString());
                    } else {
                        callbacks.onSuccessSynchronous(data);
                    }
                    break;
                }
                case ack: {
                    callbacks.onSuccesAsynchronous();
                    break;
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
