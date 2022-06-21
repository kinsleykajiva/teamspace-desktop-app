package team.space.webrtc.janus.clientapi;

import dev.onvoid.webrtc.RTCIceServer;

import java.util.List;

public interface IJanusGatewayCallbacks extends IJanusCallbacks {
    public void onSuccess();

    public void onDestroy();

    public String getServerUri();

    public RTCIceServer getIceServers();

    public Boolean getIpv6Support();

    public Integer getMaxPollEvents();
}
