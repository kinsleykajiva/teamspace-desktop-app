package team.space.webrtc.janus.clientapi;

import java.math.BigInteger;

public interface IJanusMessenger {
    void connect();

    void disconnect();

    void sendMessage(String message);

    void sendMessage(String message, BigInteger session_id);

    void sendMessage(String message, BigInteger session_id, BigInteger handle_id);

    void receivedMessage(String message);

    JanusMessengerType getMessengerType();
}
