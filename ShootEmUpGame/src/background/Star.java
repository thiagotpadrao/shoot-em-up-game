package background;

import java.awt.Color;

import lib.GameLib;

public class Star {
    private double [] X;            // stores the X coordinates 
	private double [] Y;            // stores the Y coordinates
	private double speed;           // speed 
	private double count = 0.0;     // counter to assist in drawing
    private Color cor;              // color
    private int size;               // size

    public Star(int X, int Y, double speed, Color cor, int size){
        this.X = new double [X];
        this.Y = new double [Y];
        this.speed = speed;
        this.cor = cor;
        this.size = size;

        /* defines random positions for the stars across the screen */
        for(int i = 0; i < this.X.length; i++){
			
			this.X[i] = Math.random() * GameLib.WIDTH;
			this.Y[i] = Math.random() * GameLib.HEIGHT;
		}
    }

    /* method to draw the stars */
    public void draw(Long delta){
        GameLib.setColor(cor);
		count += speed * delta;
			
		for(int i = 0; i < X.length; i++){	
			GameLib.fillRect(X[i], (Y[i] + count) % GameLib.HEIGHT, size, size);
		}
    }

    /* getters and setters */
    public double[] getX() {
        return X;
    }
    public double[] getY() {
        return Y;
    }
    public double getSpeed() {
        return speed;
    }
    public double getCount() {
        return count;
    }
    public Color getCor() {
        return cor;
    }
    public int getSize() {
        return size;
    }
    public void setX(double[] x) {
        X = x;
    }
    public void setY(double[] y) {
        Y = y;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    public void setCount(double count) {
        this.count = count;
    }
    public void setCor(Color cor) {
        this.cor = cor;
    }
    public void setSize(int size) {
        this.size = size;
    }
}
