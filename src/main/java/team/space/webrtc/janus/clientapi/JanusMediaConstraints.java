package team.space.webrtc.janus.clientapi;

import dev.onvoid.webrtc.PeerConnectionFactory;
import dev.onvoid.webrtc.RTCPeerConnection;
import dev.onvoid.webrtc.media.MediaDevices;
import dev.onvoid.webrtc.media.audio.AudioOptions;
import dev.onvoid.webrtc.media.audio.AudioTrack;
import dev.onvoid.webrtc.media.audio.AudioTrackSource;
import dev.onvoid.webrtc.media.video.VideoDevice;
import dev.onvoid.webrtc.media.video.VideoDeviceSource;
import dev.onvoid.webrtc.media.video.VideoTrack;

import java.util.List;

public class JanusMediaConstraints {

    public class JanusVideo {
        private int maxHeight, minHeight, maxWidth, minWidth, maxFramerate, minFramerate;

        public JanusVideo() {
            maxFramerate = 15;
            minFramerate = 0;
            maxHeight = 240;
            minHeight = 0;
            maxWidth = 320;
            minWidth = 0;
        }

        public int getMaxHeight() {
            return maxHeight;
        }

        public void setMaxHeight(int maxHeight) {
            this.maxHeight = maxHeight;
        }

        public int getMinHeight() {
            return minHeight;
        }

        public void setMinHeight(int minHeight) {
            this.minHeight = minHeight;
        }

        public int getMaxWidth() {
            return maxWidth;
        }

        public void setMaxWidth(int maxWidth) {
            this.maxWidth = maxWidth;
        }

        public int getMinWidth() {
            return minWidth;
        }

        public void setMinWidth(int minWidth) {
            this.minWidth = minWidth;
        }

        public int getMaxFramerate() {
            return maxFramerate;
        }

        public void setMaxFramerate(int maxFramerate) {
            this.maxFramerate = maxFramerate;
        }

        public int getMinFramerate() {
            return minFramerate;
        }

        public void setMinFramerate(int minFramerate) {
            this.minFramerate = minFramerate;
        }
    }

    private RTCPeerConnection peerConnection;
    private AudioOptions audioOptions = new AudioOptions();
    private VideoDeviceSource videoSource = new VideoDeviceSource();
    private boolean sendAudio = true;
    private boolean recvVideo = true;
    private JanusVideo video = new JanusVideo();
    private boolean recvAudio = true;
    private AudioTrackSource audioTrackSource;
    private AudioTrack audioTrack;
    private VideoTrack videoTrack;

    public JanusMediaConstraints(PeerConnectionFactory peerConnectionFactory, RTCPeerConnection peerConnection) {
        audioTrackSource = peerConnectionFactory.createAudioSource(audioOptions);
        audioTrack = peerConnectionFactory.createAudioTrack("audioTrack", audioTrackSource);
        peerConnection.addTrack(audioTrack, List.of("stream"));
        this.peerConnection = peerConnection;


    }

    public JanusMediaConstraints() {
    }

    public void setVideo() {
        VideoDevice device = MediaDevices.getVideoCaptureDevices().get(0);
        videoSource.setVideoCaptureDevice(device);
        videoSource.setVideoCaptureCapability(MediaDevices.getVideoCaptureCapabilities(device).get(0));
        peerConnection.addTrack(videoTrack, List.of("stream"));
    }

    public JanusVideo getVideo() {
        return video;
    }

    public RTCPeerConnection getPeerConnection() {
        return peerConnection;
    }

    public void setVideo(JanusVideo video) {
        this.video = video;
    }

    public Boolean getSendVideo() {
        return video != null;
    }

    public boolean getSendAudio() {
        return sendAudio;
    }

    public void setSendAudio(boolean sendAudio) {
        this.sendAudio = sendAudio;
    }

    public boolean getRecvVideo() {
        return recvVideo;
    }

    public void setRecvVideo(boolean recvVideo) {
        this.recvVideo = recvVideo;
    }

    public boolean getRecvAudio() {
        return recvAudio;
    }

    public void setRecvAudio(boolean recvAudio) {
        this.recvAudio = recvAudio;
    }

    public VideoDeviceSource getVideoSource() {
        return videoSource;
    }
}
