package week1;

import processing.core.PApplet;
import processing.core.PVector;

public class Ball {
	
	// Kinematics stuff
	PVector pos;
	PVector vel;
	
	int r = 7;
	int t0;				// initial time for each bounce cycle
	int time_til_note;
	double note_velocity; // used to determine midi strength
	
	static double gravity = .1;
	static double epsilon = 0.00000001;
	
	// MIDI
	int prevNote = 0;
	int nextNote = 1;
	boolean playedNote = false;
	
	public Ball(PVector pos, PVector vel, WeekOne parent) {
		this.pos = pos;
		this.vel = vel;
		note_velocity = Math.sqrt(2*gravity*(parent.proj.y_max-pos.y));
		
		this.t0 = parent.millis();
		
		nextNote = parent.proj.getMidiFromX(getNextBounceX(pos, vel, true, parent.proj.y_max)); //discount the initial time
	}
	
	public void update(WeekOne parent) {
		if (vel.mag() < epsilon)	// if velocity is pretty much zero, don't do anything
			return;
		
		vel.add(new PVector(0, (float) gravity));
		pos.add(vel);
		
		// MIDI handled separately from drawing because of delay
		if ((parent.millis() - t0) > time_til_note - 650 && !playedNote && inRange(nextNote)) {
			
			if (note_velocity > 1)
				parent.output.sendNoteOn(0, nextNote, (int) (note_velocity * 12.0));
			playedNote = true;
		}
		if ((parent.millis() - t0 > time_til_note - 560) && inRange(nextNote)) {
			parent.output.sendNoteOff(0, nextNote, 50);
		}
		
		// check if we're at the bottom
		int maxY = parent.proj.y_max + parent.proj.getOffsetY(pos.x) - r;	
		if (pos.y > maxY-r) {	
			vel.y *= -.8f;							// take away some velocity 
			vel.x *= .8f;
			note_velocity = -vel.y;

			prevNote = nextNote;					// since we're starting a new bounce cycle 
			nextNote = parent.proj.getMidiFromX(getNextBounceX(pos, vel, false, parent.proj.y_max)); // figure out the next note


			t0 = parent.millis();					// and reset t0 for new bounce cycle
			playedNote = false;

			pos.y = maxY-r;	// hack to make sure ball doesn't fall through!	
		}
		
		// make them bounce off sides
		if (pos.x < parent.proj.left_wall || pos.x > parent.proj.right_wall) {
			vel.x *= -.95f;
		}
	}
	
	private boolean inRange(int note) {
		return (note > 20) && (note < 109);
	}
	
	public double getNextBounceX(PVector pos, PVector vel, boolean init, int y_max) {
		double t = 0;
		if (init) {
			double a = gravity/2;
			double b = vel.y;
			double c = y_max - pos.y;
			
			t = (-b + Math.sqrt(b * b + 4 * a * c))/(2*a);
			time_til_note = (int) (t * 20.8);	// time when ball will hit
		} else {
			t = -vel.y * 2/gravity;
			time_til_note = (int) (t * 20.2);	// time when ball will hit
		}
	
		return pos.x + vel.x * t;
	}
	
	// draws the ball
	public void display(PApplet parent) {
		parent.ellipse(pos.x, pos.y, r, r);
	}
	
}
