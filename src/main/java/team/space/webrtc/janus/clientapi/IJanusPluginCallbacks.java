package team.space.webrtc.janus.clientapi;

import dev.onvoid.webrtc.PeerConnectionObserver;
import dev.onvoid.webrtc.RTCRtpTransceiver;
import dev.onvoid.webrtc.media.MediaStream;
import org.json.JSONObject;

public interface IJanusPluginCallbacks extends IJanusCallbacks {
    void success(JanusPluginHandle handle);

    void onMessage(JSONObject msg, JSONObject jsep);

    // void onLocalStream(MediaStream stream);
    void onLocalStream(PeerConnectionObserver connectionObserver);

//    void onRemoteStream(MediaStream stream);
    void  onTrack(RTCRtpTransceiver transceiver);

    void onDataOpen(Object data);

    void onData(Object data);

    void onCleanup();

    void onDetached();

    JanusSupportedPluginPackages getPlugin();
}
