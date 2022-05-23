package team.space.utils;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class MediaUtils {

private MediaPlayer mediaPlayerIncomingCall ;
private MediaPlayer mediaPlayerAlert ;

    public  void stopNotificationsAlert() {
        if(mediaPlayerAlert != null) {
            mediaPlayerAlert.stop();
    }
    }

    public  void stopIncomingCallAlert() {
        if(mediaPlayerIncomingCall != null) {
            mediaPlayerIncomingCall.stop();
        }
    }

    public  void playNotificationsAlert() {
        Platform.runLater(() -> {
            Media sound = new Media(getClass().getResource("/sounds/alert-tone.mp3").toExternalForm());
            mediaPlayerAlert = new MediaPlayer(sound);
            mediaPlayerAlert.setVolume(1);
            mediaPlayerAlert.setCycleCount(1);
            mediaPlayerAlert.play();
        });
    }
    public  void playIncomingCallAlert() {
        Platform.runLater(() -> {
            Media sound = new Media(getClass().getResource("/sounds/incoming-ringtone.mp3").toExternalForm());
            mediaPlayerIncomingCall = new MediaPlayer(sound);
            mediaPlayerIncomingCall.setVolume(100);
            mediaPlayerIncomingCall.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayerIncomingCall.play();
        });
    }
}
