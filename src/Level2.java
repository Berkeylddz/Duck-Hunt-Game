import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.util.Duration;


import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
/**
 The Level2 class represents the second level of a game. It extends the GameLevel class and implements the functionality
 specific to Level 2. It contains methods to start and end the level, handle key presses, and manage game elements such
 as crosshairs, ducks, and game sounds.
 */
public class Level2 extends GameLevel{
    private double VOLUME;
    private double SCALE;
    boolean gameOver=false;
    /**
     Constructs a Level2 object with the specified parameters.
     @param primaryStage the primary stage of the application
     @param root the root stack pane where the game is displayed
     @param SCALE the scale factor for resizing game elements
     @param VOLUME the volume level for game sounds
     */
    public Level2(Stage primaryStage, StackPane root, Integer backgorundIndex, Integer crossairIndex,double SCALE,double VOLUME) {
        super(primaryStage, root,backgorundIndex,crossairIndex);
        this.SCALE=SCALE;
        this.VOLUME = VOLUME;
    }



    /**
     Starts Level 2 by initializing game elements, setting up event handlers, and starting animation timelines.
     @param gamePageImageview the image view representing the game page background
     @param crosshairImageView the image view representing the crosshair
     */
    @Override
    public void startLevel(ImageView gamePageImageview, Integer crosshairImageView) {
        root.getChildren().clear();

        AtomicInteger ammo = new AtomicInteger(3);
        Label levelLabel = new Label("Level 2");
        Label ammoLabel = new Label("Ammo Left: " + ammo);
        VBox levelBox = new VBox(levelLabel);
        VBox ammoBox = new VBox(ammoLabel);

        ammoBox.setAlignment(Pos.TOP_RIGHT);
        ammoBox.setPadding(new Insets(10));

        levelBox.setAlignment(Pos.TOP_CENTER);
        levelBox.setPadding(new Insets(10));

        javafx.scene.image.Image crosshairImage = new Image(new File("assets/crosshair/" + crosshairImageView + ".png").toURI().toString());
        ImageView crosshairImageView1 = new ImageView(crosshairImage);

        crosshairImageView1.setFitWidth(crosshairImage.getWidth()*SCALE); // Crosshair genişliği
        crosshairImageView1.setFitHeight(crosshairImage.getHeight()*SCALE);

        VBox crosshairBox = new VBox(crosshairImageView1);
        crosshairBox.setAlignment(Pos.CENTER);

        root.setOnMouseMoved(event -> {
            crosshairBox.setTranslateX(event.getX() - crosshairBox.getLayoutBounds().getWidth() / 2);
            crosshairBox.setTranslateY(event.getY() - crosshairBox.getLayoutBounds().getHeight() / 2);
        });



        javafx.scene.image.Image duckImage1 = new Image(new File("assets/duck_black/1.png").toURI().toString());
        AtomicReference<ImageView> duckImageView = new AtomicReference<>(new ImageView(duckImage1));

        duckImageView.get().setFitHeight(duckImage1.getHeight()*SCALE);
        duckImageView.get().setFitWidth(duckImage1.getWidth()*SCALE);

        VBox duckBox = new VBox(duckImageView.get());
        duckBox.setAlignment(Pos.TOP_LEFT);
        duckBox.setPadding(new Insets(100, 0, 0, 10));

        Bounds bounds = root.getBoundsInLocal();
        double maxX = bounds.getMaxX()-50;
        double minx = bounds.getMinX();
        double miny = bounds.getMinY()-50;
        double maxY = bounds.getMaxY()-150;


        String[] duckImages = {
                "assets/duck_black/1.png",
                "assets/duck_black/2.png",
                "assets/duck_black/3.png"
        };

        int[] duckImageIndex = {0}; // İşaretçi nesnesi oluşturuldu
        final int[] speed = {50};
        final int[] speedY = {25};

        AtomicReference<Double> crosshairCenterX = new AtomicReference<>((double) 0);
        AtomicReference<Double> crosshairCenterY = new AtomicReference<>((double) 0);

        AtomicReference<Double> duckStartX = new AtomicReference<>(duckBox.getTranslateX());
        AtomicReference<Double> duckEndX = new AtomicReference<>(duckStartX.get() + duckBox.getBoundsInParent().getWidth());


        AtomicReference<Double> duckStartY = new AtomicReference<>(duckBox.getTranslateY());
        AtomicReference<Double> duckEndY = new AtomicReference<>(duckStartY.get() + duckBox.getBoundsInParent().getHeight());

        String gunShotPlayer = "assets/effects/Gunshot.mp3";
        AudioClip gunShotMusic = new AudioClip(new File(gunShotPlayer).toURI().toString());
        gunShotMusic.setVolume(VOLUME);

        String duckFallPlayer = "assets/effects/DuckFalls.mp3";
        AudioClip duckFallMusic = new AudioClip(new File(duckFallPlayer).toURI().toString());
        duckFallMusic.setVolume(VOLUME);

        String gameoverPlayer = "assets/effects/GameOver.mp3";
        AudioClip gameOvermusic = new AudioClip(new File(gameoverPlayer).toURI().toString());
        gameOvermusic.setVolume(VOLUME);

        String levelCompletedPlayer = "assets/effects/LevelCompleted.mp3";
        AudioClip levelCompletedSound = new AudioClip(new File(levelCompletedPlayer).toURI().toString());
        levelCompletedSound.setVolume(VOLUME);

        Label gameOverLabel = new Label("GAME OVER!");
        gameOverLabel.setFont(new Font(30));
        Label againLabel = new Label("Press Enter to play again");
        againLabel.setFont(new Font(30));
        Label escLabel = new Label("Press ESC to exit");
        escLabel.setFont(new Font(30));
        VBox infoBox = new VBox(gameOverLabel,againLabel,escLabel);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setVisible(false);

        Label winLabel = new Label("YOU WIN!");
        winLabel.setFont(new Font(30));
        Label enterLabel = new Label("Press ENTER to play next level");
        enterLabel.setFont(new Font(30));
        VBox winBox = new VBox(winLabel,enterLabel);
        winBox.setAlignment(Pos.CENTER);
        winBox.setVisible(false);



        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.4), event -> {

            double currentX = duckBox.getTranslateX();
            duckStartX.set(currentX);
            double a = currentX + 120;
            duckEndX.set(a);

            double currentY = duckBox.getTranslateY();
            double c = currentY+100;
            duckStartY.set(c);
            double b = c + 60;
            duckEndY.set(b);

            crosshairCenterX.set(crosshairBox.getBoundsInParent().getMinX() + crosshairBox.getBoundsInParent().getWidth() / 2);
            crosshairCenterY.set(crosshairBox.getBoundsInParent().getMinY() + crosshairBox.getBoundsInParent().getHeight() / 2);

            if(currentX >= maxX){
                speed[0] *= -1;
                duckImageView.get().setScaleY(-1);

            }

            else if(currentX < minx){
                speed[0] *= -1;
                duckImageView.get().setRotate(+270);
            }

            else if(currentY<miny){
                speedY[0] *=-1;
                duckImageView.get().setRotate(+90);
            }
            else if(currentY>maxY){
                speedY[0] *=-1;
                duckImageView.get().setRotate(+180);
            }

            duckBox.setTranslateX(currentX+ speed[0]);
            duckImageView.get().setImage(new Image(new File(duckImages[duckImageIndex[0]]).toURI().toString()));
            duckImageIndex[0] = (duckImageIndex[0] + 1) % duckImages.length;

            duckBox.setTranslateY(currentY-speedY[0]);


        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();




        root.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                gunShotMusic.play();


                if (crosshairCenterX.get() >= duckStartX.get() && crosshairCenterX.get() <= duckEndX.get()) {
                    ammo.getAndDecrement();
                    ammoLabel.setText("Ammo Left: " + ammo);
                    timeline.stop();
                    duckFallMusic.play();
                    levelCompletedSound.play();
                    gameOver=true;
                    endLevel();

                    duckImageView.get().setImage(new Image(new File("assets/duck_black/7.png").toURI().toString()));

                    Timeline timeline1 = new Timeline();
                    KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(0.5), event1 -> {
                        duckImageView.get().setImage(new Image(new File("assets/duck_black/8.png").toURI().toString()));

                        double targetY = 800;
                        TranslateTransition transition = new TranslateTransition(Duration.seconds(2), duckBox);
                        transition.setToY(targetY);
                        transition.setOnFinished(e -> {
                            // Burada düşme tamamlandığında yapılması gereken işlemleri gerçekleştirin
                            winBox.setVisible(true);
                        });
                        transition.play();
                    });
                    timeline1.getKeyFrames().add(keyFrame1);
                    timeline1.play();

                }

                else {
                    ammo.getAndDecrement();
                    ammoLabel.setText("Ammo Left: " + ammo);
                    if(ammo.get()==0){
                        gameOvermusic.play();
                        infoBox.setVisible(true);
                    }
                }
            }
        });


        root.getChildren().addAll(gamePageImageview, crosshairBox, levelBox, ammoBox, duckBox,infoBox,winBox);

    }


    /**

     Checks if the level has ended.
     @return true if the level has ended, false otherwise
     */
    @Override
    public boolean endLevel() {
        return gameOver;
    }
    /**

     Checks if the game is over.
     @return always returns false as Level 2 does not end the game
     */
    @Override
    public boolean gameOver() {
        return false;
    }
    /**

     Handles key press events.
     @param keyEvent the key event to handle
     */
    @Override
    public void handleKeyPress(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER){


        }
        else if(keyEvent.getCode() == KeyCode.ESCAPE){
            ;
        }
    }
}
