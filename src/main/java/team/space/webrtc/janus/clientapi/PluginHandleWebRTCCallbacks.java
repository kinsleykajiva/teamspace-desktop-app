package team.space.webrtc.janus.clientapi;

import org.json.JSONObject;

import java.math.BigInteger;

public  class PluginHandleWebRTCCallbacks implements IPluginHandleWebRTCCallbacks {

    private final JanusMediaConstraints constraints;
    private final JSONObject jsep;
    private final boolean trickle;
    public PluginHandleWebRTCCallbacks(JanusMediaConstraints constraints, JSONObject jsep, boolean trickle) {
        this.constraints = constraints;
        this.jsep = jsep;
        this.trickle = trickle;
    }

    @Override
    public void onSuccess(JSONObject obj) {

    }

    @Override
    public JSONObject getJsep() {
        return jsep;
    }

    @Override
    public JanusMediaConstraints getMedia() {
        return constraints;
    }

    @Override
    public Boolean getTrickle() {
        return trickle;
    }

    @Override
    public void trickleCandidateComplete(BigInteger videoRoomHandlerId) {

    }

    @Override
    public void onCallbackError(String error) {

    }
}