package hello;

import java.util.ArrayList;

public class LineSegment {
	
	private Point[] points = null;
		
	public LineSegment() {}
	
	public LineSegment(Point[] points) {
		this.points = points;
	}
	
	public Point[] getPoints() {
		return points;
	}
	
	public void setPoints(Point[] points) {
		this.points = points;
	}
	
	public double getLengthSquared() {
		int pointCount = points.length;
		float deltaX = points[pointCount - 1].getX() - points[0].getX();
		float deltaY = points[pointCount - 1].getY() - points[0].getY();
		return deltaX * deltaX + deltaY * deltaY;
	}
}
