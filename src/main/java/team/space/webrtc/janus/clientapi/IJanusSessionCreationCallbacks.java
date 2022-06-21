package team.space.webrtc.janus.clientapi;

import org.json.JSONObject;

public interface IJanusSessionCreationCallbacks extends IJanusCallbacks {
    void onSessionCreationSuccess(JSONObject obj);
}