import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public abstract class GameLevel {
    protected Stage primaryStage;
    protected StackPane root;
    protected ImageView backgroundImageView;
    protected ImageView crosshairImageView;
    protected VBox messageBox;
    protected int backgroundIndex = 1;
    protected int crosshairIndex = 1;

    public GameLevel(Stage primaryStage, StackPane root, Integer backgroundIndex, Integer crosshairIndex) {
        this.primaryStage = primaryStage;
        this.root = root;
        this.backgroundIndex = backgroundIndex;
        this.crosshairIndex=crosshairIndex;

        createBackground();
        createCrosshair();
        createMessageBox();
    }

    public abstract void startLevel(ImageView gamePageImageview, Integer crosshairImageView);

    public abstract boolean endLevel();

    public abstract boolean gameOver();

    public abstract void handleKeyPress(KeyEvent event);



    protected void createBackground() {
        Image backgroundImage = new Image(new File("/Users/berke/IdeaProjects/Assignment4/src/assets-2/background/" + backgroundIndex + ".png").toURI().toString());
        backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.fitWidthProperty().bind(root.widthProperty());
        backgroundImageView.fitHeightProperty().bind(root.heightProperty());

    }

    protected void createCrosshair() {
        Image crosshairImage = new Image(new File("/Users/berke/IdeaProjects/Assignment4/src/assets-2/crosshair/" + crosshairIndex + ".png").toURI().toString());
        crosshairImageView = new ImageView(crosshairImage);
        crosshairImageView.setX(primaryStage.getWidth() / 2 - crosshairImage.getWidth() / 2);
        crosshairImageView.setY(primaryStage.getHeight() / 2 - crosshairImage.getHeight() / 2);
    }

    protected void createMessageBox() {
        messageBox = new VBox(10);
        messageBox.setAlignment(Pos.TOP_CENTER);
        messageBox.setPadding(new Insets(75, 0, 0, 50));
    }

}
