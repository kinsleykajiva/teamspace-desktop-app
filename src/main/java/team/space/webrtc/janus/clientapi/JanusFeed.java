package team.space.webrtc.janus.clientapi;


import java.math.BigInteger;


import team.space.requests.getallusers.User;
import team.space.webrtc.janus.utils.VideoView;


public class JanusFeed {

    private BigInteger feedID;  // Feed 구분 ID. 서버에서 자동으로 생성하기 때문에 이 코드로 사용자를 인식할 수는 없다.
    private String displayName; // 여기에선 사용자 아이디를 말한다.
    private User publisherUser; // Feed 를 생성한 사용자 정보. displayName 을 통해

    private VideoView videoRenderer;    // 사용자의 영상통화 화면을 렌더링하는 객체

    private IJanusPluginCallbacks pluginCallbacks;  // Feed 와 연결 수립, 연결 해제 등의 이벤트를 처리하는 콜백 리스너

    /*
        생성자 정의
     */

    public JanusFeed(){}

    public JanusFeed(BigInteger feedID, String displayName, User publisherUser, VideoView videoRenderer, IJanusPluginCallbacks pluginCallbacks){
        this.feedID = feedID;
        this.displayName = displayName;
        this.publisherUser = publisherUser;
        this.videoRenderer = videoRenderer;
        this.pluginCallbacks = pluginCallbacks;
    }


    /*
    getter, setter 정의
     */

    public BigInteger getFeedID() {
        return feedID;
    }

    public void setFeedID(BigInteger feedID) {
        this.feedID = feedID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public User getPublisherUser() {
        return publisherUser;
    }

    public void setPublisherUser(User publisherUser) {
        this.publisherUser = publisherUser;
    }

    public IJanusPluginCallbacks getPluginCallbacks() {
        return pluginCallbacks;
    }

    public void setPluginCallbacks(IJanusPluginCallbacks pluginCallbacks) {
        this.pluginCallbacks = pluginCallbacks;
    }

    public VideoView getVideoRenderer() {
        return videoRenderer;
    }

    public void setVideoRenderer(VideoView videoRenderer) {
        this.videoRenderer = videoRenderer;
    }
}
