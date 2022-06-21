package team.space.webrtc.janus.clientapi;

import org.json.JSONObject;


interface IJanusAttachPluginCallbacks extends IJanusCallbacks {
    void attachPluginSuccess(JSONObject obj, JanusSupportedPluginPackages plugin, IJanusPluginCallbacks callbacks);
}
