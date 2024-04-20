package miniprojtemplate;

import javafx.scene.image.Image;

// Boss Class
public class Boss extends Ufo {
	public static final int MAX_UFO_SPEED = 5;
	public final static Image UFO_IMAGE = new Image("images/ufo.png",Boss.UFO_WIDTH,Boss.UFO_WIDTH,false,false);
	public final static int UFO_WIDTH=100;	//Boss is bigger than the other enemies
	private int health;

// The Boss has 3000 health
	Boss(int x, int y){
		super(x,y);
		this.loadImage(Boss.UFO_IMAGE);
		this.health = 3000;

	}

// setter for the boss health
	public void setHealth(int x){
		this.health += x;
	}

// getter for the boss health
	int getHealth(){
		 return this.health;
	}

}
