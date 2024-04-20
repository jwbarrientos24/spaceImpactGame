package miniprojtemplate;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;


public class GameStage {
	public static final int WINDOW_HEIGHT = 450;
	public static final int WINDOW_WIDTH = 800;
	private Scene scene;
	private Stage stage;
	private Group root;
	private Canvas canvas;
	private GraphicsContext gc;
	private GameTimer gametimer;
	public final Image bg = new Image("images/bg.jpg",800,450,false,false);


	//the class constructor
	public GameStage() {
		this.root = new Group();
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,Color.MIDNIGHTBLUE);
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();
		//instantiate an animation timer
		this.gametimer = new GameTimer(this.gc,this.scene);



	}

	//method to add the stage elements
	public void setStage(Stage stage) {

		this.stage = stage;

		//set stage elements here
		this.root.getChildren().add(canvas);

		this.stage.setTitle("Mini Ship Shooting Game");
		this.stage.setScene(this.scene);

		//invoke the start method of the animation timer
		this.gametimer.start();
		this.stage.show();
	}

}

