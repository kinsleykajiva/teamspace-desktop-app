package team.space.webrtc.webrtcutils;

import com.google.gson.Gson;
import dev.onvoid.webrtc.PeerConnectionObserver;
import dev.onvoid.webrtc.RTCIceServer;
import dev.onvoid.webrtc.RTCRtpTransceiver;
import dev.onvoid.webrtc.media.MediaStream;
import dev.onvoid.webrtc.media.MediaStreamTrack;
import dev.onvoid.webrtc.media.video.VideoTrack;
import org.json.JSONArray;
import org.json.JSONObject;
import team.space.controllers.MainController;
import team.space.requests.getallusers.User;
import team.space.webrtc.janus.clientapi.*;
import team.space.webrtc.janus.utils.VideoView;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

public class GroupVideoCall {
    private final String TAG = "GroupVideoCall";
    public static JanusServer janusServer;
    private JanusPluginHandle pluginHandle;


    private VideoView localRender;
    private Stack<VideoView> availableRemoteRenderers = new Stack<>();
    private HashMap<BigInteger, VideoView> remoteRenderers = new HashMap<>();
    private HashMap<BigInteger, JanusFeed> remoteCallbacks = new HashMap<>();


    private int groupVideoCallRoomID;
    private BigInteger myFeedID;
    private User senderUser;
    private HashMap<Integer, User> groupVideoCallUsers;

    private boolean loginUserEqualSender;

    private void fcmSend() {


    }

    public GroupVideoCall(VideoView localRender, ArrayList<VideoView> remoteRenders, User sender, HashMap<Integer, User> groupVideoCallUsers) {


        this.loginUserEqualSender = true;
        this.localRender = localRender;

        for (VideoView remoteRender : remoteRenders) {
            this.availableRemoteRenderers.push(remoteRender);
        }


        senderUser = sender;
        this.groupVideoCallUsers = groupVideoCallUsers;


        Random random = new Random();
        groupVideoCallRoomID = random.nextInt(1000) + 2000;
        //groupVideoCallRoomID= 1234;

        janusServer = new JanusServer(new JanusGlobalCallbacks());

    }


    public GroupVideoCall(VideoView localRender, ArrayList<VideoView> remoteRenders, User sender, HashMap<Integer, User> groupVideoCallUsers, int groupVideoCallRoomID) {


        this.loginUserEqualSender = false;
        this.localRender = localRender;

        for (VideoView remoteRender : remoteRenders) {
            this.availableRemoteRenderers.push(remoteRender);
        }
        senderUser = sender;
        this.groupVideoCallUsers = groupVideoCallUsers;

        this.groupVideoCallRoomID = groupVideoCallRoomID;


        janusServer = new JanusServer(new JanusGlobalCallbacks());
        System.out.println("groupVideoCallRoomID:xxxxx  " + groupVideoCallRoomID);

    }




    public void Start() {
        janusServer.Connect();
        System.out.println("Start");
    }


    public class JanusGlobalCallbacks implements IJanusGatewayCallbacks {

        private final String LOCAL_TAG = "JanusGlobalCB";

        public void onSuccess() {
            //  Log.e(LOCAL_TAG, "Janus Gateway connect SUCCESS");
            janusServer.Attach(new JanusVideoRoomCallbacks());
        }

        @Override
        public void onDestroy() {
            //  Log.e(LOCAL_TAG, "Janus Gateway connect DESTROY");
        }

        @Override
        public String getServerUri() {
            return WebRTCUtils.JANUS_URL;
        }


        @Override
        public RTCIceServer getIceServers() {
            RTCIceServer stunServer = new RTCIceServer();
            stunServer.urls.add("stun:stun.l.google.com:19302");
            stunServer.urls.add("stun:stun.voip.eutelia.it:3478");
            return stunServer;
        }

        @Override
        public Boolean getIpv6Support() {
            return Boolean.FALSE;
        }

        @Override
        public Integer getMaxPollEvents() {

            return 0;
        }

        @Override
        public void onCallbackError(String error) {



        }
    }


    public class JanusVideoRoomCallbacks implements IJanusPluginCallbacks {

        private final String LOCAL_TAG = "JanusVideoRoomCreateCB";

        @Override
        public void success(JanusPluginHandle handle) {

            pluginHandle = handle;
            /*if (loginUserEqualSender) {
                createVideoRoom();
            } else {
                registerUsername();
            }*/
            registerUsername();
        }

        @Override
        public void onMessage(JSONObject msg, JSONObject jsep) {
            try {
                String videoroom = msg.getString("videoroom");

                if (videoroom.equals("joined")) {


                    myFeedID = new BigInteger(msg.getString("id"));
                    publishOwnFeed();

                    if (msg.has("publishers")) {


                        JSONArray pubs = msg.getJSONArray("publishers");
                        for (int i = 0; i < pubs.length(); i++) {
                            JSONObject pub = pubs.getJSONObject(i);
                            BigInteger tehId = new BigInteger(pub.getString("id"));
                            String display = pub.getString("display");
                            newRemoteFeed(tehId, display);
                        }
                    }

                } else if (videoroom.equals("destroyed")) {


                } else if (videoroom.equals("event")) {

                    if (msg.has("configured")) {


                    } else if (msg.has("started")) {


                    } else if (msg.has("joining")) {


                    } else if (msg.has("publishers")) {
                        JSONArray pubs = msg.getJSONArray("publishers");
                        for (int i = 0; i < pubs.length(); i++) {
                            JSONObject pub = pubs.getJSONObject(i);
                            //  Log.i("VideoRoomTest", "[publishers] "+pub.getString("id"));
                            newRemoteFeed(new BigInteger(pub.getString("id")), pub.getString("display"));
                        }

                    } else if (msg.has("unpublished")) {

                        String unpublished = msg.getString("unpublished");
                        if ("ok".equals(unpublished)) {   // 자기 자신인 경우


                        } else {
                            BigInteger remoteFeedId = new BigInteger(msg.getString("unpublished"));

                            if (remoteRenderers.containsKey(remoteFeedId)) {

                                // remoteCallbacks.get(remoteFeedId).getVideoRenderer().dispose();
                                //  VideoRendererGui.remove(remoteRenderers.get(remoteFeedId));

                                // availableRemoteRenderers.push(remoteRenderers.remove(remoteFeedId));
                                remoteRenderers.remove(remoteFeedId);

                            }

                            if (remoteRenderers.size() == 0 && availableRemoteRenderers.empty()) {


                                endCall();

                            } else {

                            }
                        }

                    } else if (msg.has("leaving")) {

                        BigInteger id = new BigInteger(msg.getString("leaving"));


                    } else {

                    }
                }

                if (jsep != null) {
                    pluginHandle.handleRemoteJsep(new PluginHandleWebRTCCallbacks(null, jsep, false));
                }

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }

        @Override
        public void onLocalStream(PeerConnectionObserver connectionObserver) {

        }

        @Override
        public void onTrack(RTCRtpTransceiver transceiver) {
            //  stream.videoTracks.get(0).addRenderer(new VideoRenderer(localRender));
            MediaStreamTrack track = transceiver.getReceiver().getTrack();
            String kind = track.getKind();

            if (kind.equals(MediaStreamTrack.VIDEO_TRACK_KIND)) {
                VideoTrack videoTrack = (VideoTrack) track;
                videoTrack.addSink(frame -> {
                    // sink data to the videoTrackSink video from

                    frame.retain();
                    localRender.setVideoFrame(frame);
                    frame.release();
                });
            }


        }


        @Override
        public void onDataOpen(Object data) {
            //  Log.e(LOCAL_TAG, "success()");

        }

        @Override
        public void onData(Object data) {
            //  Log.e(LOCAL_TAG, "onData()");

        }

        @Override
        public void onCleanup() {
            //   Log.e(LOCAL_TAG, "onCleanup()");
        }

        @Override
        public void onDetached() {
            //  Log.e(LOCAL_TAG, "onDetached()");
        }

        @Override
        public JanusSupportedPluginPackages getPlugin() {
            return JanusSupportedPluginPackages.JANUS_VIDEO_ROOM;
        }

        @Override
        public void onCallbackError(String error) {
            //   Log.e(LOCAL_TAG, error);

            /*
                (참고) error 메시지 정리
                 1) Peerconnection factory is not initialized, please initialize via initializeMediaContext so that peerconnections can be made by the plugins
             */
        }
    }


    private void newRemoteFeed(BigInteger id, String display) {

        final String LOCAL_TAG = "newRemoteFeed";

        VideoView myrenderer;
        if (!remoteRenderers.containsKey(id)) {
            if (availableRemoteRenderers.empty()) {

                return;
            }

            remoteRenderers.put(id, availableRemoteRenderers.pop());
        }
        myrenderer = remoteRenderers.get(id);

        ListenerAttachCallbacks newPublisher = new ListenerAttachCallbacks(id, display, myrenderer);

        janusServer.Attach(newPublisher);
    }


    class ListenerAttachCallbacks implements IJanusPluginCallbacks {

        public VideoView videoRenderer;
        private VideoView rendererCallbacks;
        final private BigInteger feedID;
        String display;
        public JanusPluginHandle listener_handle = null;

        final String LOCAL_TAG = "ListenerAttachCB";

        public ListenerAttachCallbacks(BigInteger id, String display, VideoView renderer) {
            this.rendererCallbacks = renderer;
            this.display = display;
            this.feedID = id;

        }

        public void success(JanusPluginHandle handle) {
            listener_handle = handle;
            try {

                JSONObject body = new JSONObject();
                JSONObject msg = new JSONObject();
                body.put("request", "join");
                body.put("room", groupVideoCallRoomID);
                body.put("ptype", "subscriber");

                body.put("feed", feedID);

                msg.put("message", body);
                handle.sendMessage(new PluginHandleSendMessageCallbacks(msg));

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onMessage(JSONObject msg, JSONObject jsep) {

            try {
                String videoroom = msg.getString("videoroom");

                if (videoroom.equals("event")) {
                    if (msg.has("started")) {

                    }

                } else if (videoroom.equals("attached") && jsep != null) {

                    final JSONObject remoteJsep = jsep; // 뭐니?
                    listener_handle.createAnswer(new IPluginHandleWebRTCCallbacks() {
                        @Override
                        public void onSuccess(JSONObject obj) {
                            try {
                                JSONObject mymsg = new JSONObject();
                                JSONObject body = new JSONObject();
                                body.put("request", "start");

                                body.put("room", groupVideoCallRoomID);
                                mymsg.put("message", body);
                                mymsg.put("jsep", obj);
                                listener_handle.sendMessage(new PluginHandleSendMessageCallbacks(mymsg));

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public JSONObject getJsep() {
                            return remoteJsep;
                        }

                        @Override
                        public JanusMediaConstraints getMedia() {
                            JanusMediaConstraints cons = new JanusMediaConstraints();
                            cons.setVideo(null);
                            cons.setRecvAudio(true);
                            cons.setRecvVideo(true);

                            return cons;
                        }

                        @Override
                        public Boolean getTrickle() {
                            return true;
                        }

                        @Override
                        public void trickleCandidateComplete(BigInteger videoRoomHandlerId) {

                        }

                        @Override
                        public void onCallbackError(String error) {

                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onLocalStream(PeerConnectionObserver connectionObserver) {

        }

        @Override
        public void onTrack(RTCRtpTransceiver transceiver) {
            MediaStreamTrack track = transceiver.getReceiver().getTrack();
            String kind = track.getKind();

            if (kind.equals(MediaStreamTrack.VIDEO_TRACK_KIND)) {
                VideoTrack videoTrack = (VideoTrack) track;
                videoTrack.addSink(frame -> {
                    // sink data to the videoTrackSink video from
                    remoteCallbacks.put(feedID, new JanusFeed(feedID, display, groupVideoCallUsers.get(display), videoRenderer, this));

                    frame.retain();
                    rendererCallbacks.setVideoFrame(frame);
                    frame.release();

                });
            }

        }


        @Override
        public void onDataOpen(Object data) {

        }

        @Override
        public void onData(Object data) {

        }

        @Override
        public void onCleanup() {

        }

        @Override
        public void onDetached() {

            //listener_handle.detach();
        }

        @Override
        public JanusSupportedPluginPackages getPlugin() {
            return JanusSupportedPluginPackages.JANUS_VIDEO_ROOM;
        }

        @Override
        public void onCallbackError(String error) {
            //  Log.e(LOCAL_TAG, "[ERROR]" + error);
        }
    }



    private void publishOwnFeed() {

        final String LOCAL_TAG = "publishOwnFeed";

        if (pluginHandle != null) {
            pluginHandle.createOffer(new IPluginHandleWebRTCCallbacks() {
                @Override
                public void onSuccess(JSONObject obj) {
                    try {

                        JSONObject msg = new JSONObject();
                        JSONObject body = new JSONObject();
                        //body.put("request", "configure");
                        body.put("request", "publish"); // publish, configure
                        body.put("audio", true);
                        body.put("video", true);
                        msg.put("message", body);
                        msg.put("jsep", obj);
                        pluginHandle.sendMessage(new PluginHandleSendMessageCallbacks(msg));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public JSONObject getJsep() {
                    return null;
                }

                @Override
                public JanusMediaConstraints getMedia() {
                    JanusMediaConstraints cons = new JanusMediaConstraints();
                    cons.setRecvAudio(false);
                    cons.setRecvVideo(false);

                    cons.setSendAudio(true);
                    cons.setRecvVideo(true);
                    return cons;
                }

                @Override
                public Boolean getTrickle() {
                    return true;
                }

                @Override
                public void trickleCandidateComplete(BigInteger videoRoomHandlerId) {

                }

                @Override
                public void onCallbackError(String error) {
                    //  Log.e(LOCAL_TAG, "onCallbackError()");
                    //  Log.e(LOCAL_TAG, error);
                }
            });
        }
    }


    private void createVideoRoom() {

        String LOCAL_TAG = "createVideoRoom";

        if (pluginHandle != null) {

            JSONObject obj = new JSONObject();
            JSONObject msg = new JSONObject();

            try {
                obj.put("request", "create");
                obj.put("room", groupVideoCallRoomID);
                obj.put("is_private", false);
                obj.put("notify_joining", true);
                obj.put("publishers", 4);
                msg.put("message", obj);


                pluginHandle.sendMessage(new PluginHandleSendMessageCallbacks(msg));

                //    Log.e(LOCAL_TAG, "방번호(" + groupVideoCallRoomID+") 생성됨");

                //registerUsername();


                fcmSend();


            } catch (Exception ex) {

                ex.printStackTrace();
            }

        } else {


        }
    }



    private void registerUsername() {

        if (pluginHandle != null) {

            JSONObject obj = new JSONObject();
            JSONObject msg = new JSONObject();

            try {

                obj.put("request", "join");
                obj.put("room", groupVideoCallRoomID);
                obj.put("ptype", "publisher");
                obj.put("display", senderUser.getId_());
                msg.put("message", obj);

                pluginHandle.sendMessage(new PluginHandleSendMessageCallbacks(msg));

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {

        }
    }


    public void endCall() {
        // android.os.Process.killProcess(android.os.Process.myPid());
    }

}
