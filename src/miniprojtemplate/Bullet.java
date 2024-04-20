package miniprojtemplate;

import javafx.scene.image.Image;

// Bullet Class
public class Bullet extends Sprite {
	private final int BULLET_SPEED = 20;
	public final static Image BULLET_IMAGE = new Image("images/rocket.png",Bullet.BULLET_WIDTH,Bullet.BULLET_WIDTH,false,false);
	public final static int BULLET_WIDTH = 20;

	public Bullet(int x, int y){
		super(x,y);
		this.loadImage(Bullet.BULLET_IMAGE);
	}


	//method that will move/change the x position of the bullet
	public void move(){
		this.setVisible(true);

		// if the bullet is still in the screen, it will move and be visible, otherwise, it will not be visible
		if (this.getX() < GameStage.WINDOW_WIDTH - this.width){
			this.setDX(BULLET_SPEED);
			this.x += this.dx;
		} else if (this.getX() >= GameStage.WINDOW_WIDTH - this.width){
			this.setVisible(false);
		}

	}
}