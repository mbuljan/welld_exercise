package hello;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

public class LineSegmentsDetector {
	private final float ALLOWED_COLLINEAR_DETECTION_TOLERANCE = 0.01f;
	
	public ArrayList<LineSegment> getLineSegments(ArrayList<Point> points, int minPointCount) {
		ArrayList<LineSegment> result = new ArrayList<LineSegment>();
		
		// ensuring the proper order of points for line segment construction
		sortPointsFromLeftToRight(points);
		
		// detecting line segments for each possible point combination
		for(int i = 0; i < points.size(); i++) {
			Point referencePoint = points.get(i);
			
			ArrayList<SimpleEntry<Double, Point>> pointRadiansPairList = 
					new ArrayList<SimpleEntry<Double, Point>>();
			
			// finding an angle between the reference point and every other point
			for(int j = i; j < points.size(); j++) {
				if(i == j) continue;
				
				Point currentPoint = points.get(j);
				double deltaY = currentPoint.getY() - referencePoint.getY();
				double deltaX = currentPoint.getX() - referencePoint.getX();
				Double radians = Math.atan2(deltaY, deltaX);
				pointRadiansPairList.add(new SimpleEntry<Double, Point>(radians, currentPoint));
			}
			
			// sorting the point pairs by their angle with the reference point
			sortPointRadiansPairsByAngleWithReferencePoint(pointRadiansPairList, referencePoint);
			
			// detecting collinear points by finding points with the same angle
			// towards the reference point
			ArrayList<LineSegment> lineSegments = findLineSegments(
					referencePoint, pointRadiansPairList, minPointCount);
			result.addAll(lineSegments);
		}
		
		return result;
	}
	
	private ArrayList<LineSegment> findLineSegments(Point referencePoint,
			ArrayList<SimpleEntry<Double, Point>> pointRadiansPairList, int minPointCount) {
		
		ArrayList<LineSegment> lineSegmentList = new ArrayList<LineSegment>();
		
		if(pointRadiansPairList.size() == 0) {
			return lineSegmentList;
		}
		
		SimpleEntry<Double, Point> currentPointRadiansPair = pointRadiansPairList.get(0);
		ArrayList<Point> lineSegmentPoints = new ArrayList<Point>();
		double lastRadians = currentPointRadiansPair.getKey();
		for(int i = 0; i < pointRadiansPairList.size(); i++) {
			
			if(lineSegmentPoints.size() == 0) {
				lineSegmentPoints.add(referencePoint);
			}
			
			double currentRadians = pointRadiansPairList.get(i).getKey();
			Point currentPoint = pointRadiansPairList.get(i).getValue();
			
			double diff = Math.abs(Math.abs(currentRadians) - Math.abs(lastRadians));
			boolean isAngleCollinear = 
					diff > -ALLOWED_COLLINEAR_DETECTION_TOLERANCE && 
					diff < ALLOWED_COLLINEAR_DETECTION_TOLERANCE;
			
			if(isAngleCollinear) {
				lineSegmentPoints.add(currentPoint);
			} else {
				if(lineSegmentPoints.size() >= minPointCount) {
					reorderPointsForVerticalLines(lineSegmentPoints);
					LineSegment lineSegment = new LineSegment(lineSegmentPoints.toArray(new Point[0]));
					lineSegmentList.add(lineSegment);
				}
				lineSegmentPoints = new ArrayList<Point>();
				lineSegmentPoints.add(referencePoint);
				lineSegmentPoints.add(currentPoint);
			}
			
			lastRadians = currentRadians;
		}
		
		if(!lineSegmentPoints.isEmpty()) {
			reorderPointsForVerticalLines(lineSegmentPoints);
			LineSegment lineSegment = new LineSegment(lineSegmentPoints.toArray(new Point[0]));
			lineSegmentList.add(lineSegment);
		}
		
		return lineSegmentList;
	}
	
	private boolean isLineVertical(ArrayList<Point> points) {
		float xValue = points.get(0).getX();
		for(int i = 0; i < points.size(); i++) {
			if(points.get(i).getX() != xValue) {
				return false;
			}
		}
		
		return true;
	}
	
	private void reorderPointsForVerticalLines(ArrayList<Point> points) {
		if(isLineVertical(points)) {
			sortPointsFromDownToUp(points);
		}
	}
	
	private void sortPointsFromLeftToRight(ArrayList<Point> points){
		points.sort((Point p1, Point p2) -> {
			if(p1.getX() == p2.getX()) {
				if(p1.getY() == p2.getY()) {
					return 0;
				} else if(p1.getY() < p2.getY()) {
					return -1;
				} else {
					return 1;
				}
			} else if(p1.getX() < p2.getX()) {
				return -1;
			} else {
				return 1;
			}
		});
	}
	
	private void sortPointsFromDownToUp(ArrayList<Point> points){
		points.sort((Point p1, Point p2) -> {
			if(p1.getY() == p2.getY()) {
				return 0;
			} else if(p1.getY() < p2.getY()) {
				return -1;
			} else {
				return 1;
			}
		});
	}
	
	private void sortPointRadiansPairsByAngleWithReferencePoint(
			ArrayList<SimpleEntry<Double, Point>> pointRadiansPairList, 
			Point referencePoint) {
		
		pointRadiansPairList.sort((SimpleEntry<Double, Point> pair1, 
				SimpleEntry<Double, Point> pair2) -> {
			
			if(pair1.getKey() < pair2.getKey())
				return -1;
			else if(pair1.getKey() > pair2.getKey())
				return 1;
			else
				return 0;
		});
	}
}