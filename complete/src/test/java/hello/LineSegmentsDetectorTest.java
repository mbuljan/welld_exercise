package hello;

import java.util.ArrayList;

import org.junit.Test;

import junit.framework.TestCase;

public class LineSegmentsDetectorTest extends TestCase {
	
	private LineSegmentsDetector longestLineSegmentsCalculator = new LineSegmentsDetector();
	
	@Test
	public void testGetAllLineSegmentsSingleLineSegmentHorizontal() {
		ArrayList<Point> points = new ArrayList<Point>();
		
		points.add(new Point(2f, 1f));
		points.add(new Point(1f, 1f));
		points.add(new Point(3f, 1f));
		
		ArrayList<LineSegment> lineSegments = longestLineSegmentsCalculator.getLineSegments(points, 0);
		
		assertEquals(2, lineSegments.size());
	}
	
	@Test
	public void testGetAllLineSegmentsSingleLineSegmentVertical() {
		ArrayList<Point> points = new ArrayList<Point>();
		
		points.add(new Point(1f, 2f));
		points.add(new Point(1f, 1f));
		points.add(new Point(1f, 3f));
		
		ArrayList<LineSegment> lineSegments = longestLineSegmentsCalculator.getLineSegments(points, 0);
		
		assertEquals(2, lineSegments.size());
		assertEquals(1f, lineSegments.get(0).getPoints()[0].getX());
		assertEquals(1f, lineSegments.get(0).getPoints()[0].getY());
		
		assertEquals(1f, lineSegments.get(0).getPoints()[1].getX());
		assertEquals(2f, lineSegments.get(0).getPoints()[1].getY());
		
		assertEquals(1f, lineSegments.get(0).getPoints()[2].getX());
		assertEquals(3f, lineSegments.get(0).getPoints()[2].getY());
	}
	
	@Test
	public void testGetAllLineSegmentsSingleLineSegmentDiagonal() {
		ArrayList<Point> points = new ArrayList<Point>();
		
		points.add(new Point(4f, 4f));
		points.add(new Point(5f, 5f));
		points.add(new Point(6f, 6f));
		
		ArrayList<LineSegment> lineSegments = longestLineSegmentsCalculator.getLineSegments(points, 0);
		
		assertEquals(2, lineSegments.size());
	}
	
	@Test
	public void testGetAllLineSegmentsSquare() {
		ArrayList<Point> points = new ArrayList<Point>();
		
		points.add(new Point(2f, 2f));
		points.add(new Point(2f, 3f));
		points.add(new Point(3f, 2f));
		points.add(new Point(3f, 3f));
		
		ArrayList<LineSegment> lineSegments = longestLineSegmentsCalculator.getLineSegments(points, 0);
		
		assertEquals(6, lineSegments.size());
	}
	
	@Test
	public void testGetAllLineSegmentsTwoPoints() {
		ArrayList<Point> points = new ArrayList<Point>();
		
		points.add(new Point(2f, 2f));
		points.add(new Point(11f, 15f));
		
		ArrayList<LineSegment> lineSegments = longestLineSegmentsCalculator.getLineSegments(points, 0);
		
		assertEquals(1, lineSegments.size());
	}
	
}
