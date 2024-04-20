package miniprojtemplate;

import javafx.scene.image.Image;


// Star Class
public class Star extends Sprite{
	public final static Image STAR_IMAGE = new Image("images/star.png",Star.STAR_WIDTH,Star.STAR_WIDTH,false,false);
	public final static int STAR_WIDTH=40;


	Star(int x, int y){
		super(x,y);
		this.loadImage(Star.STAR_IMAGE);

	}
}
