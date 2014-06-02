package week1;

import processing.core.PApplet;
import processing.core.PVector;

public class Projection {
	
	PApplet processing;
	
	static int left_wall = 66;
	static int right_wall = 820;
	static int x_min = 98;
	static int x_max = 782;
	
	static int y_min_left = 347;
	static int y_max_left = 464;
	static int y_min_right = 359;
	static int y_max_right = 482;
	static int y_max = (y_max_left + y_max_right)/2;
	
	static double key_width = 7.76;
	
	static PVector TL = new PVector(x_min, y_min_left);	//top left corner
	static PVector BL = new PVector(x_min, y_max_left);  //bottom left corner
	static PVector TR = new PVector(x_max, y_min_right);	//top right corner
	static PVector BR = new PVector(x_max, y_max_right);	//bottom right corner

		
	public Projection(PApplet p) {
		this.processing = p;
	}
	
	public void drawOutline() {
		processing.line(TL.x, TL.y, BL.x, BL.y); //left
		processing.line(TL.x, TL.y, TR.x, TR.y); //top
		processing.line(TR.x, TR.y, BR.x, BR.y); //right
		processing.line(BR.x, BR.y, BL.x, BL.y); //bottom
	}
	
	// based on the x location of the ball, find the MIDI value of note it should hit
	public static int getMidiFromX(double x) {
		return (int) (21 + (x - x_min)/key_width);
	}
	
	// Y of the key when it's pressed
	public static int Ymax(double x) {
		return (int) (y_max + getOffsetY(x));
	}
	
	// taking into account distortion of the projector
	public static int getOffsetY(double x) {					
		return (int) ((x - x_min)/(x_max-x_min) * (y_max_right-y_max_left));
	}

}
