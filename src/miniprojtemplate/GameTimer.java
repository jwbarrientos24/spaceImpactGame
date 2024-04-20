package miniprojtemplate;

import java.util.ArrayList;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/*
 * The GameTimer is a subclass of the AnimationTimer class. It must override the handle method.
 */

public class GameTimer extends AnimationTimer{

	private GraphicsContext gc;
	private Scene theScene;
	private Ship myShip;
	private ArrayList<Ufo> ufoss;
	public static final int MAX_NUM_UFOES = 7;
	protected int Score = 0;
	private long tbefore = System.currentTimeMillis();
	private long t;
	private Star star;
	private Heart heart;
	private boolean immune = false;
	private int timer = 0;

	private int secondsPassed = 60;
	private Boss boss;

	GameTimer(GraphicsContext gc, Scene theScene){
		this.gc = gc;
		this.theScene = theScene;

		this.myShip = new Ship("Going merry",100,100);
		//instantiate the ArrayList of Ufo
		this.ufoss = new ArrayList<Ufo>();

		//call the spawnUfoes method
		this.spawnUfoes();
		//call method to handle mouse click event
		this.handleKeyPressEvent();
	}

	@Override
	public void handle(long currentNanoTime) {
		this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);

		//immunity timer
		if (this.immune == false){
			this.timer = 0;
		}

		// Status bar
		Font theFont = Font.font("Agency FB", FontWeight.NORMAL, 24);
		this.gc.setFill(Color.WHITE);
		this.gc.setFont(theFont);
		this.gc.fillText("Score: " + this.Score, 50, 50);
		this.gc.fillText("Strength: " + this.myShip.getStrength(), 150, 50);
		this.gc.fillText("Time Left: " + this.secondsPassed +"s", 275, 50);
		this.gc.fillText("Immunity: " + this.timer + "s", 410, 50);

		this.myShip.move();

		//moves the bullets and enemies
		this.moveBullets();
		this.moveUfoes();


		//render the ship
		if (this.myShip.isAlive()){
			this.myShip.render(this.gc);
		}

		// render the enemies and your bullets
		this.renderUfoes();
		this.renderBullets();

		//method called to check if the enemy is dead
		this.checkDeath();

		//if the ship is not immune and is still alive, will use the method setHealth to check if it collided with any enemy and reduce the strength
		if (this.myShip.getStrength() > 0 && this.immune == false){
			this.setHealth();

		// if the ship strength drops below 0, Lose alert will pop
		} else if (this.myShip.getStrength() <= 0) {
			Alert a = new Alert(AlertType.NONE,"You Lose",ButtonType.CLOSE);
			a.setContentText("You Lose");
			a.show();
			this.stop();
		}

		//timer
		t = System.currentTimeMillis();
		//t is the time elapsed
		if (t-tbefore >=1000){
			tbefore = t;
			secondsPassed -=1;

			//checks if 5 seconds have passed, if so, it will spawn an three enemies and despawn the powerups that were not gotten
			if (secondsPassed%5==0){
				spawnThreeUfoes();
				if (this.star != null){
					this.star = null;
				}
				if (this.heart != null){
					this.heart = null;
				}
			}

			//checks if 10 seconds have passed, if so, spawn a powerup
			if (secondsPassed %10 == 0 && secondsPassed !=0){
				spawnPowerUp();
			}

			//if timer reaches zero, the win condition is met and an alert will pop
			if (secondsPassed == 0){
				String al1 = "Score: " +this.Score;
				Alert b = new Alert(AlertType.NONE,"You Win",ButtonType.CLOSE);
				b.setContentText("You Win!\n" + al1);
				b.show();
				this.stop();
			}

			//after 30 seconds, spawn the boss
			if (secondsPassed == 30){
				spawnBoss();
			}

			// each second the immunity remains true, it's duration is reduced
			if (this.immune==true){
				this.timer -= 1;
			}
		}

		// if there is a star powerup, render it
		if (this.star !=null){
			this.star.render(this.gc);
		}

		// if there is a heart powerup, render it
		if (this.heart !=null){
			this.heart.render(this.gc);
		}

		// if the immunity timer is 0, then the ship isn't immune
		if (this.timer == 0){
			this.immune = false;
		}

		//check if the ship collides with any powerup
		this.PowerCollision();

	}

	//method that will render/draw the ufoss to the canvas
	private void renderUfoes() {
		for (Ufo f : this.ufoss){
			if (f.isAlive()){
				f.render(this.gc);
			}
		}
		if (this.boss!=null){
			this.boss.render(this.gc);
		}
	}

	//method that will render/draw the bullets to the canvas
	private void renderBullets() {
		ArrayList<Bullet> bList = this.myShip.getBullets();
		for(int i = 0; i < bList.size(); i++){
			Bullet b = bList.get(i);
			if (b.isVisible()){
				b.render(this.gc);
			} else if (b.isVisible() == false){
				bList.remove(i);
			}

		}

	}

	//method that will spawn the powerups on the left side of the screen
	private void spawnPowerUp(){
		Random r = new Random();
		int x = r.nextInt(GameStage.WINDOW_WIDTH/2);
		int y = r.nextInt(GameStage.WINDOW_HEIGHT - Star.STAR_WIDTH);
		Star s = new Star(x,y);
		this.star = s;

		int z = r.nextInt(GameStage.WINDOW_WIDTH/2);
		int w = r.nextInt(GameStage.WINDOW_HEIGHT - Star.STAR_WIDTH);
		Heart h = new Heart(z,w);
		this.heart = h;
	}

	//method that will spawn/instantiate three ufos at a random x,y location
	private void spawnUfoes(){
		Random r = new Random();
		for(int i=0;i<GameTimer.MAX_NUM_UFOES;i++){
			int x = r.nextInt(GameStage.WINDOW_WIDTH/2) + GameStage.WINDOW_WIDTH/2;
			int y = r.nextInt(GameStage.WINDOW_HEIGHT - Ufo.UFO_WIDTH);
			Ufo f = new Ufo(x,y);
			this.ufoss.add(f);
		}
	}

	//method that will spawn the boss
	private void spawnBoss(){
		Random r = new Random();
		int x = r.nextInt(GameStage.WINDOW_WIDTH/2) + GameStage.WINDOW_WIDTH/2;
		int y = r.nextInt(GameStage.WINDOW_HEIGHT - Boss.UFO_WIDTH);
		Boss f = new Boss(x,y);

		this.boss = f;
		}

	//method that will spawn three fish
	private void spawnThreeUfoes(){
		Random r = new Random();
		for(int i=0;i<3;i++){
			int x = r.nextInt(GameStage.WINDOW_WIDTH/2) + GameStage.WINDOW_WIDTH/2;
			int y = r.nextInt(GameStage.WINDOW_HEIGHT- Ufo.UFO_WIDTH);
			Ufo f = new Ufo(x,y);
			this.ufoss.add(f);
		}
	}

	//method that will move the bullets shot by a ship
	private void moveBullets(){
		//create a local arraylist of Bullets for the bullets 'shot' by the ship
		ArrayList<Bullet> bList = this.myShip.getBullets();

		//Loop through the bullet list and check whether a bullet is still visible.
		for(int i = 0; i < bList.size(); i++){
			Bullet b = bList.get(i);
			if (b.isVisible()){
				b.move();
			} else if (b.isVisible() == false){
				bList.remove(i);
				this.myShip.setBullets(bList);
			}
		}
	}

	//method that will move the ufoss
	private void moveUfoes(){
		//Loop through the ufoss arraylist
		for(int i = 0; i < this.ufoss.size(); i++){
			Ufo f = this.ufoss.get(i);
			if (f.isAlive()){
				f.move();

			//if ufo is dead, then remove it
			} else if (f.isAlive()==false){
				f.setVisible(false);
				this.ufoss.remove(i);
			}
		}

		// if there is a boss, move it
		if (this.boss != null){
			boss.move();
		}
	}


	//method that will listen and handle the key press events
	private void handleKeyPressEvent() {
		this.theScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent e){
            	KeyCode code = e.getCode();
                moveMyShip(code);
			}
		});

		this.theScene.setOnKeyReleased(new EventHandler<KeyEvent>(){
		            public void handle(KeyEvent e){
		            	KeyCode code = e.getCode();
		                stopMyShip(code);
		            }
		        });
    }

	//method that will move the ship depending on the key pressed
	private void moveMyShip(KeyCode ke) {
		if(ke==KeyCode.UP) this.myShip.setDY(-10);

		if(ke==KeyCode.LEFT) this.myShip.setDX(-10);

		if(ke==KeyCode.DOWN) this.myShip.setDY(10);

		if(ke==KeyCode.RIGHT) this.myShip.setDX(10);

		if(ke==KeyCode.SPACE) this.myShip.shoot();

		System.out.println(ke+" key pressed.");
   	}

	//method that will stop the ship's movement; set the ship's DX and DY to 0
	private void stopMyShip(KeyCode ke){
		this.myShip.setDX(0);
		this.myShip.setDY(0);
	}

	// method to check if any of the enemies collide with the bullet
	// if an ordinary ufo is hit, it dies and the score is increased
	// if the boss is hit, it's health is reduced by the amount of the ship's strength
	private void checkDeath(){
		ArrayList<Bullet> bList = this.myShip.getBullets();
		for(int i = 0; i < bList.size(); i++){
			for (int j =0; j< this.ufoss.size(); j++){
				if (bList.get(i).collidesWith(this.ufoss.get(j))){
					bList.get(i).setVisible(false);
					Ufo f = this.ufoss.get(j);
					f.setAlive(false);
					f.setVisible(false);
					this.ufoss.remove(j);
					this.Score++;
				}
			}

			if (this.boss !=null){
				if (bList.get(i).collidesWith(this.boss)){
					bList.get(i).setVisible(false);
					this.boss.setHealth(-this.myShip.getStrength());
				}
				if (this.boss.getHealth()<=0){
					this.boss.setAlive(false);
					this.boss.setVisible(false);
					this.boss = null;
					this.Score += 10;
				}
			}
		}
	}

	//method to check if  any of the enemies collide with the ship, if so, reduce ship health
	private void setHealth(){
		Random r = new Random();
		for (int j =0; j< this.ufoss.size(); j++){
			if (this.myShip.collidesWith(this.ufoss.get(j))){
				this.myShip.x = 100;
				this.myShip.y = r.nextInt(GameStage.WINDOW_HEIGHT-100) + 100;
				this.myShip.setStrength(-30);
				this.timer = 2;
				this.immune = true;
			}
		}
		if (this.boss!=null){
			if (this.myShip.collidesWith(this.boss)){
				this.myShip.x = 100;
				this.myShip.y = r.nextInt(GameStage.WINDOW_HEIGHT-100) + 100;
				this.myShip.setStrength(-50);
				this.timer = 2;
				this.immune = true;
			}
		}

		//if ship strength is below 0, it dies
		if (this.myShip.getStrength() <= 0){
			this.myShip.die();
		}
	}

	// method to check if any of the powerups were gotten
	private void PowerCollision(){
		if (this.star != null){
			if (this.myShip.collidesWith(this.star)){
				this.timer = 3;
				this.immune = true;
				this.star = null;
			}
		}
		if (this.heart != null){
			if (this.myShip.collidesWith(this.heart)){
				this.myShip.setStrength(50);
				this.heart = null;
			}
		}
	}

	//ship getter
	public Ship getShip(){
		return this.myShip;
	}

}
