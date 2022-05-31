package team.space.webrtc.webrtcutils;

import dev.onvoid.webrtc.*;
import dev.onvoid.webrtc.media.MediaDevices;
import dev.onvoid.webrtc.media.MediaStream;
import dev.onvoid.webrtc.media.MediaStreamTrack;
import dev.onvoid.webrtc.media.audio.AudioOptions;
import dev.onvoid.webrtc.media.audio.AudioTrack;
import dev.onvoid.webrtc.media.audio.AudioTrackSource;
import dev.onvoid.webrtc.media.video.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.map.MutableMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import team.space.utils.MediaUtils;
import team.space.utils.Shared;
import team.space.utils.XUtils;
import team.space.webrtc.janus.CreateAnswerCallback;
import team.space.webrtc.janus.CreateOfferCallback;
import team.space.webrtc.janus.CreatePeerConnectionCallback;
import team.space.webrtc.janus.Entities.Publisher;
import team.space.webrtc.janus.Entities.Room;
import team.space.webrtc.janus.Entities.VideoItem;
import team.space.webrtc.janus.utils.JanusClient;
import team.space.webrtc.janus.utils.VideoView;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static team.space.webrtc.webrtcutils.WebRTCUtils.JANUS_URL;

public class CallManager {
    private MediaUtils mediaUtils;
    private VideoCapture videoCapturer;
    private PeerConnectionFactory peerConnectionFactory;
    private RTCPeerConnection peerConnection;
    private VideoTrack videoTrack;
    private VideoDeviceSource videoSource;
    private JanusClient janusClient;
    private BigInteger videoRoomHandlerId;
    private MutableMap<String, VideoView> videoViewNodeMap = Maps.mutable.with();
    // private List<VideoItem> videoItemList = new ArrayList<>();
    private ObservableList<VideoItem> videoItemList = FXCollections.observableArrayList();
    private Room room = new Room(1234);
    private String userName = Shared.LOGGED_USER.getFullName();
    private RTCConfiguration rtcConfiguration = new RTCConfiguration();


    private VBox centerRoot;

    public CallManager(VBox centerRoot) {
        this.centerRoot = centerRoot;
    }


    public void initJanusWebrtcSession() {
        RTCIceServer stunServer = new RTCIceServer();
        stunServer.urls.add("stun:stun.l.google.com:19302");
        rtcConfiguration.iceServers.add(stunServer);
        peerConnectionFactory = createPeerConnectionFactory();
        janusClient = new JanusClient(JANUS_URL);
        janusClient.setJanusCallback(janusCallback);

        peerConnection = peerConnectionFactory.createPeerConnection(rtcConfiguration, new PeerConnectionObserver() {

            @Override
            public void onIceGatheringChange(RTCIceGatheringState state) {
                janusClient.trickleCandidateComplete(videoRoomHandlerId);
            }

            @Override
            public void onIceCandidate(RTCIceCandidate rtcIceCandidate) {

            }
        });

        AudioOptions audioOptions = new AudioOptions();
        AudioTrackSource audioSource = peerConnectionFactory.createAudioSource(audioOptions);
        AudioTrack audioTrack = peerConnectionFactory.createAudioTrack("audioTrack", audioSource);


        videoSource = new VideoDeviceSource();
        VideoDevice device = MediaDevices.getVideoCaptureDevices().get(0);
        videoSource.setVideoCaptureDevice(device);
        videoSource.setVideoCaptureCapability(MediaDevices.getVideoCaptureCapabilities(device).get(0)); //I believe index 0 is auto-resolution, 17 is 1280x720 @ 10fps
        videoSource.start();
        videoTrack = peerConnectionFactory.createVideoTrack("CAM", videoSource);

        peerConnection.addTrack(videoTrack, List.of("stream"));
        peerConnection.addTrack(audioTrack, List.of("stream"));

        janusClient.connect();

        videoItemList.addListener((ListChangeListener<VideoItem>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (VideoItem item : c.getAddedSubList()) {
                        System.out.println("XXXXXX-- Added:###: " + item.getUserId());
                        if (item.getDisplay().equals(userName)) {
                            Platform.runLater(() -> {


                                System.out.println("local video view");
                                VideoView localVideoView = new VideoView();
                                localVideoView.resize(400, 400);
                                localVideoView.setId(String.valueOf(item.getUserId()));
                                centerRoot.getChildren().add(localVideoView);
                                videoTrack.addSink(localVideoView::setVideoFrame);

                            });

                        } else {
                            Platform.runLater(() -> {
                                System.out.println("remote video view");
                                VideoView remoteVideoView = new VideoView();
                                remoteVideoView.resize(400, 400);
                                remoteVideoView.setId(String.valueOf(item.getUserId()));
                                centerRoot.getChildren().add(remoteVideoView);
                                videoViewNodeMap.put(String.valueOf(item.getUserId()), remoteVideoView);
                            });
                        }
                    }
                }
                if (c.wasRemoved()) {
                    for (VideoItem item : c.getRemoved()) {
                        System.out.println("removing local video view" + item.getUserId());
                        //  centerRoot.getChildren().remove(item);
                        Node target = centerRoot.getChildren().stream()
                                .filter(node -> node instanceof VideoView &&
                                        !node.getId().equals(null) && !node.getId().equals("null") &&
                                        node.getId().equals(String.valueOf(item.getUserId())))
                                .findFirst()
                                .orElse(null);
                        if (target != null) {
                            Platform.runLater(() -> {
                                System.out.println("removing local video view Node");
                                centerRoot.getChildren().remove(target);

                            });
                        }
                    }
                }
            }
        });

    }

    void notifyChnagesAll() {

    }

    private team.space.webrtc.janus.utils.JanusCallback janusCallback = new team.space.webrtc.janus.utils.JanusCallback() {
        @Override
        public void onCreateSession(BigInteger sessionId) {
            janusClient.attachPlugin("janus.plugin.videoroom");
        }

        @Override
        public void onAttached(BigInteger handleId) {
            System.out.println("onAttached");
            videoRoomHandlerId = handleId;
            janusClient.joinRoom(handleId, room.getId(), userName);


        }

        @Override
        public void onSubscribeAttached(BigInteger subscriptionHandleId, BigInteger feedId) {
            Publisher publisher = room.findPublisherById(feedId);
            if (publisher != null) {
                publisher.setHandleId(subscriptionHandleId);
                janusClient.subscribe(subscriptionHandleId, room.getId(), feedId);
            }
        }

        @Override
        public void onDetached(BigInteger handleId) {
            videoRoomHandlerId = null;
        }

        @Override
        public void onHangup(BigInteger handleId) {

        }

        @Override
        public void onMessage(BigInteger sender, BigInteger handleId, JSONObject msg, JSONObject jsep) {
            System.out.println("onMessage  : " + msg);
            if (!msg.has("videoroom")) {
                return;
            }
            try {
                String type = msg.getString("videoroom");
                if ("joined".equals(type)) {
                    // offer
                    createOffer(peerConnection, new CreateOfferCallback() {
                        @Override
                        public void onCreateOfferSuccess(RTCSessionDescription sdp) {
                            if (videoRoomHandlerId != null) {
                                janusClient.publish(videoRoomHandlerId, sdp);
                            }
                        }

                        @Override
                        public void onCreateFailed(String error) {

                        }
                    });

                    JSONArray publishers = msg.getJSONArray("publishers");
                    handleNewPublishers(publishers);
                } else if ("event".equals(type)) {
                    if (msg.has("configured") && msg.getString("configured").equals("ok") && jsep != null) {
                        //  sdp answer
                        String sdp = jsep.getString("sdp");
                        peerConnection.setRemoteDescription(new RTCSessionDescription(RTCSdpType.ANSWER, sdp), new SetSessionDescriptionObserver() {
                            @Override
                            public void onSuccess() {
                                System.out.println(" setRemoteDescription onCreateSuccess");
                                Platform.runLater(() -> {

                                    VideoItem videoItem = addNewVideoItem(null, userName);
                                    videoItem.setPeerConnection(peerConnection);
                                    videoItem.setVideoTrack(videoTrack);
                                    videoItemList
                                            .stream()
                                            .filter(item -> item.getDisplay().equals(userName))
                                            .forEach(item -> {
                                                item.setPeerConnection(peerConnection);
                                                item.setVideoTrack(videoTrack);
                                            });
                                });
                            }
                            @Override
                            public void onFailure(String error) {
                                System.err.println("setRemoteDescription onFailure   - " + error);

                            }
                        });
                    } else if (msg.has("unpublished")) {
                        Long unPublishdUserId = msg.getLong("unpublished");
                        System.out.println("unPublishdUserId  " + unPublishdUserId);
                    } else if (msg.has("leaving")) {
                        System.out.println("leaving - ### userId :  "  + msg);
                        BigInteger leavingUserId = (msg.getBigInteger("leaving"));
                        room.removePublisherById(leavingUserId);
                        Platform.runLater(() -> {

                            Iterator<VideoItem> it = videoItemList.iterator();
                            int index = 0;
                            while (it.hasNext()) {
                                VideoItem next = it.next();
                                if (leavingUserId.equals(next.getUserId())) {
                                    it.remove();
                                   // videoItemList.remove(next);
                                }
                                index++;
                            }

                        });
                    } else if (msg.has("publishers")) {
                        System.out.println("publishers - ###");
                        JSONArray publishers = msg.getJSONArray("publishers");
                        handleNewPublishers(publishers);
                    } else if (msg.has("started") && msg.getString("started").equals("ok")) {
                        System.out.println("subscription started ok");
                    }
                } else if ("attached".equals(type) && jsep != null) {
                    String sdp = jsep.getString("sdp");
                    BigInteger feedId = msg.getBigInteger("id");
                    String display = msg.getString("display");
                    Publisher publisher = room.findPublisherById(feedId);
                    VideoItem videoItem = addNewVideoItem(feedId, display);
                    VideoView videoFrameNode = (VideoView) centerRoot.lookup(String.valueOf(videoItem.getUserId()));

                    RTCPeerConnection peerConnection = createPeerConnection(new CreatePeerConnectionCallback() {
                        @Override
                        public void onIceGatheringComplete() {
                            janusClient.trickleCandidateComplete(sender);
                        }

                        @Override
                        public void onIceCandidate(RTCIceCandidate candidate) {
                            janusClient.trickleCandidate(sender, candidate);
                        }

                        @Override
                        public void onIceCandidatesRemoved(RTCIceCandidate[] candidates) {
                        }


                        @Override
                        public void publishFrame(VideoTrack videoTrack, VideoFrame frame) {
                            Platform.runLater(() -> {
                                centerRoot.getChildren().stream()
                                        .filter(node -> node instanceof VideoView && !node.getId().equals(null) && !node.getId().equals("null"))
                                        .forEach(node -> {
                                            VideoView videoView = (VideoView) node;
                                            centerRoot.lookup(String.valueOf(videoItem.getUserId()));
                                            if (videoView != null) {
                                                try {
                                                    frame.retain();
                                                    videoView.setVideoFrame(frame);
                                                    frame.release();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    System.err.println("Render Error : " + e.getMessage());
                                                }
                                            }
                                        });
                            });
                        }

                        @Override
                        public void onRemoveStream(MediaStream stream) {
                            // videoItem.videoTrack = null;
                            videoItem.setVideoTrack(null);

                            videoItemList
                                    .stream()
                                    .filter(item -> item.getUserId().equals(videoItem.getUserId()))
                                    .forEach(item -> item.setVideoTrack(null)  );
                        }
                    });

                    videoItem.setPeerConnection(peerConnection);
                    peerConnection.setRemoteDescription(new RTCSessionDescription(RTCSdpType.OFFER, sdp), new SetSessionDescriptionObserver() {

                        @Override
                        public void onSuccess() {
                            System.out.println("setRemoteDescription onSetSuccess");

                            createAnswer(peerConnection, new CreateAnswerCallback() {
                                @Override
                                public void onSetAnswerSuccess(RTCSessionDescription sdp) {
                                    janusClient.subscriptionStart(publisher.getHandleId(), room.getId(), sdp);
                                }

                                @Override
                                public void onSetAnswerFailed(String error) {

                                }
                            });
                        }

                        @Override
                        public void onFailure(String s) {
                            System.err.println("setRemoteDescription onFailure  : " + s);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onIceCandidate(BigInteger handleId, JSONObject candidate) {
        }

        @Override
        public void onDestroySession(BigInteger sessionId) {

        }

        @Override
        public void onError(String error) {

        }
    };

    private PeerConnectionFactory createPeerConnectionFactory() {

        PeerConnectionFactory factory11 = new PeerConnectionFactory();

        VideoDeviceSource vid = new VideoDeviceSource();
        VideoDevice device = MediaDevices.getVideoCaptureDevices().get(0);
        vid.setVideoCaptureDevice(device);
        vid.setVideoCaptureCapability(MediaDevices.getVideoCaptureCapabilities(device).get(0)); //I believe index 0 is auto-resolution, 17 is 1280x720 @ 10fps

        AudioTrack audioTrack;
        VideoTrack videoTrack;
        AudioOptions audioOptions = new AudioOptions();
        audioOptions.noiseSuppression = true;

        AudioTrackSource audioSource = factory11.createAudioSource(audioOptions);
        audioTrack = factory11.createAudioTrack("AUDIO", audioSource);
        videoTrack = factory11.createVideoTrack("CAM", vid);

        return factory11;
    }

    private void handleNewPublishers(JSONArray publishers) {
        for (int i = 0; i < publishers.length(); i++) {
            try {
                JSONObject publishObj = publishers.getJSONObject(i);
                BigInteger feedId = publishObj.getBigInteger("id");
                String display = publishObj.getString("display");

                janusClient.subscribeAttach(feedId);

                room.addPublisher(new Publisher(feedId, display));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private RTCPeerConnection createPeerConnection(CreatePeerConnectionCallback callback) {


        RTCIceServer stunServer = new RTCIceServer();
        stunServer.urls.add("stun:stunserver.org:3478");
        stunServer.urls.add("stun:webrtc.encmed.cn:5349");
        RTCConfiguration config = new RTCConfiguration();
        config.iceServers.add(stunServer);

        return peerConnectionFactory.createPeerConnection(config, new PeerConnectionObserver() {


            @Override
            public void onSignalingChange(RTCSignalingState newState) {

                System.out.println("onSignalingChange " + newState);
            }

            @Override
            public void onIceConnectionChange(RTCIceConnectionState newState) {
//
                System.out.println("onIceConnectionChange " + newState);
            }

            @Override
            public void onIceConnectionReceivingChange(boolean receiving) {
                System.out.println("onIceConnectionReceivingChange " + receiving);
//
            }

            @Override
            public void onIceGatheringChange(RTCIceGatheringState newState) {
                System.out.println("onIceGatheringChange " + newState);

                if (newState == RTCIceGatheringState.COMPLETE) {
                    if (callback != null) {
                        callback.onIceGatheringComplete();
                    }
                }
            }

            @Override
            public void onIceCandidate(RTCIceCandidate candidate) {

                if (callback != null) {
                    callback.onIceCandidate(candidate);
                }
            }

            @Override
            public void onIceCandidatesRemoved(RTCIceCandidate[] candidates) {

                System.out.println("onIceCandidatesRemoved");
                if (callback != null) {
                    callback.onIceCandidatesRemoved(candidates);
                }
            }

            @Override
            public void onRemoveTrack(RTCRtpReceiver receiver) {
                //  PeerConnectionObserver.super.onRemoveTrack(receiver);
            }

            @Override
            public void onTrack(RTCRtpTransceiver transceiver) {
                System.out.println("::::: on-Track :::::");

                MediaStreamTrack track = transceiver.getReceiver().getTrack();
                String kind = track.getKind();
               /* if (kind.equals(MediaStreamTrack.AUDIO_TRACK_KIND) && audioTrackSink != null) {
                    AudioTrack audioTrack = (AudioTrack) track;
                    audioTrack.addSink(audioTrackSink);
                }
                else if (kind.equals(MediaStreamTrack.VIDEO_TRACK_KIND) && nonNull(videoTrackSink)) {
                    VideoTrack videoTrack = (VideoTrack) track;
                    videoTrack.addSink(videoTrackSink);
                }*/
                if (kind.equals(MediaStreamTrack.VIDEO_TRACK_KIND)) {
                    VideoTrack videoTrack = (VideoTrack) track;
                    videoTrack.addSink(frame -> {
                        // sink data to the videoTrackSink video from

                        if (callback != null) {
                            callback.publishFrame(videoTrack, frame);
                        }
                    });
                }
            }

            @Override
            public void onRemoveStream(MediaStream stream) {

                System.out.println("onRemoveStream () ");

                if (callback != null) {
                    callback.onRemoveStream(stream);
                }
            }

            @Override
            public void onDataChannel(RTCDataChannel dataChannel) {

            }

            @Override
            public void onRenegotiationNeeded() {

            }

            @Override
            public void onAddTrack(RTCRtpReceiver receiver, MediaStream[] mediaStreams) {

            }
        });
    }


    private void createOffer(RTCPeerConnection peerConnection, CreateOfferCallback callback) {

        RTCOfferOptions options = new RTCOfferOptions();

        peerConnection.createOffer(options, new CreateSessionDescriptionObserver() {
            @Override
            public void onSuccess(RTCSessionDescription rtcSessionDescription) {
                peerConnection.setLocalDescription(rtcSessionDescription, new SetSessionDescriptionObserver() {

                    @Override
                    public void onSuccess() {
                        System.out.println("xxxxxxxvvvv setLocalDescription success");
                       /*

                        String payload = String.format("{\"janus\" : \"message\", \"transaction\" : \"1234\", \"apisecret\":\"%s\", \"body\" : {\"audio\" : false, \"video\" : false}, \"jsep\" : {\"type\" : \"%s\", \"sdp\" : \"%s\"}}",
                                "apiSecret",
                                rtcSessionDescription.sdpType.toString(),
                                rtcSessionDescription.sdp.toString().trim().replace("\r\n", "\\n"));
                        System.err.println(payload);*/
                    }

                    @Override
                    public void onFailure(String error) {
                        System.err.println("setLocalDescription failure" + error);

                    }
                });
                if (callback != null) {
                    callback.onCreateOfferSuccess(rtcSessionDescription);
                }
            }

            @Override
            public void onFailure(String error) {
                if (callback != null) {
                    callback.onCreateFailed(error);
                }
            }
        });
    }


    private void createAnswer(RTCPeerConnection peerConnection, CreateAnswerCallback callback) {

        RTCAnswerOptions answerOptions = new RTCAnswerOptions();
        peerConnection.createAnswer(answerOptions, new CreateSessionDescriptionObserver() {
            @Override
            public void onSuccess(RTCSessionDescription rtcSessionDescription) {
                peerConnection.setLocalDescription(rtcSessionDescription, new SetSessionDescriptionObserver() {

                    @Override
                    public void onSuccess() {
                        System.out.println("setLocalDescription success");
                        if (callback != null) {
                            callback.onSetAnswerSuccess(rtcSessionDescription);
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        System.out.println("setLocalDescription failure");
                        if (callback != null) {
                            callback.onSetAnswerFailed(error);
                        }
                    }
                });

            }

            @Override
            public void onFailure(String s) {
                System.out.println("createAnswer failure" + s);
            }
        });


    }

    VideoItem addNewVideoItem(BigInteger userId, String display) {
        VideoItem videoItem = new VideoItem();
        videoItem.setUserId(userId);
        videoItem.setDisplay(display);
        videoItemList.add(videoItem);
        return videoItem;
    }

    VideoItem addNewVideoItem(BigInteger userId, String display, RTCPeerConnection peerConnection, VideoTrack videoTrack) {
        VideoItem videoItem = new VideoItem();
        videoItem.setUserId(userId);
        videoItem.setDisplay(display);
        videoItem.setPeerConnection(peerConnection);
        videoItem.setVideoTrack(videoTrack);
        videoItemList.add(videoItem);
        return videoItem;
    }


    public static FlowPane renderCallers(List<VideoItem> videoItemList, VideoTrack videoTrack) {
        FlowPane flowContainer = new FlowPane();


        videoItemList.forEach(caller -> {
            VideoView videoView = new VideoView();
            Button videoBtn = new Button(caller.getDisplay());
            videoBtn.setMnemonicParsing(false);
            videoBtn.getStyleClass().add("cat-button");
            videoBtn.setPrefWidth(160.0);
            videoBtn.setMaxWidth(160.0);
            videoBtn.setPrefHeight(200.0);
            videoBtn.setMaxHeight(200.0);
            videoBtn.setWrapText(true);
            videoBtn.setTextAlignment(TextAlignment.CENTER);
            videoBtn.setAlignment(Pos.CENTER);
            videoBtn.setContentDisplay(ContentDisplay.TOP);

            videoView.resize(100, 100);
            videoBtn.setGraphic(videoView);
            videoBtn.setOnAction(e2 -> {
                System.out.println("videoBtn.setOnAction");
            });
            videoTrack.addSink(videoView::setVideoFrame);
            flowContainer.getChildren().add(videoBtn);
        });

        return flowContainer;
    }

    void addViewItem(VideoTrack videoTrack, boolean islocal) {

        VideoView localVideoView = new VideoView();

        localVideoView.resize(400, 400);

        centerRoot.getChildren().add(localVideoView);
        videoTrack.addSink(localVideoView::setVideoFrame);
        //if(islocal){
            /*try {
                videoSource.start();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        // }

        System.out.println("xxxxxxx  addViewItem");

    }

    public static FlowPane renderACaller(VideoItem videoItem, VideoTrack videoTrack, VideoDeviceSource videoSource) {
        FlowPane flowContainer = new FlowPane();

        VideoView videoView = new VideoView();
        Button videoBtn = new Button(videoItem == null ? "mewadii" : videoItem.getDisplay());
        videoBtn.setMnemonicParsing(false);
        videoBtn.getStyleClass().add("cat-button");
        videoBtn.setPrefWidth(160.0);
        videoBtn.setMaxWidth(160.0);
        videoBtn.setPrefHeight(200.0);
        videoBtn.setMaxHeight(200.0);
        videoBtn.setWrapText(true);
        videoBtn.setTextAlignment(TextAlignment.CENTER);
        videoBtn.setAlignment(Pos.CENTER);
        videoBtn.setContentDisplay(ContentDisplay.TOP);

        videoView.resize(100, 100);
        videoBtn.setGraphic(videoView);
        videoBtn.setOnAction(e2 -> {
            System.out.println("videoBtn.setOnAction");
        });
        videoTrack.addSink(videoView::setVideoFrame);
        flowContainer.getChildren().add(videoBtn);
        try {
            videoSource.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flowContainer;
    }

}
