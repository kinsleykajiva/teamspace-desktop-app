package team.space.webrtc.janus.clientapi;

import org.json.JSONObject;

public interface IJanusMessageObserver {
    void receivedNewMessage(JSONObject obj);

    void onClose();

    void onOpen();

    void onError(Exception ex);
}
