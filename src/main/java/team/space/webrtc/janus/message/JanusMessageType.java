package team.space.webrtc.janus.message;


import java.util.NoSuchElementException;

/**
 * Event types used to classify messages when communicating with the Janus WebRTC Server.
 * @see <a href="https://janus.conf.meetecho.com/docs/videoroom">Janus Official Video Room Plugin Docs  </a>
 * @see <a href="https://janus.conf.meetecho.com/docs/audiobridge.html">Janus Official Audio Bridge Plugin Docs  </a>
 * */
public enum JanusMessageType {


    /**
     * Event related to messages being sent from plugins.
     * <ul>
     *     <li> Vide Plugin below  </li>
     *     <li> If successful, a configured event will be sent back </li>
     *     <li> the plugin tear down the PeerConnection, and remove the publisher from the list of active streams. If successful, the response will look like this: {
     *         "videoroom" : "event",
     *         "unpublished" : "ok"
     *          }
     *      </li>
     *      <li> As soon as the PeerConnection is gone, all the other participants will also be notified about the fact that the stream is no longer available: <br> {  "videoroom" : "event",  "room" : <room ID>, "unpublished" : <unique ID of the publisher who unpublished>} </li>
     *      <li>If successful, a configured event will be sent back as before, formatted like this:<br> { "videoroom" : "event", "configured" : "ok"}</li>
     *      <li>If leaving video room is successful, the response will look like this:<br> {"videoroom" : "event", "leaving" : "ok"}  <br> Other participants will receive a "leaving" event to notify them the circumstance: <br> { "videoroom" : "event", "room" : <room ID>,  "leaving : <unique ID of the participant who left>} </li>
     *      <li>To complete the setup of the PeerConnection the subscriber is supposed to send a JSEP SDP answer back to the plugin This is done by means of a start request, If successful this request returns a started event:<br> {  "videoroom" : "event", started" : "ok"}</li>
     *      <li> For paused event request made events : <br> {  "videoroom" : "event",  "started" : "ok"} </li>
     *      <li> For switch request event :<br> {  "videoroom" : "event", "switched" : "ok", "room" : <room ID>, "changes" : <number of successful changes (may be smaller than the size of the streams array provided in the request)>,  "streams" : [   // Current configuration of the subscription, same format as when subscribing    // Will contain info on all streams, not only those that have been updated         ]}  </li>
     *      <li> If successful, the plugin will attempt to tear down the PeerConnection, and will send back a left event:<br> { "videoroom" : "event", "left" : "ok",}  </li>
     *      <li> Audio Plugin below</li>
     *      <li> All the participants will receive an event notification with the ID of the participant who just left: {"audiobridge" : "event",  "room" : <numeric ID of the room>, "leaving" : <numeric ID of the participant who left>}</li>
     *      <li> An error instead (and the same applies to all other requests, so this won't be repeated) would provide both an error code and a more verbose description of the cause of the issue:: <br> { "audiobridge" : "event", "error_code" : <numeric ID, check Macros below>,"error" : "<error description as a string>"} </li>
     * </ul>
     *
     */
    EVENT("event"),

    /**
     * To get generic info from the server.
     * <ul>
     *     <li>  </li>
     *     <li>  </li>
     * </ul>
     */
    INFO("info"),

    /**
     * Info response from the server.
     */
    SERVER_INFO("server_info"),

    /**
     * To create a new audio/video room session with the server.
     * <ul>
     *     <li> A successful creation procedure will result in a created response: <br> { "audiobridge" : "created",  "room" : <unique numeric ID>, "permanent" : <true if saved to config file, false if not>}  </li>
     *     <li> A successful creation procedure will result in a created response: <br> { "videoroom" : "created",   "room" : <unique numeric ID>,  "permanent" : <true if saved to config file, false if not>}   </li>
     * </ul>
     *
     */
    CREATE("create"),

    /**
     * If the request is successful.
     * <ul>
     *     <li>video plugin below</li>
     *     <li> You can check whether a room exists using the <strong>exists</strong> request, which has to be formatted as follows:<br> {  "videoroom" : "success",    "room" : <unique numeric ID>,  "exists" : <true|false>}  </li>
     *     <li> You can configure whether to check tokens or add/remove people who can join a room using the <strong>allowed</strong> request . A successful request will result in a <strong>success</strong> response:<br>{ "videoroom" : "success","room" : <unique numeric ID>,  "allowed" : [ // Updated, complete, list of allowed tokens (only for enable|add|remove)   ]} </li>
     *     <li> for <strong>kick</strong> ,  <strong>moderate</strong>,  <strong>list</strong> request events . A successful request will result in a success response:<br> {  "videoroom" : "success",}</li>
     *     <li>audio bridge plugin below</li>
     *     <li>You can check whether a room exists using the <strong>exists</strong> request,A successful request will result in a success response: <br> { "audiobridge" : "success",  "room" : <unique numeric ID>,  "allowed" : [  // Updated, complete, list of allowed tokens (only for enable|add|remove)  ]} </li>
     *     <li>for  <strong>kick</strong> , <strong><mute|unmute, whether to mute or unmute></strong> , <strong>list </strong> , <strong>stop_file</strong> , <strong>resetdecoder</strong> , <strong>is_playing</strong>, <strong>stop_rtp_forward</strong> , <strong>play_file</strong> , request   . A successful request will result in a success response:<br> { "audiobridge" : "success", </li>
     *
     * </ul>
     */
    SUCCESS("success"),

    /**
     * If an asynchronous request has been accepted.
     */
    ACK("ack"),

    /**
     * To create a new handle to attach to a plugin.
     */
    ATTACH("attach"),

    /**
     * To detach from a plugin and destroy the plugin handle.
     */
    DETACH("detach"),

    /**
     * Occurs when e.g. a session was destroyed due to inactivity.
     */
    TIMEOUT("timeout"),

    /**
     * To destroy the current session.
     */
    DESTROY("destroy"),

    /**
     * If the server failed to process the request.
     */
    ERROR("error"),

    /**
     * For everything that is related to the communication with a plugin.
     */
    MESSAGE("message"),

    /**
     * To keep a session alive.
     */
    KEEP_ALIVE("keepalive"),


    // WebRTC-related events.

    /**
     * To trickle ICE candidates.
     */
    TRICKLE("trickle"),

    /**
     * ICE and DTLS succeeded. A PeerConnection has been correctly established
     * with the user/application.
     */
    WEBRTC_UP("webrtcup"),

    /**
     * Whether Janus is receiving audio/video on the PeerConnection.
     */
    MEDIA("media"),

    /**
     * Whether Janus is reporting trouble sending/receiving media on the
     * PeerConnection.
     */
    SLOW_LINK("slowlink"),

    /**
     * The PeerConnection was closed, either by Janus or by the
     * user/application, and as such cannot be used anymore.
     */
    HANGUP("hangup");






    private final String type;
    JanusMessageType(String event) {
        this.type = event;
    }

    public String getType() {
        return type;
    }

    public static JanusMessageType fromString(String typeStr) {
        for (var value : JanusMessageType.values()) {
            if (value.getType().equals(typeStr)) {
                return value;
            }
        }

        throw new NoSuchElementException();
    }

}
