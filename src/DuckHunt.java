import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 The DuckHunt class represents a JavaFX application for the game Duck Hunt.
 It extends the Application class and provides the start() method to initialize and start the game.
 */
public class DuckHunt extends Application {
    private static final String TITLE_SCREEN_MUSIC = "assets/effects/Title.mp3";
    private static final String FAVICON_PATH = "assets/favicon/1.png";
    private static final String GAME_TITLE = "HUBBM Duck Hunt";
    private static final String NAVIGATION_MESSAGE = "USE ARROW KEYS TO NAVIGATE";
    private static final String START_MESSAGE = "PRESS ENTER TO START";
    private static final String EXIT_MESSAGE = "PRESS ESC TO EXIT";

    private static final Duration MESSAGE_DURATION = Duration.seconds(3);

    private GameLevel currentLevel;

    private Stage primaryStage;
    private StackPane root;
    private ImageView backgroundImageView;
    private VBox messageBox;
    private Label navigationLabel;
    private Label startLabel;
    private Label exitLabel;
    private boolean gameStarted;
    private int backgroundIndex = 1;
    private int crosshairIndex = 1;
    private AudioClip titleMusic=null;
    int level = 1;
    private double VOLUME = 0.025;
    private double SCALE = 3;

    /**
     The main entry point for the JavaFX application.
     It initializes the primary stage, sets up the title screen music, favicon, and the root scene.
     It starts the game by creating the initial level and showing the messages.
     @param primaryStage the primary stage for the application
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        titleMusic = new AudioClip(new File(TITLE_SCREEN_MUSIC).toURI().toString());
        titleMusic.setCycleCount(AudioClip.INDEFINITE);
        titleMusic.setVolume(VOLUME);
        titleMusic.play();

        navigationLabel = new Label(NAVIGATION_MESSAGE);
        navigationLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white");

        startLabel = new Label(START_MESSAGE);
        startLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: white");

        exitLabel = new Label(EXIT_MESSAGE);
        exitLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: white");

        messageBox = new VBox(10);
        messageBox.setAlignment(Pos.BOTTOM_CENTER);
        messageBox.getChildren().addAll(startLabel, exitLabel);
        messageBox.setPadding(new Insets(0, 0, 150, 0));

        try {
            BufferedImage bufferedImage = ImageIO.read(new File(FAVICON_PATH));
            Image icon = SwingFXUtils.toFXImage(bufferedImage, null);
            primaryStage.getIcons().add(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        root = new StackPane();

        Image backgroundImage = new Image(new File(FAVICON_PATH).toURI().toString());
        backgroundImageView = new ImageView(backgroundImage);



        root.getChildren().add(backgroundImageView);

        backgroundImageView.setFitHeight(backgroundImage.getHeight()*SCALE);
        backgroundImageView.setFitWidth(backgroundImage.getWidth()*SCALE);

        root.getChildren().add(messageBox);

        Scene scene = new Scene(root, backgroundImage.getWidth()*SCALE, backgroundImage.getHeight()*SCALE);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPress);



        EventHandler<MouseEvent> mouseExitedHandler = event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();
            scene.setCursor(Cursor.DEFAULT);

            if (mouseX < 0 || mouseX >= scene.getWidth() || mouseY < 0 || mouseY >= scene.getHeight()) {
                scene.setCursor(Cursor.NONE);
            } else {
                scene.setCursor(Cursor.DEFAULT);
            }
        };

        primaryStage.setScene(scene);
        primaryStage.setTitle(GAME_TITLE);


        primaryStage.show();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> showMessages()),
                new KeyFrame(MESSAGE_DURATION)
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        currentLevel = new Level1(primaryStage,root,backgroundIndex,crosshairIndex,SCALE,VOLUME);



    }

    /**
     This method displays the messages in the message box.
     It sets the message box to be visible and creates a timeline animation to hide the message box after a certain duration.
     The duration is defined by the MESSAGE_DURATION constant.
     */

    private void showMessages() {
        messageBox.setVisible(true);
        Timeline hideTimeline = new Timeline(
                new KeyFrame(Duration.seconds(2), e -> messageBox.setVisible(false))
        );
        hideTimeline.play();
    }


    private ImageView gamePageImageView = null;

    private boolean selectMode = false; // Seçim modu başlangıçta false

    /**
     This method handles the key press events for the game. It checks for various conditions and performs
     different actions based on the key pressed and the current state of the game.
     @param event the KeyEvent object representing the key press event
     */
    private void handleKeyPress(KeyEvent event) {

        if(currentLevel.endLevel()==true){
            level++;
        }

        else if(currentLevel.gameOver()==true && event.getCode() == KeyCode.ENTER){
            level=1;
        }


        if(gameStarted && event.getCode() == KeyCode.ENTER && level==1){
            currentLevel = new Level1(primaryStage,root,backgroundIndex,crosshairIndex,SCALE,VOLUME);
            currentLevel.handleKeyPress(event);
            currentLevel.startLevel(gamePageImageView,crosshairIndex);
        }

        else if(gameStarted && event.getCode() == KeyCode.ENTER && level==2 ){

            currentLevel = new Level2(primaryStage,root,backgroundIndex,crosshairIndex,SCALE,VOLUME);
            currentLevel.handleKeyPress(event);
            currentLevel.startLevel(gamePageImageView,crosshairIndex);
        }
        else if(gameStarted && event.getCode() == KeyCode.ENTER && level==3 ){

            currentLevel = new Level3(primaryStage,root,backgroundIndex,crosshairIndex,SCALE,VOLUME);
            currentLevel.handleKeyPress(event);
            currentLevel.startLevel(gamePageImageView,crosshairIndex);
        }
        else if(gameStarted && event.getCode() == KeyCode.ENTER && level==4 ){
            currentLevel = new Level4(primaryStage,root,backgroundIndex,crosshairIndex,SCALE,VOLUME);
            currentLevel.handleKeyPress(event);
            currentLevel.startLevel(gamePageImageView,crosshairIndex);
        }
        else if(gameStarted && event.getCode() == KeyCode.ENTER && level==5 ){
            currentLevel = new Level5(primaryStage,root,backgroundIndex,crosshairIndex,SCALE,VOLUME);
            currentLevel.handleKeyPress(event);
            currentLevel.startLevel(gamePageImageView,crosshairIndex);
        }
        else if(gameStarted && event.getCode() == KeyCode.ENTER && level==6 ){
            currentLevel = new Level6(primaryStage,root,backgroundIndex,crosshairIndex,SCALE,VOLUME);
            currentLevel.handleKeyPress(event);
            currentLevel.startLevel(gamePageImageView,crosshairIndex);
        }



        else {
            if(event.getCode() == KeyCode.ENTER){
                if(!selectMode){
                    selectMode=true;
                    root.getChildren().clear();

                    Image gamePageImage = new Image(new File("assets/background/" + backgroundIndex + ".png").toURI().toString());
                    gamePageImageView = new ImageView(gamePageImage);

                    root.getChildren().add(gamePageImageView);

                    gamePageImageView.fitWidthProperty().bind(root.widthProperty());
                    gamePageImageView.fitHeightProperty().bind(root.heightProperty());

                    Image crosshairImage = new Image(new File("assets/crosshair/" + crosshairIndex + ".png").toURI().toString());
                    ImageView crosshairImageView = new ImageView(crosshairImage);
                    crosshairImageView.setFitWidth(crosshairImage.getWidth()*SCALE); // Crosshair genişliği
                    crosshairImageView.setFitHeight(crosshairImage.getHeight()*SCALE);
                    crosshairImageView.setX(primaryStage.getWidth() / 2 - crosshairImage.getWidth() / 2);
                    crosshairImageView.setY(primaryStage.getHeight() / 2 - crosshairImage.getHeight() / 2);

                    root.getChildren().add(crosshairImageView);

                    messageBox.getChildren().clear();
                    Label navigationLabel = new Label("USE ARROW KEYS TO NAVIGATE");
                    navigationLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white");

                    Label startLabel = new Label("PRESS ENTER TO START");
                    startLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: white");

                    Label exitLabel = new Label("PRESS ESC TO EXIT");
                    exitLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: white");

                    messageBox = new VBox(10); // 10 is the spacing between labels
                    messageBox.setAlignment(Pos.TOP_CENTER);
                    messageBox.getChildren().addAll(navigationLabel, startLabel,exitLabel);
                    messageBox.setPadding(new Insets(75, 0, 0, 50));

                    root.getChildren().addAll(messageBox);
                }else {
                    gameStarted=true;
                    titleMusic.stop();

                    String introMusicPath = "assets/effects/Intro.mp3";
                    AudioClip introMusic = new AudioClip(new File(introMusicPath).toURI().toString());
                    MediaPlayer introMusicPlayer;

                    Media introMusicMedia = new Media(new File(introMusicPath).toURI().toString());
                    introMusicPlayer = new MediaPlayer(introMusicMedia);
                    introMusicPlayer.setVolume(VOLUME);
                    introMusicPlayer.setOnEndOfMedia(() -> {
                        introMusicPlayer.stop();  // Müzik bittiğinde müziği durdurun
                        gameStarted = true;  // Oyunu başlatın
                        currentLevel.startLevel(gamePageImageView,crosshairIndex);

                    });

                    introMusicPlayer.play();

                }

            }
            else if (event.getCode() == KeyCode.ESCAPE) {
                if(gameStarted){
                    primaryStage.close();
                }



            }
            else if(selectMode){
                if (event.getCode() == KeyCode.RIGHT) {
                    if (backgroundIndex >= 6) {
                        backgroundIndex -= 3;
                    }
                    backgroundIndex += 1;

                    Image gamePageImage = new Image(new File("assets/background/" + backgroundIndex + ".png").toURI().toString());
                    gamePageImageView.setImage(gamePageImage);
                } else if (event.getCode() == KeyCode.LEFT) {
                    if (backgroundIndex <= 1) {
                        backgroundIndex += 3;
                    }
                    backgroundIndex -= 1;
                    Image gamePageImage = new Image(new File("assets/background/" + backgroundIndex + ".png").toURI().toString());
                    gamePageImageView.setImage(gamePageImage);
                } else if (event.getCode() == KeyCode.UP) {
                    if (crosshairIndex >= 7) {
                        crosshairIndex -= 6;
                    }
                    crosshairIndex += 1;
                    Image crosshairImage = new Image(new File("assets/crosshair/" + crosshairIndex + ".png").toURI().toString());
                    ImageView crosshairImageView = (ImageView) root.getChildren().get(1);
                    crosshairImageView.setImage(crosshairImage);
                } else if (event.getCode() == KeyCode.DOWN) {
                    crosshairIndex -= 1;
                    if (crosshairIndex <= 0) {
                        crosshairIndex += 6;
                    }
                    Image crosshairImage = new Image(new File("assets/crosshair/" + crosshairIndex + ".png").toURI().toString());
                    ImageView crosshairImageView = (ImageView) root.getChildren().get(1);
                    crosshairImageView.setImage(crosshairImage);
                }
            }

        }





    }

    public static void main(String[] args) {
        launch(args);
    }
}
