import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

import org.junit.AfterClass;
import org.junit.Test;

@SuppressWarnings("rawtypes")
public class Testing {
	
	private static int points = 0;
	
///////////  BASIC TESTS
	
	@Test
	public void testNextTo() {
		Graph g = new Graph();
		Graph.Node N1 = g.new Node(1, 0);
		Graph.Node N2 = g.new Node(1.5, 0);
		N1.addNeigbor(N2, 10);
		assertTrue(N1.nextTo(N2));
		assertFalse(N2.nextTo(N1));
		
		N1 = g.new Node(1, 0);
		N2 = g.new Node(5.5, 0);
		N1.addNeigbor(N2, 10);
		assertTrue(N1.nextTo(N2));
		assertFalse(N2.nextTo(N1));
		
		N1 = g.new Node(1, 0);
		N2 = g.new Node(20, 0);
		N2.addNeigbor(N1, 10);
		assertTrue(N2.nextTo(N1));
		assertFalse(N1.nextTo(N2));
		
		N1 = g.new Node(1, 0);
		N2 = g.new Node(5.5, 0);
		N1.addNeigbor(N2, 10);
		N2.addNeigbor(N1, 10);
		assertTrue(N1.nextTo(N2));
		assertTrue(N2.nextTo(N1));
		
		points += 5;
	}
	
	@Test
	public void testStraightDistance() {
		Graph g = new Graph();
		Graph.Node a = g.new Node(0, 0);
		Graph.Node b = g.new Node(0, 1);
		Graph.Node c = g.new Node(2, 2);
		Graph.Node d = g.new Node(3, 2);
		Graph.Node e = g.new Node(5, 0);
		
		a.addNeigbor(b, 1);
		a.addNeigbor(c, 3);
		a.addNeigbor(e, 8);
		b.addNeigbor(a, 1);
		b.addNeigbor(c, 3);
		c.addNeigbor(a, 3);
		c.addNeigbor(b, 3);
		c.addNeigbor(d, 1);
		d.addNeigbor(c, 1);
		d.addNeigbor(e, 3);
		e.addNeigbor(a, 8);
		e.addNeigbor(d, 3);
		
		assertEquals(new Double(5), a.straightDistance(e));
		assertEquals(new Double(1), a.straightDistance(b));
		assertEquals(new Double(Math.sqrt(5)), b.straightDistance(c));
	}
	
	@Test
	public void testPathFinding() {
		Graph g = new Graph();
		Graph.Node a = g.new Node(0, 0);
		Graph.Node b = g.new Node(0, 1);
		Graph.Node c = g.new Node(2, 2);
		Graph.Node d = g.new Node(3, 2);
		Graph.Node e = g.new Node(5, 0);
		
		a.addNeigbor(b, 1);
		a.addNeigbor(c, 3);
		a.addNeigbor(e, 8);
		b.addNeigbor(a, 1);
		b.addNeigbor(c, 3);
		c.addNeigbor(a, 3);
		c.addNeigbor(b, 3);
		c.addNeigbor(d, 1);
		d.addNeigbor(c, 1);
		d.addNeigbor(e, 3);
		e.addNeigbor(a, 8);
		e.addNeigbor(d, 3);
		
		g.addNode("A", a);
		g.addNode("B", b);
		g.addNode("C", c);
		g.addNode("D", d);
		g.addNode("E", e);
		
		ArrayList<Graph.Node> path = new ArrayList<Graph.Node>();
		path.add(e);
		path.add(d);
		path.add(c);
		path.add(a);
		assertEquals(path, g.findPath("A", "E"));
		path = new ArrayList<Graph.Node>();
		path.add(d);
		path.add(c);
		path.add(a);
		assertEquals(path, g.findPath("A", "D"));
		
		g = new Graph();
		a = g.new Node(0, 0);
		b = g.new Node(0, 1);
		c = g.new Node(2, 2);
		d = g.new Node(3, 2);
		e = g.new Node(5, 0);
		
		a.addNeigbor(b, 1);
		a.addNeigbor(c, 3);
		a.addNeigbor(e, 6.5);
		b.addNeigbor(a, 1);
		b.addNeigbor(c, 3);
		c.addNeigbor(a, 3);
		c.addNeigbor(b, 3);
		c.addNeigbor(d, 1);
		d.addNeigbor(c, 1);
		d.addNeigbor(e, 3);
		e.addNeigbor(a, 8);
		e.addNeigbor(d, 3);
		
		g.addNode("A", a);
		g.addNode("B", b);
		g.addNode("C", c);
		g.addNode("D", d);
		g.addNode("E", e);
		
		path = new ArrayList<Graph.Node>();
		path.add(e);
		path.add(a);
		assertEquals(path, g.findPath("A", "E"));
	}
	
	@Test
	public void testModel() {
		Model m = new Model();
		ArrayList<String> nodes = new ArrayList<String>();
		nodes.add("Chapel South");
		nodes.add("Percopo West");
		nodes.add("Percopo South");
		nodes.add("Percopo East");
		ArrayList<Graph.Node> path =  m.findPath("Percopo East", "Chapel South");
		ArrayList<String> pathStrings = new ArrayList<String>();
		for(Graph.Node n : path) {
			pathStrings.add(n.key);
		}
		assertEquals(nodes, pathStrings);
	}

	@AfterClass
	public static void testDoNothing(){
		System.out.println("Points: " + points);
	}

}


