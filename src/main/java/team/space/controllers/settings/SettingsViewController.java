package team.space.controllers.settings;

import dev.onvoid.webrtc.media.audio.AudioDevice;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;
import team.space.controllers.settings.tabs.AudioVideoTabPane;
import team.space.webrtc.webrtcutils.WebRTCUtils;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class SettingsViewController implements Initializable {

    @FXML
    private ChoiceBox<WebRTCUtils.CodecPreference> codecPreference;
    @FXML
    private ChoiceBox<AudioDevice> audioOutput;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setAudioOutputDevices(WebRTCUtils.getAudioRenderDevicesBlocking());
        codecPreference.setItems(FXCollections.observableArrayList(WebRTCUtils.CodecPreference.getAvailablePreferences()));
        codecPreference.setValue(WebRTCUtils.CodecPreference.VP9);

        audioOutput.setConverter(new StringConverter<>() {
            @Override
            public String toString(final AudioDevice audioDevice) {
                return audioDevice.getName();
            }

            @Override
            public AudioDevice fromString(final String string) {
                return audioOutput.getItems().stream()
                        .filter(audioDevice -> audioDevice.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }
    public void setAudioOutputDevices(final List<AudioDevice> audioDevices) {
        final ObservableList<AudioDevice> observableList = FXCollections.observableList(audioDevices);
        FXCollections.sort(observableList, Comparator.comparing(AudioDevice::getName));
        audioOutput.setItems(observableList);
        audioOutput.getSelectionModel().selectFirst();
    }
    public WebRTCUtils.CodecPreference getPreferredVideoCodec() {
        return codecPreference.getValue();
    }

    public AudioDevice getSelectedAudioDevice() {
        return audioOutput.getValue();
    }
}
