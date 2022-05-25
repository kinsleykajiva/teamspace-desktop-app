package team.space.webrtc.webrtcutils;

import dev.onvoid.webrtc.media.Device;
import dev.onvoid.webrtc.media.DeviceChangeListener;
import dev.onvoid.webrtc.media.MediaDevices;

public class WebRTCUtils {

    private static  boolean isMediaReady = false;


    private static DeviceChangeListener listener = new DeviceChangeListener() {

        @Override
        public void deviceConnected(Device device) {
            System.out.println(WebRTCUtils.class + "- Device connected: " + device.getName());

        }

        @Override
        public void deviceDisconnected(Device device) {
            System.out.println(WebRTCUtils.class + "- Device connected: " + device.getName());
        }
    };
    public static boolean isMediaReady(){
        MediaDevices.addDeviceChangeListener(listener);

        return false;
    }

    public  static  void initWebRTC(){
        java.lang.System.setProperty("java.net.preferIPv6Addresses", "false");
        java.lang.System.setProperty("java.net.preferIPv4Stack", "true");


    }

}
