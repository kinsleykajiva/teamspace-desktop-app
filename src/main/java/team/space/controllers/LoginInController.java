package team.space.controllers;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.DebugFlags;
import io.objectbox.query.Query;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.SneakyThrows;
import team.space.database.objectio.DBObjectManager;
import team.space.database.objectio.LoginInCache;
import team.space.database.objectio.LoginInCache_;
import team.space.database.objectio.MyObjectBox;
import team.space.requests.resigster.RegisterRoot;
import team.space.utils.Screen;
import team.space.utils.Shared;
import team.space.utils.StageManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.EventObject;
import java.util.ResourceBundle;

import static team.space.network.ReqLogin.loginReq;
import static team.space.network.ReqLogin.registerRequest;

public class LoginInController implements Initializable {




    @FXML
    private VBox vbox;


    private Parent fxml;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
      /*  TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        t.setToX(vbox.getLayoutX() * 20);
        t.play();
        t.setOnFinished((e) ->{
            try{
                fxml = FXMLLoader.load(getClass().getResource("/views/login/SignIn.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(fxml);

                HBox HBoxClose = (HBox) fxml.lookup("#HBoxClose");
                Label txtClose = (Label) HBoxClose.lookup("#txtClose");
                ImageView imgLoadingView = (ImageView) HBoxClose.lookup("#imgLoadingView");
                if(txtClose != null){
                    txtClose.setOnMouseClicked((e1) -> {
                        Platform.exit();
                    });
                }

                Button btnSignIn = (Button) fxml.lookup("#btnSignIn");
                if(btnSignIn != null){
                    btnSignIn.setOnMouseClicked(e2->{
                        System.out.println("Sign in");
                    });
                }



            }catch(IOException ex){

            }
        });*/
        showSignin();


    }


    void registerAction(Label responseMessage ,ImageView loadingEffect ,String fullName, String companyName, String emailAddress, String password){
        loadingEffect.setVisible(true);
        Task<RegisterRoot> task = new Task<RegisterRoot>() {
            @Override
            protected RegisterRoot call() throws Exception {
                return registerRequest( fullName,  companyName,  emailAddress,  password);
            }
        };

        task.setOnSucceeded((e) -> {
            RegisterRoot registerRoot = task.getValue();
            if(registerRoot.isSuccess()){
                System.out.println("Register success");
                showSignin();
            }else{
                responseMessage.setText("Failed.Please try again...");
                System.out.println("Register fail");
            }
            loadingEffect.setVisible(false);
        });
        task.setOnFailed((e) -> {
            loadingEffect.setVisible(false);
            System.err.println("Register fail");
        });
        task.setOnCancelled((e) -> {
            System.err.println("Register fail");
            loadingEffect.setVisible(false);
        });



        new Thread(task).start();


    }

   public static void startMainView(Class clazzz){
        if( StageManager.getStage() != null){
            StageManager.getStage().close();
        }

        Stage primaryStage = new Stage();
        // set application name
        Shared.appName = "TeamSpace Work Application";
        primaryStage.setTitle(Shared.appName);
        // set application version
        Shared.appVersion = "1.0.0";
        // set available languages
        //scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        // set application icon
        primaryStage.setResizable(true);
          primaryStage.getIcons().add(new Image(clazzz.getResourceAsStream("/images/icon.png")));
       // primaryStage.getIcons().addAll(loadIcons("icon-16.png", "icon-24.png", "icon-32.png", "icon-64.png", "icon-128.png", "icon-256.png", "icon-512.png"));
        // set first screen
        Shared.screen = Screen.MAIN_SCREEN;
        new StageManager(primaryStage);
    }

    void logInAction(Label responseMessage ,ImageView loadingEffect , String emailAddress, String password){
        System.err.println("Log in");
        loadingEffect.setVisible(true);
        Task<LoginInCache> task = new Task<>() {
            @Override
            protected LoginInCache call() throws Exception {
                return loginReq(emailAddress, password);
            }
        };

        task.setOnSucceeded((e) -> {
            LoginInCache registerRoot = task.getValue();
            if(registerRoot != null){

                System.out.println("login success");

                Box<LoginInCache> store = DBObjectManager.getinstance().getStore().boxFor(LoginInCache.class);
                Query<LoginInCache> query = store.query(LoginInCache_.email.equal(emailAddress)).build();

                Shared.LOGGED_USER = query.findFirst();
                query.close();


                Box<LoginInCache> loginInCacheBox = DBObjectManager.getinstance().getStore().boxFor(LoginInCache.class);
                Query<LoginInCache> checkExistsquery = loginInCacheBox.query().build();
               // checkExistsquery.
               var hhh=  checkExistsquery.findFirst();
//                System.out.println("_ LOGGED_USER  :  " + checkExistsquery.get() );
                System.out.println("_ LOGGED_USER  :  " + hhh.getUserId() );
                System.out.println("_ LOGGED_USER  :  " + hhh.getCompanyId() );

                // open the main window or scene
//               var primarStage =  StageManager.getStage();

                startMainView(getClass());

            }else{

                responseMessage.setText("Failed.Please try again...");
                System.out.println("login fail");
                responseMessage.setVisible(true);
                responseMessage.setText("Failed.Please try again...");
            }
            loadingEffect.setVisible(false);
        });
        task.setOnFailed((e) -> {
            loadingEffect.setVisible(false);
           // task.get
            responseMessage.setVisible(true);
            responseMessage.setText("Failed.Please try again...");
            System.err.println("login fail ------- " + task.getMessage());
        });
        task.setOnCancelled((e) -> {
            System.err.println("login fail 5555555");
            loadingEffect.setVisible(false);
            responseMessage.setVisible(true);
            responseMessage.setText("Failed.Please try again...");
        });



        new Thread(task).start();


    }


void showSignin(){
    TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
    t.setToX(vbox.getLayoutX() * 20);
    t.play();
    t.setOnFinished((e) ->{
        try{
            fxml = FXMLLoader.load(getClass().getResource("/views/login/SignIn.fxml"));
            vbox.getChildren().removeAll();
            vbox.getChildren().setAll(fxml);

            HBox HBoxClose = (HBox) fxml.lookup("#HBoxClose");
            Label txtClose = (Label) HBoxClose.lookup("#txtClose");
            ImageView imgLoadingView = (ImageView) HBoxClose.lookup("#imgLoadingView");
            Label txtLoadingMessage = (Label) HBoxClose.lookup("#txtLoadingMessage");
            HBox HBoxtxtEmail = (HBox) fxml.lookup("#HBoxtxtEmail");
            TextField txtEmail = (TextField) HBoxtxtEmail.lookup("#txtEmail");


            HBox HBoxtxtPassword = (HBox) fxml.lookup("#HBoxtxtPassword");
            TextField txtPassword = (TextField) HBoxtxtPassword.lookup("#txtPassword");




//            ImageView imgLoadingView = (ImageView) HBoxClose.lookup("#imgLoadingView");
            if(txtClose != null){
                txtClose.setOnMouseClicked((e1) -> {
                    Platform.exit();
                });
            }

            Button btnSignIn = (Button) fxml.lookup("#btnSignIn");
            if(btnSignIn != null){
                btnSignIn.setOnMouseClicked(e2->{
                    System.err.println("Sign in");



                    if(txtEmail.getText().isEmpty()){
                        txtLoadingMessage.setVisible(true);
                        txtLoadingMessage.setText("Please enter your email");
                        txtEmail.setStyle("-fx-border-color: red");
                        return;
                    }

                    if(txtPassword.getText().isEmpty()){
                        txtLoadingMessage.setVisible(true);
                        txtLoadingMessage.setText("Please enter your password");
                        txtPassword.setStyle("-fx-border-color: red");
                        return;
                    }

                    txtLoadingMessage.setVisible(false);
                    imgLoadingView.setVisible(true);
                    logInAction(txtLoadingMessage ,imgLoadingView, txtEmail.getText(), txtPassword.getText());
                });
            }



        }catch(IOException ex){

        }
    });
}

    @FXML
    private void open_signin(ActionEvent event){
        showSignin();
    }


    /*@FXML
    private void onExitLoginScreen(ActionEvent event){
        Platform.exit();

    }*/

    @FXML
    private void open_signup(ActionEvent event){
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        t.setToX(0);
        t.play();
        t.setOnFinished((e) ->{
            try{
//                sign upp screen
                fxml = FXMLLoader.load(getClass().getResource("/views/login/SignUp.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(fxml);

                HBox HBoxClose = (HBox) fxml.lookup("#HBoxClose");
                Label txtClose = (Label) HBoxClose.lookup("#txtClose");
                ImageView imgLoadingView = (ImageView) HBoxClose.lookup("#imgLoadingView");
                Label txtLoadingMessage = (Label) HBoxClose.lookup("#txtLoadingMessage");

                HBox HBoxtxtCompany = (HBox) fxml.lookup("#HBoxtxtCompany");
                TextField txtCompany = (TextField) HBoxtxtCompany.lookup("#txtCompany");

                HBox HBoxtxtFullName = (HBox) fxml.lookup("#HBoxtxtFullName");
                TextField txtFullName = (TextField) HBoxtxtFullName.lookup("#txtFullName");


                HBox HBoxtxtEmail = (HBox) fxml.lookup("#HBoxtxtEmail");
                TextField txtEmail = (TextField) HBoxtxtEmail.lookup("#txtEmail");


                HBox HBoxtxtPassword = (HBox) fxml.lookup("#HBoxtxtPassword");
                TextField txtPassword = (TextField) HBoxtxtPassword.lookup("#txtPassword");






                if(txtClose != null){
                    txtClose.setOnMouseClicked((e1) -> {
                        Platform.exit();
                    });
                }

                Button btnSignIn = (Button) fxml.lookup("#btnSignUp");
                if(btnSignIn != null){
                    btnSignIn.setOnMouseClicked(e2->{
                        System.err.println("Sign btnSignUp");
                        imgLoadingView.setVisible(true);

                        registerAction(txtLoadingMessage ,imgLoadingView , txtFullName.getText() , txtCompany.getText() , txtEmail.getText() , txtPassword.getText());


                    });
                }
                /*Label  btnClose= (Label) fxml.getParent().lookup("btnClose");
                btnClose.setOnMouseClicked((e1) -> {
                    Platform.exit();
                });*/
            }catch(IOException ex){

            }
        });
    }
}

