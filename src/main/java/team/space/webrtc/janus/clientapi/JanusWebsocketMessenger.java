package team.space.webrtc.janus.clientapi;

import org.json.JSONObject;
import team.space.webrtc.janus.utils.WebSocketChannel;

import java.math.BigInteger;

public class JanusWebsocketMessenger implements IJanusMessenger , WebSocketChannel.WebSocketCallback {
    private final String uri;
    private final IJanusMessageObserver handler;
    private final JanusMessengerType type = JanusMessengerType.websocket;
    private WebSocketChannel client = null;

    public JanusWebsocketMessenger(String uri, IJanusMessageObserver handler) {
        this.uri = uri;
        this.handler = handler;
    }


    @Override
    public void connect() {
        client = new WebSocketChannel();
        client.setWebSocketCallback(this);
        client.connect(uri);

    }

    @Override
    public void disconnect() {
        client.close();
    }

    @Override
    public void sendMessage(String message) {
        client.sendMessage(message);
    }

    @Override
    public void sendMessage(String message, BigInteger session_id) {
        sendMessage(message);
    }

    @Override
    public void sendMessage(String message, BigInteger session_id, BigInteger handle_id) {
        sendMessage(message);
    }

    @Override
    public void receivedMessage(String message) {
        try {
            JSONObject obj = new JSONObject(message);
            handler.receivedNewMessage(obj);
        } catch (Exception ex) {
            handler.onError(ex);
        }
    }

    @Override
    public JanusMessengerType getMessengerType() {
        return type;
    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onMessage(String text) {
        sendMessage(text);
    }

    @Override
    public void onClosed() {
        onClose(-1, "unknown", true);
    }

    private void onClose(int code, String reason, boolean remote) {
        handler.onClose();
    }

    private void onError(Exception ex) {
        handler.onError(ex);
    }
}
