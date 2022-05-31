package team.space.webrtc.janus;

import dev.onvoid.webrtc.RTCSessionDescription;

public interface CreateAnswerCallback {
        void onSetAnswerSuccess(RTCSessionDescription sdp);

        void onSetAnswerFailed(String error);
        }