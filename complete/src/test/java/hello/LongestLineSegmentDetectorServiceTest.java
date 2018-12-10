package hello;

import java.net.URI;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import junit.framework.TestCase;

public class LongestLineSegmentDetectorServiceTest extends TestCase {
	
	private static HttpHeaders headers = new HttpHeaders();
	private static boolean setupDone = false;
	private static Thread springThread = null;
	
	@Before
    public void setUp() throws InterruptedException {
		if(!setupDone) {
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			springThread = new Thread(() -> {
				SpringApplication.run(Application.class, new String[0]);
			});
			
			springThread.start();
			Thread.sleep(15000);
			setupDone = true;
		}
    }
	
	@Test
	public void testAddPoint() {
		assertEquals(true, addPointRequest(1f, 1f));
	}
	
	@Test
	public void testAddPointDuplicatePointsDetection() {
		clearPointsRequest();
		
		assertEquals(true, addPointRequest(3f, 3f));
		assertEquals(false, addPointRequest(3f, 3f));
	}
	
	@Test
	public void testGetPoints() {
		clearPointsRequest();
		
		addPointRequest(10f, 10f);
		addPointRequest(11f, 11f);
		addPointRequest(12f, 12f);
		
		Point[] points = getPointsRequest();
		
		assertEquals(3, points.length);
	}
	
	@Test
	public void testDetectLongestLineSegmentAllPointsCollinear() {
		clearPointsRequest();
		
		addPointRequest(10f, 5f);
		addPointRequest(15f, 5f);
		addPointRequest(20f, 5f);
		
		LineSegment[] lineSegments = getLongestLineSegmentsRequest(3);
		
		assertEquals(1, lineSegments.length);
	}
	
	@Test
	public void testDetectLongestLineSegmentTwoPointLineLargest() {
		clearPointsRequest();
		
		addPointRequest(0f, 0f);
		addPointRequest(100f, 100f);
		addPointRequest(30f, 30f);
		addPointRequest(40f, 30f);
		addPointRequest(50f, 30f);
		
		LineSegment[] lineSegments = getLongestLineSegmentsRequest(2);
		
		assertEquals(1, lineSegments.length);
	}
	
	@Test
	public void testDetectLongestLineSegmentTwoEqualyLargestLineSegments() {
		clearPointsRequest();
		
		addPointRequest(11f, 10f);
		addPointRequest(16f, 10f);
		addPointRequest(21f, 10f);
		
		addPointRequest(10f, 30f);
		addPointRequest(15f, 30f);
		addPointRequest(20f, 30f);
		
		LineSegment[] lineSegments = getLongestLineSegmentsRequest(3);
		
		assertEquals(2, lineSegments.length);
	}
	
    private boolean addPointRequest(float x, float y) {
		RestTemplate restTemplate = new RestTemplate();
	
		String url = "http://localhost:8080/addpoint";
		String requestJson = "{"
				+ "\"x\" : \"" +  x + "\"" + "," 
				+ "\"y\" : \"" +  y + "\""
				+ "}";
		
		HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
		return restTemplate.postForObject(url, entity, Boolean.class);
    }
    
    private Point[] getPointsRequest() {
    	RestTemplate restTemplate = new RestTemplate();
    	
		String url = "http://localhost:8080/space";
		return restTemplate.getForObject(url, Point[].class);
    }
    
    private void clearPointsRequest() {
		RestTemplate restTemplate = new RestTemplate();
    	
		String url = "http://localhost:8080/space";
		restTemplate.delete(URI.create(url));
    }
    
    private LineSegment[] getLongestLineSegmentsRequest(int minPointCount) {
    	RestTemplate restTemplate = new RestTemplate();
    	
		String url = "http://localhost:8080/lines/" + minPointCount;
		return restTemplate.getForObject(url, LineSegment[].class);
    }
}
