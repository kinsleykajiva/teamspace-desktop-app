package team.space.webrtc.janus.clientapi;

import dev.onvoid.webrtc.*;
import dev.onvoid.webrtc.media.MediaDevices;
import dev.onvoid.webrtc.media.MediaStream;
import dev.onvoid.webrtc.media.audio.AudioOptions;
import dev.onvoid.webrtc.media.audio.AudioSource;
import dev.onvoid.webrtc.media.audio.AudioTrack;
import dev.onvoid.webrtc.media.audio.AudioTrackSource;
import dev.onvoid.webrtc.media.video.VideoDevice;
import dev.onvoid.webrtc.media.video.VideoDeviceSource;
import dev.onvoid.webrtc.media.video.VideoTrack;
import javafx.concurrent.Task;

import org.json.JSONException;
import org.json.JSONObject;
import team.space.webrtc.janus.Entities.JanusMessageType;
import team.space.webrtc.janus.utils.JanusClient;

import java.math.BigInteger;
import java.util.List;

import static java.util.Objects.nonNull;

public class JanusPluginHandle {

    private boolean started = false;
    private MediaStream myStream = null;
    private MediaStream remoteStream = null;
    private RTCSessionDescription mySdp = null;
    private RTCPeerConnection pc = null;
    private RTCDataChannel dataChannel = null;
    private boolean trickle = true;
    private boolean iceDone = false;
    private boolean sdpSent = false;

    private final String VIDEO_TRACK_ID = "ofstream_video";
    //    private final String AUDIO_TRACK_ID = "ofstream_audio";
    private final String LOCAL_MEDIA_ID = "1198181";

    class WebRtcObserver implements SetSessionDescriptionObserver, PeerConnectionObserver, CreateSessionDescriptionObserver {
        private final IPluginHandleWebRTCCallbacks webRtcCallbacks;


        //  BigInteger videoRoomHandlerId;
        public WebRtcObserver(IPluginHandleWebRTCCallbacks callbacks) {
            this.webRtcCallbacks = callbacks;
            // this.videoRoomHandlerId = videoRoomHandlerId;
        }

        @Override
        public void onIceCandidate(RTCIceCandidate rtcIceCandidate) {
            System.out.println("JanusPluginHandle.class: onIceCandidate " + rtcIceCandidate);
            if (trickle) {
                sendTrickleCandidate(rtcIceCandidate);
            }
        }

        @Override
        public void onSuccess() {
            System.out.println("JanusPluginHandle.class: onSuccess ");

            if (mySdp == null) {
                createSdpInternal(webRtcCallbacks, false);
            }

        }

        @Override
        public void onSuccess(RTCSessionDescription rtcSessionDescription) {
            System.out.println("JanusPluginHandle.class: onSuccess " + rtcSessionDescription);

            //  peerConnection.setLocalDescription(this, rtcSessionDescription);
            onLocalSdp(rtcSessionDescription, webRtcCallbacks);
        }

        @Override
        public void onFailure(String s) {
            System.err.println("JanusPluginHandle.class: onFailure " + s);
            webRtcCallbacks.onCallbackError(s);
        }


        @Override
        public void onIceConnectionChange(RTCIceConnectionState state) {
            System.out.println("JanusPluginHandle.class: onIceConnectionChange " + state);
            switch (state) {
                case DISCONNECTED:
                    break;
                case CONNECTED:
                    System.out.println("JanusPluginHandle.class: onIceConnectionChange " + state);
                    break;
                case NEW:
                    break;
                case CHECKING:
                    break;
                case CLOSED:
                    break;
                case FAILED:
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onIceGatheringChange(RTCIceGatheringState state) {
            //  webRtcCallbacks.trickleCandidateComplete(videoRoomHandlerId);
            switch (state) {
                case NEW:
                    break;
                case GATHERING:
                    break;
                case COMPLETE:
                    if (!trickle) {
                        mySdp = pc.getLocalDescription();
                        sendSdp(webRtcCallbacks);
                    } else {
                        sendTrickleCandidate(null);
                    }
                    break;
                default:
                    break;
            }
        }


        @Override
        public void onSignalingChange(RTCSignalingState state) {
            System.out.println("JanusPluginHandle.class: onSignalingChange " + state);
            switch (state) {
                case STABLE:
                    break;
                case HAVE_LOCAL_OFFER:
                    break;
                case HAVE_REMOTE_OFFER:
                    break;
                case CLOSED:
                    break;
            }
        }

        @Override
        public void onAddStream(MediaStream stream) {
            PeerConnectionObserver.super.onAddStream(stream);
        }

        @Override
        public void onRemoveStream(MediaStream stream) {
            PeerConnectionObserver.super.onRemoveStream(stream);
        }

        @Override
        public void onDataChannel(RTCDataChannel dataChannel) {
            PeerConnectionObserver.super.onDataChannel(dataChannel);
        }

        @Override
        public void onAddTrack(RTCRtpReceiver receiver, MediaStream[] mediaStreams) {
            PeerConnectionObserver.super.onAddTrack(receiver, mediaStreams);
        }

        @Override
        public void onRemoveTrack(RTCRtpReceiver receiver) {
            PeerConnectionObserver.super.onRemoveTrack(receiver);
        }
    } // end of class

    private PeerConnectionFactory sessionFactory = null;
    private JanusServer server;
    public final JanusSupportedPluginPackages plugin;
    public final BigInteger id;
    private final IJanusPluginCallbacks callbacks;

    private class AsyncPrepareWebRtc extends Task<Void> {

        private IPluginHandleWebRTCCallbacks IPluginHandleWebRTCCallbacks;

        AsyncPrepareWebRtc(IPluginHandleWebRTCCallbacks params) {
            this.IPluginHandleWebRTCCallbacks = params;
        }

        @Override
        protected Void call() throws Exception {
            IPluginHandleWebRTCCallbacks cb = IPluginHandleWebRTCCallbacks;
            prepareWebRtc(cb);
            return null;
        }
    }

    private class AsyncHandleRemoteJsep extends Task<Void> {
        private IPluginHandleWebRTCCallbacks IPluginHandleWebRTCCallbacks;

        AsyncHandleRemoteJsep(IPluginHandleWebRTCCallbacks params) {
            this.IPluginHandleWebRTCCallbacks = params;
        }


        @Override
        protected Void call() throws Exception {
            IPluginHandleWebRTCCallbacks webrtcCallbacks = IPluginHandleWebRTCCallbacks;
            if (sessionFactory == null) {
                webrtcCallbacks.onCallbackError("WebRtc PeerFactory is not initialized. Please call initializeMediaContext");
                return null;
            }
            JSONObject jsep = webrtcCallbacks.getJsep();
            if (jsep != null) {
                if (pc == null) {

                    System.err.println("JanusPluginHandle.class: could not set remote offer");

                    callbacks.onCallbackError("No peerconnection created, if this is an answer please use createAnswer");
                    return null;
                }
                try {

                    String sdpString = jsep.getString("sdp");
                    String type = jsep.getString("type");
                    var t = RTCSdpType.ANSWER.equals(type.toUpperCase()) ? RTCSdpType.ANSWER : RTCSdpType.OFFER;
                    System.out.println("JanusPluginHandle.class: setRemoteDescription " + sdpString);
                    String sdp = jsep.getString("sdp");
                    var sdpp = new WebRtcObserver(webrtcCallbacks);
                    pc.setRemoteDescription(new RTCSessionDescription(t, sdp), sdpp);

                } catch (JSONException ex) {
                    System.out.println("JanusPluginHandle.class: could not set remote offer " + ex.getMessage());
                    webrtcCallbacks.onCallbackError(ex.getMessage());
                }
            }

            return null;
        }
    }

    public JanusPluginHandle(JanusServer server, JanusSupportedPluginPackages plugin, BigInteger handle_id, IJanusPluginCallbacks callbacks) {
        this.server = server;
        this.plugin = plugin;
        id = handle_id;
        this.callbacks = callbacks;
        sessionFactory = new PeerConnectionFactory();
    }

    public void onMessage(String msg) {
        try {
            JSONObject obj = new JSONObject(msg);
            callbacks.onMessage(obj, null);
        } catch (JSONException ex) {
            //TODO do we want to notify the GatewayHandler?
        }
    }

    public void onMessage(JSONObject msg, JSONObject jsep) {
        callbacks.onMessage(msg, jsep);
    }

    private void onLocalStream(PeerConnectionObserver stream) {
        callbacks.onLocalStream(stream);
    }

    private void onRemoteStream(MediaStream stream) {
        // callbacks.onRemoteStream(stream);
    }


    public void onDataOpen(Object data) {
        callbacks.onDataOpen(data);
    }

    public void onData(Object data) {
        callbacks.onData(data);
    }

    public void onCleanup() {
        callbacks.onCleanup();
    }

    public void onDetached() {
        callbacks.onDetached();
    }

    public void sendMessage(IPluginHandleSendMessageCallbacks obj) {
        server.sendMessage(TransactionType.plugin_handle_message, id, obj, plugin);
    }

    private void sendTrickleCandidate(RTCIceCandidate candidate) {
        try {
            JSONObject message = new JSONObject();
            JSONObject cand = new JSONObject();
            if (candidate == null)
                cand.put("completed", true);
            else {
                cand.put("candidate", candidate.sdp);
                cand.put("sdpMid", candidate.sdpMid);
                cand.put("sdpMLineIndex", candidate.sdpMLineIndex);
            }
            message.put("candidate", cand);

            server.sendMessage(message, JanusMessageType.trickle, id);
        } catch (JSONException ex) {

        }
    }

    private RTCConfiguration rtcConfiguration = new RTCConfiguration();

    private void streamsDone(IPluginHandleWebRTCCallbacks webRTCCallbacks) {
        RTCIceServer stunServer = new RTCIceServer();
        stunServer.urls.add("stun:stun.l.google.com:19302");
        rtcConfiguration.iceServers.add(stunServer);
        PeerConnectionFactory peerConnectionFactory = new PeerConnectionFactory();

        AudioTrack audioTrack;
        VideoTrack videoTrack;


        if (webRTCCallbacks.getMedia().getRecvAudio()) {
            AudioOptions audioOptions = new AudioOptions();
            audioOptions.noiseSuppression = true;
            AudioTrackSource audioSource = peerConnectionFactory.createAudioSource(audioOptions);
            audioTrack = peerConnectionFactory.createAudioTrack("AUDIO", audioSource);

            pc.addTrack(audioTrack, List.of("stream"));
        }
        if (webRTCCallbacks.getMedia().getRecvVideo()) {
            VideoDeviceSource vid = new VideoDeviceSource();
            VideoDevice device = MediaDevices.getVideoCaptureDevices().get(0);
            vid.setVideoCaptureDevice(device);
            vid.setVideoCaptureCapability(MediaDevices.getVideoCaptureCapabilities(device).get(0));

            videoTrack = peerConnectionFactory.createVideoTrack("CAM", vid);
            pc.addTrack(videoTrack, List.of("stream"));
        }
        pc = sessionFactory.createPeerConnection(rtcConfiguration, new WebRtcObserver(webRTCCallbacks));

        if (myStream != null) {
            // pc.addStream(myStream);
        }
        if (webRTCCallbacks.getJsep() == null) {
            createSdpInternal(webRTCCallbacks, true);
        } else {
            try {
                JSONObject obj = webRTCCallbacks.getJsep();
                String sdp = obj.getString("sdp");
                String type = obj.getString("type");


                var t = RTCSdpType.ANSWER.equals(type.toUpperCase()) ? RTCSdpType.ANSWER : RTCSdpType.OFFER;

                var sdpp = new WebRtcObserver(webRTCCallbacks);
                pc.setRemoteDescription(new RTCSessionDescription(t, sdp), sdpp);

            } catch (Exception ex) {
                webRTCCallbacks.onCallbackError(ex.getMessage());
            }
        }
    }

    public void createOffer(IPluginHandleWebRTCCallbacks webrtcCallbacks) {
        var t = new AsyncPrepareWebRtc(webrtcCallbacks);
        new Thread(t).start();
    }

    public void createAnswer(IPluginHandleWebRTCCallbacks webrtcCallbacks) {
        var t = new AsyncPrepareWebRtc(webrtcCallbacks);
        new Thread(t).start();
    }

    private void prepareWebRtc(IPluginHandleWebRTCCallbacks callbacks) {
        if (pc != null) {
            if (callbacks.getJsep() == null) {
                createSdpInternal(callbacks, true);
            } else {
                try {
                    JSONObject jsep = callbacks.getJsep();
                    String sdp = jsep.getString("sdp");
                    String type = jsep.getString("type");
                    var t = RTCSdpType.ANSWER.equals(type.toUpperCase()) ? RTCSdpType.ANSWER : RTCSdpType.OFFER;

                    // SessionDescription.Type type = SessionDescription.Type.fromCanonicalForm(jsep.getString("type"));
                    // SessionDescription sdp = new SessionDescription(type, sdpString);
                    // pc.setRemoteDescription(new WebRtcObserver(callbacks), sdp);

                    var sdpp = new WebRtcObserver(callbacks);
                    pc.setRemoteDescription(new RTCSessionDescription(t, sdp), sdpp);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    callbacks.onCallbackError(ex.getMessage());
                }
            }
        } else {
            trickle = callbacks.getTrickle() != null ? callbacks.getTrickle() : false;
            AudioTrack audioTrack = null;
            VideoTrack videoTrack = null;
            MediaStream stream = null;
            if (callbacks.getMedia().getSendAudio()) {
                // AudioSource source = sessionFactory.createAudioSource(new MediaConstraints());
                //audioTrack = sessionFactory.createAudioTrack(AUDIO_TRACK_ID, source);

                AudioOptions audioOptions = new AudioOptions();
                audioOptions.noiseSuppression = true;
                AudioTrackSource audioSource = sessionFactory.createAudioSource(audioOptions);
                audioTrack = sessionFactory.createAudioTrack("AUDIO", audioSource);
            }
            if (callbacks.getMedia().getSendVideo()) {
                var videoSource = callbacks.getMedia().getVideoSource();
                VideoDevice device = MediaDevices.getVideoCaptureDevices().get(0);
                videoSource.setVideoCaptureDevice(device);
                videoSource.setVideoCaptureCapability(MediaDevices.getVideoCaptureCapabilities(device).get(0));

                /*if (nonNull(cameraCapability)) {
                    var nearestCapability = getNearestCameraFormat(cameraCapability);

                    LOGGER.debug("Video capture capability: " + cameraCapability);
                    LOGGER.debug("Video capture nearest capability: " + nearestCapability);

                    cameraSource.setVideoCaptureCapability(nearestCapability);
                }*/

                JanusMediaConstraints.JanusVideo videoConstraints = callbacks.getMedia().getVideo();

                // VideoSource source = sessionFactory.createVideoSource(capturer, constraints);
                //videoTrack = sessionFactory.createVideoTrack(VIDEO_TRACK_ID, source);
                videoTrack = sessionFactory.createVideoTrack("CAM", videoSource);
            }
            if (audioTrack != null || videoTrack != null) {
                pc.addTrack(audioTrack, List.of("stream"));
                pc.addTrack(videoTrack, List.of("stream"));
                /// stream = sessionFactory.createLocalMediaStream(LOCAL_MEDIA_ID);
                /*if (audioTrack != null)
                    stream.addTrack(audioTrack);
                if (videoTrack != null)
                    stream.addTrack(videoTrack);*/
            }
            /*myStream = stream;
            if (stream != null) {
                onLocalStream(stream);
            }*/
            //  callbacks.
            // onLocalStream();
            streamsDone(callbacks);
        }
    }

    private void createSdpInternal(IPluginHandleWebRTCCallbacks callbacks, Boolean isOffer) {
        //  MediaConstraints pc_cons = new MediaConstraints();
        //pc_cons.optional.add(new MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"));
        if (callbacks.getMedia().getRecvAudio()) {
            // pc_cons.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        }
        if (callbacks.getMedia().getRecvVideo()) {
            //  Log.d("VIDEO_ROOM", "Receiving video");
            System.out.println("Receiving video");
        }
        if (isOffer) {
            RTCOfferOptions options = new RTCOfferOptions();
            pc.createOffer(options, new WebRtcObserver(callbacks));
        } else {
            RTCAnswerOptions answerOptions = new RTCAnswerOptions();
            pc.createAnswer(answerOptions, new WebRtcObserver(callbacks));
        }
    }

    public void handleRemoteJsep(IPluginHandleWebRTCCallbacks webrtcCallbacks) {
        var t = new AsyncHandleRemoteJsep(webrtcCallbacks);
        new Thread(t).start();
    }

    public void hangUp() {
        if (remoteStream != null) {
            remoteStream.dispose();
            remoteStream = null;
        }
        if (myStream != null) {
            myStream.dispose();
            myStream = null;
        }
        if (pc != null && pc.getSignalingState() != RTCSignalingState.CLOSED) {
            pc.close();
        }
        pc = null;
        started = false;
        mySdp = null;
        if (dataChannel != null) {
            dataChannel.close();
        }
        dataChannel = null;
        trickle = true;
        iceDone = false;
        sdpSent = false;
    }

    public void detach() {
        hangUp();
        JSONObject obj = new JSONObject();
        server.sendMessage(obj, JanusMessageType.detach, id);
    }

    private void onLocalSdp(RTCSessionDescription sdp, IPluginHandleWebRTCCallbacks callbacks) {
        if (pc != null) {
            if (mySdp == null) {
                mySdp = sdp;
                pc.setLocalDescription(sdp, new WebRtcObserver(callbacks));
            }
            if (!iceDone && !trickle)
                return;
            if (sdpSent)
                return;
            try {
                sdpSent = true;
                JSONObject obj = new JSONObject();
                obj.put("sdp", mySdp.sdp);
                obj.put("type", mySdp.sdpType);
                callbacks.onSuccess(obj);
            } catch (JSONException ex) {
                callbacks.onCallbackError(ex.getMessage());
            }
        }
    }


    private void sendSdp(IPluginHandleWebRTCCallbacks callbacks) {
        if (mySdp != null) {
            mySdp = pc.getLocalDescription();
            if (!sdpSent) {
                sdpSent = true;
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("sdp", mySdp.sdp);
                    obj.put("type", mySdp.sdpType);
                    callbacks.onSuccess(obj);
                } catch (JSONException ex) {
                    callbacks.onCallbackError(ex.getMessage());
                }
            }
        }
    }


}
