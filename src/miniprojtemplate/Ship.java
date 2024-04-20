package miniprojtemplate;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.image.Image;



public class Ship extends Sprite{
	private String name;
	private int strength;
	private boolean alive;

	private ArrayList<Bullet> bullets;
	public final static Image SHIP_IMAGE = new Image("images/spaceship.png",Ship.SHIP_WIDTH, Ship.SHIP_HEIGHT,true,false);
	private final static int SHIP_WIDTH = 60;
	private final static int SHIP_HEIGHT = 81;

	public Ship(String name, int x, int y){
		super(x,y);
		this.name = name;
		Random r = new Random();
		this.alive = true;
		this.bullets = new ArrayList<Bullet>();
		this.loadImage(Ship.SHIP_IMAGE);
		this.strength = 100 + r.nextInt(50);

	}

	public boolean isAlive(){
		if(this.alive) return true;
		return false;
	}

	public String getName(){
		return this.name;
	}

	public void die(){
    	this.alive = false;
    }

	//method that will get the bullets 'shot' by the ship
	public ArrayList<Bullet> getBullets(){
		return this.bullets;
	}

	//method called if spacebar is pressed
	public void shoot(){
		//compute for the x and y initial position of the bullet
		int x = (int) (this.x + SHIP_WIDTH);
		int y = (int) (this.y + SHIP_HEIGHT/8);
		Bullet b = new Bullet(x,y);
		this.bullets.add(b);
		/*
		 * TODO: Instantiate a new bullet and add it to the bullets arraylist of ship
		 */
    }

	//method called if up/down/left/right arrow key is pressed.
	public void move() {
		if (this.x >= 0 && this.x <= GameStage.WINDOW_WIDTH - (int) this.width && this.y >= 0 && this.y <= GameStage.WINDOW_HEIGHT - (int) this.height){
			this.x += this.dx;
			this.y += this.dy;
		} else if (this.x < 0) {
			this.x = 0;
		} else if (this.x > GameStage.WINDOW_WIDTH - (int) this.width) {
			this.x = GameStage.WINDOW_WIDTH - (int) this.width;
		} else if (this.y < 0) {
			this.y = 0;
		} else if (this.y > GameStage.WINDOW_HEIGHT - (int) this.height) {
			this.y = GameStage.WINDOW_HEIGHT - (int) this.height;
		}
	}


	//setter for the ship's strength which is used when getting the heart powerup
	public void setStrength(int x){
		this.strength += x;
	}

	// getter for the ship's strength which is used to see if it;s still alive
	public int getStrength(){
		return this.strength;
	}

	//adding the bullets to an array
	public void setBullets(ArrayList<Bullet> bullets){
		this.bullets =bullets;
	}

}
