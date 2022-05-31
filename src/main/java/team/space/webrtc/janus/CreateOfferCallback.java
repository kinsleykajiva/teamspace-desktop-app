package team.space.webrtc.janus;

import dev.onvoid.webrtc.RTCSessionDescription;

public interface CreateOfferCallback {
    void onCreateOfferSuccess(RTCSessionDescription sdp);

    void onCreateFailed(String error);
}