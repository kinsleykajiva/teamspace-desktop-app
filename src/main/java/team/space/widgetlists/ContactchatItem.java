package team.space.widgetlists;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import team.space.models.Contact;

import java.io.IOException;

public class ContactchatItem {

    private Contact contact;

    @FXML  public javafx.scene.control.Label userNameLabel,userNameText,messageTimeLabel,nombreMessageLabel;
    @FXML  public AnchorPane rootAncherPane_user_custome_cell;
    @FXML  public ImageView avatarImageView;
    @FXML  public Circle circleNumberCount;
    @FXML  public Rectangle rectangleCurrentSelect;



    public ContactchatItem( Contact contact) {
        //
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource( "/views/user_custom_cell_view.fxml" ));
        fxmlLoader.setController(this);
        try {
            // fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        rectangleCurrentSelect.setVisible(false);
       this. contact = contact;
        userNameText.setText(/*contact.getFullName()*/ "Last chatMessage Shown Here");
        userNameLabel.setText(contact.getFullName());
        nombreMessageLabel.setText(/*contact.getFullName()*/"1");
    }



    public AnchorPane getRootAncherPane_user_custome_cell() {
        return rootAncherPane_user_custome_cell;
    }


}
