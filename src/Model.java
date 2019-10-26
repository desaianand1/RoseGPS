import java.util.ArrayList;

public class Model {
	private Graph g;
	ArrayList<Graph.Node> nodes;
	Graph.Node lake;
	
	/*
	 * All of the arrays below are parallel
	 */
	
	// Names of each node used in hashtable
	private String[] nodeNames = {
			"Apartments",
			"Blum",
			"BSB North",
			"BSB South",
			"BSB West",
			"Chapel North",
			"Chapel South",
			"Deming",
			"Lakeside North",
			"Lakeside South",
			"Mees",
			"Olin North",
			"Percopo North",
			"Percopo East",
			"Percopo South",
			"Percopo West",
			"Scharp",
			"Speed North",
			"Speed South",
			"SRC East",
			"SRC West",
			"Union North",
			"Union East",
			"Union South",
			"Union West"

	};
	
	// Cords of each node [x][0] is y, [x][1] is x
	private double[][] nodeCords = {
			{39.48359, -87.32902},	//apartments
			{39.48358, -87.32831},	//blum
			{39.48264, -87.32554},	//bsb north
			{39.48225, -87.32574},	//bsb south
			{39.48263, -87.32576},	//bsb west
			{ 39.48268, -87.32952},	//chapel north
			{ 39.48237, -87.32931},	//chapel south
			{39.48326, -87.32575},	//deming
			{39.48331, -87.33023},	//lakeside north
			{39.48241, -87.33077},	//lakeside south
			{39.48366, -87.32782},	//mees
			{39.48294, -87.32541},	//olin north
			{ 39.48225, -87.32828},	//percopo north
			{ 39.48206, -87.32784},	//per east
			{ 39.48192, -87.32827},	//per south
			{ 39.48203, -87.32890},	//per west
			{39.48374, -87.32816},	//scharp
			{39.48223, -87.32656},	//speed north
			{39.4821, -87.32657},	//speed south
			{39.48484, -87.3264},	//src east
			{39.48475, -87.32815},	//src west
			{39.48365, -87.32631},	//union north
			{39.48343, -87.32631},	//union east
			{39.48315, -87.32664},	//union south
			{39.48365, -87.32728}	//union west
	};
	
	
	Model() {
		g = new Graph();
		
		// Create all the nodes and add them to an ArrayList
		nodes = new ArrayList<Graph.Node>();
		for(int i = 0; i < this.nodeNames.length; i++) {
			Graph.Node n = g.new Node(this.nodeCords[i][1],this.nodeCords[i][1], this.nodeNames[i]);
			this.nodes.add(n);
		}
		int apartments = 0;
		int blum = 1;
		int bsbNorth = 2;
		int bsbSouth = 3;
		int bsbWest = 4;
		int chapelNorth = 5;
		int chapelSouth = 6;
		int deming = 7;
		int lakesideNorth = 8;
		int lakesideSouth = 9;
		int mees = 10;
		int olinNorth = 11;
		int percopoNorth = 12;
		int percopoEast = 13;
		int percopoSouth = 14;
		int percopoWest = 15;
		int scharp = 16;
		int speedNorth = 17;
		int speedSouth = 18;
		int srcEast = 19;
		int srcWest = 20;
		int unionNorth = 21;
		int unionEast = 22;
		int unionSouth = 23;
		int unionWest = 24;
		// Set the correct neighbors for each node
		Graph.Node[][] neighbors = {
				{	// Apartments DONE
					nodes.get(lakesideNorth),
					nodes.get(scharp),
					nodes.get(blum),
					nodes.get(chapelNorth),
					nodes.get(unionSouth)
				},
				{	// Blum DONE
					nodes.get(scharp),
					nodes.get(apartments),
					nodes.get(mees),
					nodes.get(chapelNorth),
					nodes.get(unionSouth)
				},
				{	//bsb north DONE
					nodes.get(bsbSouth),
					nodes.get(olinNorth)
				},
				{	//bsb south DONE
					nodes.get(speedNorth),
					nodes.get(speedSouth),
					nodes.get(bsbNorth),
					nodes.get(bsbWest),
				},
				{	//bsb west DONE
					nodes.get(speedNorth),
					nodes.get(speedSouth),
					nodes.get(bsbSouth),
					nodes.get(olinNorth)
				},
				{   // Chapel North DONE
					nodes.get(chapelSouth),
					nodes.get(percopoWest),	//searching here
					nodes.get(blum),
					nodes.get(mees),
					nodes.get(unionSouth),
					nodes.get(apartments)
				},
				{	// Chapel South DONE
					nodes.get(percopoNorth),
					nodes.get(percopoWest),
					nodes.get(chapelNorth)
				},
				{	//deming DONE
					nodes.get(unionSouth),
					nodes.get(unionEast),
					nodes.get(olinNorth),
					nodes.get(percopoNorth),
					nodes.get(percopoEast),
					nodes.get(speedNorth)
				},
				{	// Lakeside North DONE
					nodes.get(lakesideSouth),
					nodes.get(apartments)
				},
				{	// Lakeside South DONE
					nodes.get(percopoWest),
					nodes.get(percopoSouth),
					nodes.get(lakesideNorth)
				},
				{	//Mees DONE
					nodes.get(scharp),
					nodes.get(chapelNorth),
					nodes.get(blum),
					nodes.get(unionSouth),
					nodes.get(srcWest),
					nodes.get(srcEast),
					nodes.get(unionWest)
				},
				{	//olin north DONE
					nodes.get(deming),
					nodes.get(bsbNorth),
					nodes.get(bsbWest),
					nodes.get(percopoEast),
					nodes.get(unionSouth),
					nodes.get(percopoNorth)
				},
				{	// Percopo North DONE
					nodes.get(percopoEast),
					nodes.get(percopoWest),
					nodes.get(chapelSouth),
					nodes.get(unionSouth),
					nodes.get(speedNorth),
					nodes.get(olinNorth),
					nodes.get(deming)
				},
				{	// Percopo East DONE
					nodes.get(percopoNorth),
					nodes.get(percopoSouth),
					nodes.get(unionSouth),
					nodes.get(speedSouth),
					nodes.get(olinNorth),
					nodes.get(deming)
				},
				{	// Percopo South DONE
					nodes.get(percopoEast),
					nodes.get(percopoWest),
					nodes.get(lakesideSouth),
					nodes.get(speedSouth)
				},
				{	// Percopo West DONE
					nodes.get(percopoNorth),
					nodes.get(percopoSouth),
					nodes.get(chapelSouth),
					nodes.get(chapelNorth),
					nodes.get(lakesideSouth)
				},
				{	// Scharp DONE
					nodes.get(mees),
					nodes.get(blum),
					nodes.get(apartments),
					nodes.get(srcWest),
					nodes.get(srcEast)
				},
				{	//Speed North DONE
					nodes.get(percopoNorth),
					nodes.get(unionSouth),
					nodes.get(speedSouth),
					nodes.get(bsbSouth),
					nodes.get(bsbWest),
					nodes.get(deming)
				},
				{	//Speed South DONE
					nodes.get(speedNorth),
					nodes.get(percopoSouth),
					nodes.get(percopoEast),
					nodes.get(bsbSouth),
					nodes.get(bsbWest)
				},
				{	//src east DONE
					nodes.get(mees),
					nodes.get(srcWest),
					nodes.get(unionNorth),
					nodes.get(unionWest)
				},
				{	//src west DONE
					nodes.get(mees),
					nodes.get(scharp),
					nodes.get(unionWest),
					nodes.get(unionNorth),
					nodes.get(srcEast)
				},
				{	//union north DONE
					nodes.get(srcWest),
					nodes.get(srcEast),
					nodes.get(unionEast),
					nodes.get(unionWest)
				},
				{	//union east DONE
					nodes.get(deming),
					nodes.get(unionNorth)
				},
				{	//Union South DONE
					nodes.get(chapelNorth),
					nodes.get(apartments),
					nodes.get(blum),
					nodes.get(mees),
					nodes.get(unionWest),
					nodes.get(percopoNorth),
					nodes.get(percopoEast),
					nodes.get(speedNorth),
					nodes.get(deming),
					nodes.get(olinNorth)
				},
				{	//union west DONE
					nodes.get(mees),
					nodes.get(srcWest),
					nodes.get(srcEast),
					nodes.get(unionNorth),
					nodes.get(unionSouth)
				}
		};
		
		// Set the correct distances to each neighbor
		double[][] distances = {
				{	//Apartments
					435.0,
					278.0,
					208.0,
					523.0,
					767.0
				},
				{	//Blum
					120.0,
					238.0,
					202.0,
					656.0,
					587.0
				},
				{	//bsb north
					153.0,
					141.0
				},
				{	//bsb south
					254.0,
					262.0,
					153.0,
					302.0
				},
				{	//bsb west
					321.0,
					339.0,
					302.0,
					189.0
				},
				{	//chapel north
					278.0,
					332.0,
					699.0,
					639.0,
					877.0,
					523.0
				},
				{	//chapel south
					309.0,
					175.0,
					278.0
				},
				{	//deming
					324.0,
					170.0,
					266.0,
					894.0,
					845.0,
					558.0
				},
				{	//Lakeside north
					520.0,
					435.0
				},
				{	//lakeside South
					702.0,
					791.0,
					520.0
				},
				{	//mees
					163.0,
					639.0,
					202.0,
					566.0,
					580.0,
					891.0,
					202.0
				},
				{	//olin north
					266.0,
					141.0,
					189.0,
					963.0,
					477.0,
					1011.0
				},
				{	//percopo north
					354.0,
					304.0,
					309.0,
					671.0,
					741.0,
					1011.0,
					894.0
				},
				{	//percopo east
					354.0,
					178.0,
					623.0,
					451.0,
					963.0,
					845.0
				},
				{	//percopo south
					178.0,
					200.0,
					791.0,
					483.0
				},
				{	//percopo west
					304.0,
					200.0,
					175.0,
					372.0,
					702.0
				},
				{	//Scharp
					163.0,
					120.0,
					248.0,
					466.0,
					1043.0
				},
				{	//speed north
					741.0,
					328.0,
					89.0,
					254.0,
					321.0,
					558.0
				},
				{	//speed south
					89.0,
					483.0,
					451.0,
					262.0,
					339.0
				},
				{	//src east
					891.0,
					796.0,
					608.0,
					693.0
				},
				{	//src west
					580.0,
					466.0,
					664.0,
					965.0,
					796.0
				},
				{	//union north
					965.0,
					608.0,
					82.0,
					497.0
				},
				{	//union east
					170.0,
					82.0
				},
				{	//union south
					877.0,
					767.0,
					587.0,
					566.0,
					345.0,
					671.0,
					623.0,
					328.0,
					324.0,
					477.0
				},
				{	//union west
					202.0,
					664.0,
					693.0,
					497.0,
					345.0,
				}	
		};
		
		
		for(int i = 0; i < this.nodeNames.length; i++) {
			for(int j = 0; j < neighbors[i].length; j++) {
				nodes.get(i).addNeighbor(neighbors[i][j], distances[i][j]);
			}
			g.addNode(this.nodeNames[i], nodes.get(i));
		}
		
		
		
		this.lake = g.new Node( 39.48322, -87.32801,"Lake North");
		lake.addNeighbor(nodes.get(percopoNorth), 542.0);
		lake.addNeighbor(nodes.get(percopoEast), 494.0);
		lake.addNeighbor(nodes.get(blum), 197.0);
		lake.addNeighbor(nodes.get(mees), 179.0);
		
		return;
	}
	
	public void lakeIsAllowed() {
		nodes.get(12).addNeighbor(lake, 542.0);
		nodes.get(13).addNeighbor(lake, 494.0);
		nodes.get(1).addNeighbor(lake, 197.0);
		nodes.get(10).addNeighbor(lake, 179.0);
		this.g.addNode("Lake North", this.lake);
		System.out.println("Lake turned on");
	}
	
	public void lakeIsntAllowed() {
//		nodes.get(12).removeNeighbor(lake, 542.0);
//		nodes.get(13).removeNeighbor(lake, 494.0);
//		nodes.get(1).removeNeighbor(lake, 197.0);
//		nodes.get(10).removeNeighbor(lake, 179.0);
		this.g.removeNode("Lake North");
		System.out.println("lake turned off");
	}
	
	public String[] getNodeNames() {
		return this.nodeNames;
	}
	
	public ArrayList<Graph.Node> findPath(String start, String end) {
		return g.findPath(start, end);
	}

}
