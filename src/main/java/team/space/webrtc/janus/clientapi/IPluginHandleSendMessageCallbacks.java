package team.space.webrtc.janus.clientapi;

import org.json.JSONObject;

public interface IPluginHandleSendMessageCallbacks  extends IJanusCallbacks {

    void onSuccessSynchronous(JSONObject obj);

    void onSuccesAsynchronous();

    JSONObject getMessage();
}
