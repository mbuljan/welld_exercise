package hello;

public class Point {
	
	private float x = 0;
	private float y = 0;
	
	public Point() {}
	
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return x; 
	}
	
	public float getY() {
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Point)) {
			return false;
		}
		
		Point otherPoint = (Point) o;
		return x == otherPoint.getX() && y == otherPoint.getY();
	}
}
