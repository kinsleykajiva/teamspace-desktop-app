package team.space.webrtc.janus.clientapi;

import org.json.JSONObject;

public interface ITransactionCallbacks {
    void reportSuccess(JSONObject obj);

    TransactionType getTransactionType();
}
