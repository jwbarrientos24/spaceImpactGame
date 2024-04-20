package miniprojtemplate;

import javafx.scene.image.Image;
import java.util.Random;

public class Ufo extends Sprite {
	public static final int MAX_UFO_SPEED = 5;
	public final static Image UFO_IMAGE = new Image("images/ufo.png",Ufo.UFO_WIDTH,Ufo.UFO_WIDTH,false,false);
	public final static int UFO_WIDTH=50;
	private boolean alive;
	//attribute that will determine if a fish will initially move to the right
	private boolean moveRight;
	private int speed;


	Ufo(int x, int y){
		super(x,y);
		this.alive = true;
		this.loadImage(Ufo.UFO_IMAGE);
		Random r = new Random();

		// randomizing the speed of the enemies and whether they'll move left or right
		this.speed = r.nextInt(MAX_UFO_SPEED-1) +1;
		this.moveRight = r.nextBoolean();

	}

	//method that changes the x position of the fish
	void move(){
		if (this.moveRight == true && this.x < GameStage.WINDOW_WIDTH){
			this.setDX(this.speed);
			this.x += this.dx;
		} else if (this.x >= GameStage.WINDOW_WIDTH) {
			this.x = GameStage.WINDOW_WIDTH;
			this.moveRight = false;
		}

		if (this.moveRight == false && this.x > 0){
			this.setDX(-this.speed);
			this.x += this.dx;
		} else if (this.x <= 0) {
			this.x = 0;
			this.moveRight = true;
		}
	}

	//getter for the alive status of the ufo
	public boolean isAlive() {
		return this.alive;
	}

	//setter of the alive status of the ufo
	public void setAlive(boolean a){
		this.alive = a;
	}
}
