package game;

// Abstract class GameObject
public abstract class GameObject {
    private int state;                // state
    private double X;                 // X coordinate
    private double Y;                 // Y coordinate
    private double radius;            // radius (size)

    /* method that returns whether or not the object collided with the object passed as a parameter */
    public boolean collidesWith(GameObject other) {
        double dx = this.getX() - other.getX();
        double dy = this.getY() - other.getY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        return dist < (this.getRadius() + other.getRadius()) * 0.8;
    }

    /* getters and setters */
    public int getState() {
        return state;
    }
    public double getX() {
        return this.X;
    }
    public double getY() {
        return this.Y;
    }
    public double getRadius() {
        return this.radius;
    }
    public void setState(int state) {
        this.state = state;
    }
    public void setX(double x) {
        X = x;
    }
    public void setY(double y) {
        Y = y;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }
}



