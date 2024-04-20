package miniprojtemplate;

import javafx.scene.image.Image;

// Heart powerup class that increases the strength
public class Heart extends Sprite{
	public final static Image HEART_IMAGE = new Image("images/heart.png",Heart.HEART_WIDTH,Heart.HEART_WIDTH,false,false);
	public final static int HEART_WIDTH=30;


	Heart(int x, int y){
		super(x,y);
		this.loadImage(Heart.HEART_IMAGE);

	}
}
