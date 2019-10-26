import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Graph {
	private Map<String, Graph.Node> nodeMap;
	private ArrayList<Node> checked;
	
	Graph() {
		this.nodeMap = new HashMap<String, Graph.Node>();
		this.checked = new ArrayList<Node>();
	}
	
	public void addNode(String name, Graph.Node n) throws IllegalArgumentException {
		if(name.isEmpty() || name == null) throw new IllegalArgumentException("Node Must Have a name");
		this.nodeMap.put(name, n);
	}
	
	public void addNode(String name, double x, double y, ArrayList<Node> neighbors, ArrayList<Double> distances) throws IllegalArgumentException {
		if(name.isEmpty() || name == null) throw new IllegalArgumentException("Node Must Have a name");
		if(neighbors.size() != distances.size()) throw new IllegalArgumentException("Parrallel arrays for neighbors and distances must have same size");
		ArrayList<Graph.NodeTuple> neighborsArr = new ArrayList<Graph.NodeTuple>();
		for(int i = 0; i < neighbors.size(); i++) {
			Graph.NodeTuple t = new Graph.NodeTuple(neighbors.get(i), distances.get(i));
			neighborsArr.add(t);
		}
		Graph.Node n = new Graph.Node(x, y, neighborsArr);
		this.nodeMap.put(name, n);
	}
	
	public void removeNode(String name) {
		Graph.Node n = this.nodeMap.get(name);
		for(Graph.NodeTuple a : n.neighbors) {
			for(Graph.NodeTuple b : a.aItem.neighbors) {
				if(b.aItem.equals(n)) {
					a.aItem.neighbors.remove(b);
					break;
				}
			}
		}
		this.nodeMap.remove(name);
	}
	
	public ArrayList<Graph.Node> findPath(String startLoc, String endLoc) {
		Graph.Node start = this.nodeMap.get(startLoc);
		Graph.Node end = this.nodeMap.get(endLoc);
		this.checked = new ArrayList<Node>();
		PriorityQueue<Graph.NodeTuple> q = new PriorityQueue<Graph.NodeTuple>();
		q.add(new Graph.NodeTuple(start, start.straightDistance(end)));
		Graph.NodeTuple current = null;
		do {
			current = q.poll();
			this.checked.add(current.aItem);
			current.aItem.addNeigborsToQueue(q, end, current, current.bItem - current.aItem.straightDistance(end));
		} while(!current.aItem.equals(end));
		ArrayList<Graph.Node> path = new ArrayList<>();
		while(current.previous != null) {
			path.add(current.aItem);
			current = current.previous;
		}
		path.add(start);
		return path;
	}
	
	public class Node {
		private double x, y;
		public String key;
		public ArrayList<Graph.NodeTuple> neighbors;
		
		Node(double x, double y) {
			this.x = x;
			this.y = y;
			this.neighbors = new ArrayList<Graph.NodeTuple>();
		}
		
		Node(double x, double y, String key) {
			this.x = x;
			this.y = y;
			this.key = key;
			this.neighbors = new ArrayList<Graph.NodeTuple>();
		}
		
		Node(double x, double y, ArrayList<Graph.NodeTuple> neighbors) {
			this.x = x;
			this.y = y;
			this.neighbors = neighbors;
		}
		
		public void addNeighbor(Node n, double d) {
			this.neighbors.add(new Graph.NodeTuple(n, d));
		}
		
		public void removeNeighbor(Node n, double d) {
			this.neighbors.remove(new Graph.NodeTuple(n, d));
		}
		
		public Double straightDistance(Graph.Node n) {
			return Math.sqrt(
					((n.x - this.x) * (n.x - this.x)) + 
					((n.y - this.y) * (n.y - this.y))
					);
		}
	
		public void addNeigborsToQueue(PriorityQueue<Graph.NodeTuple> q, Graph.Node destination, Graph.NodeTuple previous, double overhead) {
			for(Graph.NodeTuple t : this.neighbors) {
				if(!checked.contains(t.aItem)) {
				q.add(new Graph.NodeTuple(t.aItem, overhead + t.bItem + t.aItem.straightDistance(destination), previous));
				}
			}
		}
		
		public boolean nextTo(Graph.Node n) {
			for(Graph.NodeTuple t : neighbors) {
				if(t.aItem.equals(n)) return true;
			}
			return false;
		}
	}
	
	private class NodeTuple implements Comparable<Graph.NodeTuple>{
		public Graph.Node aItem;
		public double bItem;
		public Graph.NodeTuple previous;
		
		NodeTuple(Graph.Node n, double d) {
			this.aItem = n;
			this.bItem = d;
		}
		
		NodeTuple(Graph.Node n, double d, Graph.NodeTuple previous) {
			this.aItem = n;
			this.bItem = d;
			this.previous = previous;
		}
		
		@Override
		public int compareTo(Graph.NodeTuple t) {
			if (this.bItem == t.bItem) return 0;
			return this.bItem > t.bItem ? 1 : -1;
		}
	}
}
