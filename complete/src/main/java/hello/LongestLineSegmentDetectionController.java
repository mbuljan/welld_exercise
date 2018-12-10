package hello;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class LongestLineSegmentDetectionController {

	private static Map<String, Point> points = new HashMap<String, Point>();	
    
    @RequestMapping(method = RequestMethod.POST, value = "/addpoint")
    public Boolean addPoint(@RequestBody Point point) {
        String key = point.getX() + "," + point.getY();
        if(points.containsKey(key)) {
        	return false;
        }
        
        points.put(key, point);
        return true;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/space")
    public Point[] getPoints() {
    	Point[] result = new Point[points.values().size()];
        return points.values().toArray(result);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/lines/{minPointCount}")
    @ResponseBody
    public LineSegment[] detectLongestLineSegment(
    		@PathVariable("minPointCount") int minPointCount) {
        
    	if(points.size() <= 1) {
    		return new LineSegment[0];
    	}
    	
    	LineSegmentsDetector lineSegmentdetector = new LineSegmentsDetector();
    	ArrayList<Point> pointsArrayList = new ArrayList<Point>(points.values());
    	ArrayList<LineSegment> lineSegmentList = lineSegmentdetector.
    			getLineSegments(pointsArrayList, minPointCount);
    	
    	if(lineSegmentList.isEmpty()) {
    		return new LineSegment[0];
    	}
    	
    	lineSegmentList.sort((LineSegment lineSegment1, LineSegment lineSegment2) -> {
    		double lineSegment1Magnitude = lineSegment1.getLengthWithoutSqrt();
    		double lineSegment2Magnitude = lineSegment2.getLengthWithoutSqrt();
    		if(lineSegment1Magnitude < lineSegment2Magnitude) {
    			return 1;
    		} else if(lineSegment1Magnitude > lineSegment2Magnitude) {
    			return -1;
    		} else {
    			return 0;
    		}
    	});
    	
    	// check if there are multiple line segments of the same length
    	LineSegment largestLineSegment = lineSegmentList.get(0);
    	double largestLength = lineSegmentList.get(0).getLengthWithoutSqrt();
    	ArrayList<LineSegment> result = new ArrayList<LineSegment>();
    	result.add(largestLineSegment);
    	
    	for(int i = 1; i < lineSegmentList.size(); i++) {
    		LineSegment currentLineSegment = lineSegmentList.get(i);
    		if(currentLineSegment.getLengthWithoutSqrt() == largestLength) {
    			result.add(currentLineSegment);
    		}
    	}
    	
    	return result.toArray(new LineSegment[0]);
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value = "/space")
    public void deleteAllPoints() {
        points.clear();
    }
}
