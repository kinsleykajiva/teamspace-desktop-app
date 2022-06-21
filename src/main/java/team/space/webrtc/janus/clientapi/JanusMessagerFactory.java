package team.space.webrtc.janus.clientapi;

public class JanusMessagerFactory {
    public static IJanusMessenger createMessager(String uri, IJanusMessageObserver handler) {

        /*if (uri.indexOf("ws") == 0) {
            return new JanusWebsocketMessenger(uri, handler);
        }*/ /*else {
            return new JanusRestMessenger(uri, handler);
        }*/
        return new JanusWebsocketMessenger(uri, handler);
    }
}
