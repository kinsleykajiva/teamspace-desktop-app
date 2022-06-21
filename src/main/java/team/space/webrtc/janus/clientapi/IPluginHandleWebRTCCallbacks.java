package team.space.webrtc.janus.clientapi;

import org.json.JSONObject;

import java.math.BigInteger;

public interface IPluginHandleWebRTCCallbacks extends IJanusCallbacks {
    JSONObject getJsep();
    void onSuccess(JSONObject obj);
    JanusMediaConstraints getMedia();
    Boolean getTrickle();
    void trickleCandidateComplete(BigInteger videoRoomHandlerId);
}
