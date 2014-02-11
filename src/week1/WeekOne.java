package week1;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import processing.core.PApplet;
import processing.core.PVector;
import rwmidi.MidiInput;
import rwmidi.MidiOutput;
import rwmidi.RWMidi;

public class WeekOne extends PApplet {
	
	int y_min = 347;		// "top left" of the fallboard
	int radius = 7;
	
	public MidiInput input;
	public MidiOutput output;
	
	Ball[] balls = new Ball[21];
	
	public void setup() {
		size(1024, 535 + y_min, P3D);
		input = RWMidi.getInputDevices()[0].createInput(this);
		output = RWMidi.getOutputDevices()[0].createOutput();
		
		initBalls();
	}

	public void draw() {
		background(0);
		
		for (int i = 0; i < balls.length; i++) {
			Ball b = balls[i];
			b.update(this);
			b.display(this);
		}
	}

	public void initBalls() {
		for (int i = 0; i < balls.length; i++) {
			float ballX = random(99, 787);
//			Ball b = new Ball(new PVector(ballX, Ball.y_min + Ball.getOffsetY(ballX)),
			Ball b = new Ball(new PVector(500, 351),
							new PVector(random(-3.0f, 3.0f), random(0, 4.0f)), this);
//			Ball b = new Ball(new PVector(500, 351), new PVector(random(-1f, 1f), random(1f, 2f)), this);
			balls[i] = b;
		}
	}
	
	public void mousePressed() {
		System.out.println(mouseX + ", " + mouseY);
		//initBalls();
		balls = new Ball[0];
		background(0);
	}
	
	public void keyPressed() {
		if (key == '1') 
			balls = new Ball[1];
		else if (key == '2')
			balls = new Ball[2];
		else if (key == '3')
			balls = new Ball[3];
		else if (key == '4')
			balls = new Ball[20];
		initBalls();
	}
	
	public static void main(String[] args) {
		/* 
		 * places window on second screen automatically if there's additional display
		 * 
		 */
		int primary_width;
		int screen_y = 0;
		
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice devices[] = environment.getScreenDevices();
		String location;
		if (devices.length > 1) {
			primary_width = devices[0].getDisplayMode().getWidth();
			location = "--location=" +primary_width+ "," + screen_y;
		} else {
			location="--location=0,0";
		}
	    PApplet.main(new String[] { location, WeekOne.class.getName() });
	}
}
