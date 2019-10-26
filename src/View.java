import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class View extends JFrame {
	static Model m;
	private static String[] choices;
	private static final int SCREEN_HEIGHT = 1080;
	private static final int SCREEN_WIDTH = 1920;
	private static final double WIDTH_SCALE = 0.2;
	private static final double HEIGHT_SCALE = 0.8;

	public View() {
		super("RoseGPS");
		// this.setSize(3000, 3000);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setMinimumSize(new Dimension(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.choices = m.getNodeNames();
	}

	public static void main(String[] args) {
		m = new Model();
		JFrame view = new View();

		/* Map Image */
		final ImagePanel p1 = new ImagePanel();

		/* Location Choices */
		JPanel p2 = new JPanel();
		p2.setSize(200, 200);
		p2.setLayout(new FlowLayout());

		/* search button */
		JButton search = new JButton("Search");

		/* Drop Box */
		final JComboBox<String> start_box = new JComboBox<>(choices);
		final JComboBox<String> end_box = new JComboBox<>(choices);
		start_box.setVisible(true);
		end_box.setVisible(true);
		JLabel start = new JLabel("Choose Start Location: ");
		JLabel end = new JLabel("Choose End Location: ");
		final JCheckBox lake = new JCheckBox();
		JLabel freeze = new JLabel("Lake is frozen");

		p2.add(start);
		p2.add(start_box);
		p2.add(end);
		p2.add(end_box);
		p2.add(search);
		p2.add(lake);
		p2.add(freeze);

		/* Direction PrintOut */
		JPanel p3 = new JPanel();
		final JTextArea cs = new JTextArea();

		/* Console */
		TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Console");
		border.setTitleJustification(TitledBorder.LEFT);
		cs.setBorder(border);
		cs.setEditable(false);
		cs.setPreferredSize(new Dimension((int) (WIDTH_SCALE * SCREEN_WIDTH), (int) (HEIGHT_SCALE * SCREEN_HEIGHT))); // 200,300
		cs.setFont(cs.getFont().deriveFont(16f));
		p3.add(cs);
		cs.append("\n The direction of the path will be displayed here.");
		cs.setForeground(Color.RED);

		lake.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(lake.isSelected()) {
					System.out.println("lake on " + lake.isSelected());
					m.lakeIsAllowed();
				} else {
					System.out.println("lake off " + lake.isSelected());
					m.lakeIsntAllowed();
				}
			}
		});
		
		/* Action listener for button */
		search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String startLoc = (String) start_box.getSelectedItem();
				String endLoc = (String) end_box.getSelectedItem();
				String instructions = "Passing Through: \n";

				ArrayList<Graph.Node> path = m.findPath(startLoc, endLoc);
				//System.out.println(path.toString());

				if (path.size() < 2) {
					throw new IndexOutOfBoundsException();
				}

				p1.clearAssets();

				for (int i = path.size() - 1; i > 0; i--) {
					Graph.Node current = path.get(i - 1);
					Graph.Node next = path.get(i);
					//System.out.println(current.key);
					//System.out.println(",");
					//System.out.println(next.key);
					p1.drawLine(current.key, next.key);

					instructions += (next.key + "\n");
				}
				
				instructions += ((String) endLoc);

				cs.setForeground(Color.BLUE);
				cs.setText("Going From: " + startLoc + "\n" + "Going To: " + endLoc);
				cs.append("\n" + instructions);
			}
		});

		view.add(p1, BorderLayout.CENTER);
		view.add(p2, BorderLayout.NORTH);
		view.add(p3, BorderLayout.EAST);
		view.setVisible(true);
	}
}

/* class for drawing the map and the nodes */
class ImagePanel extends JPanel {
	static private ImageIcon load_img;
	static private Image new_img;
	static private ImageIcon img;
	private MouseAdapter mouse;
	private ArrayList<position> points;
	private ArrayList<Integer> lines;
	private HashMap<String, path> viewPoints;

	public ImagePanel() {
		this.load_img = new ImageIcon("data/GPS.png");
		this.new_img = this.load_img.getImage().getScaledInstance(1438, 830, Image.SCALE_DEFAULT);
		this.img = new ImageIcon(new_img);

		// a simple mouse adapter that returns the position of the mouse click
		this.mouse = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				System.out.print("position:" + e.getX() + "," + e.getY() + " ");
			}
		};
		this.addMouseListener(mouse);

		this.points = new ArrayList<>();
		this.lines = new ArrayList<>();

		this.viewPoints = viewPointsInit();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		// draw image of the map
		g.drawImage(img.getImage(), 0, 0, this);
		g.setColor(Color.ORANGE);
		ArrayList<position> a = this.points;
		g.translate(-5, -5);
		// fill all the nodes
		for (int i = 0; i < a.size(); i++) {
			g.fillOval((int) a.get(i).x, (int) a.get(i).y, 10, 10);
		}

		// draw line test
		g2.setColor(Color.RED);
		g2.setStroke(new BasicStroke(3));

		for (int i = 0; i < this.lines.size(); i += 4) {
			g2.drawLine(this.lines.get(i), this.lines.get(i + 1), this.lines.get(i + 2), this.lines.get(i + 3));
		}
		
		g.translate(5, 5);
	}

	public void drawLine(String start, String end) {

		path p = this.viewPoints.get(end + start);
		int[] pathPoints = p.getPath();

		for (int i = 0; i < pathPoints.length; i += 4) {
			this.drawPoints(pathPoints[i], pathPoints[i + 1], pathPoints[i + 2], pathPoints[i + 3]);
		}
	}

	// redraws the points and lines on the button press, at random positions for
	// now
	public void drawPoints(double p11, double p12, double p21, double p22) {
		position p1 = new position(p11, p12);
		position p2 = new position(p21, p22);

		this.lines.add((int) (p11 + 5));
		this.lines.add((int) (p12 + 5));
		this.lines.add((int) (p21 + 5));
		this.lines.add((int) (p22 + 5));

		this.points.add(p1);
		this.points.add(p2);
		this.repaint();
	}

	public void clearAssets() {
		this.lines.clear();
		this.points.clear();
	}

	public class position {
		double x;
		double y;

		public position(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public position(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}

	public class path {
		private String name;
		private int[] pathPoints;

		public path(String name, int[] points) {
			this.name = name;
			this.pathPoints = points;
		}

		public int[] getPath() {
			return this.pathPoints;
		}
	}

	public HashMap<String, path> viewPointsInit() {
		/*
		 * percopo south = 514,808 
		 * percopo east = 592,778 
		 * percopo west = 397,784
		 * percopo north = 512,729
		 * chapel south = 320,702
		 * chapel north = 279,626
		 * lakeside south = 48,691
		 * lakeside north = 148,474
		 * apartments = 375,410
		 * blum = 491,427
		 * scharp = 534,371
		 * speed north = 856,729
		 * spped south = 830,765
		 * mees = 598,390
		 * union west = 700,394 
		 * union south = 818,514 
		 * lake north = 560,496
		 * lake south = 613,655
		 * lake junction = 642,710
		 * BSB West = 981,637
		 * BSB South = 983,730
		 * BSB North = 1021,636
		 * Deming = 983,487
		 * Union South = 817,515
		 * Union East = 878,446
		 * Olin North = 1047,565
		 */

		HashMap<String, path> ret = new HashMap<>();

		// grant works here
		// the image updated and now these are wrong

		int[] arr = {514,808,593,813,593,813,592,778};
		ret.put("Percopo SouthPercopo East", new path("Percopo SouthPercopo East", arr));
		ret.put("Percopo EastPercopo South", new path("Percopo EastPercopo South", arr));

		int[] arr2 = {514,808,415,810,415,810,397,795,397,795,397,784};
		ret.put("Percopo SouthPercopo West", new path("Percopo SouthPercopo West", arr2));
		ret.put("Percopo WestPercopo South", new path("Percopo WestPercopo South", arr2));

		int[] arr3 = {592,778,627,773,627,773,641,708,641,708,560,724,560,724,512,729 };
		ret.put("Percopo EastPercopo North", new path("Percopo EastPercopo North", arr3));
		ret.put("Percopo NorthPercopo East", new path("Percopo NorthPercopo East", arr3));

		int[] arr4 = {397,784,370,755,370,755,385,729,385,729,402,719,
				402,719,420,713,420,713,469,730,469,730,512,729};
		ret.put("Percopo WestPercopo North", new path("Percopo WestPercopo North", arr4));
		ret.put("Percopo NorthPercopo West", new path("Percopo NorthPercopo West", arr4));
		
		int[] arr5 = {397,784,371,754,371,754,330,722,330,722,320,702};
		ret.put("Percopo WestChapel South", new path("Percopo WestChapel South", arr5));
		ret.put("Chapel SouthPercopo West", new path("Chapel SouthPercopo West", arr5));
		
		int[] arr6 = {512,729,470,730,470,730,350,687,350,687,329,691,329,691,320,702};
		ret.put("Percopo NorthChapel South", new path("Percopo NorthChapel South", arr6));
		ret.put("Chapel SouthPercopo North", new path("Chapel SouthPercopo North", arr6));
		
		int[] arr7 = {320,702,316,720,316,720,296,693,296,693,270,693,270,693,255,661,255,661,266,630,266,630,279,626};
		ret.put("Chapel NorthChapel South", new path("Chapel NorthChapel South", arr7));
		ret.put("Chapel SouthChapel North", new path("Chapel SouthChapel North", arr7));
		
		int[] arr8 = {279,626,267,630,267,630,258,660,258,660,270,691,270,691,298,695,298,695,317,721,317,721,331,724,331,724,397,784};
		ret.put("Chapel NorthPercopo West", new path("Chapel NorthPercopo West", arr8));
		ret.put("Percopo WestChapel North", new path("Percopo WestChapel North", arr8));
		
		int[] arr9 = {48,691,66,696,66,696,83,705,83,705,103,721,103,721,118,742,118,742,179,734,179,734,199,739,
				199,739,292,784,292,784,304,794,304,794,314,824,314,824,341,822,341,822,416,811,416,811,397,795,
				397,795,397,784};
		ret.put("Lakeside SouthPercopo West", new path("Lakeside SouthPercopo West", arr9));
		ret.put("Percopo WestLakeside South", new path("Percopo WestLakeside South", arr9));
		
		int[] arr10 = {48,691,66,696,66,696,83,705,83,705,103,721,103,721,118,742,118,742,179,734,179,734,199,739,
				199,739,292,784,292,784,304,794,304,794,316,824,316,824,341,822,341,822,416,811,416,811,514,808};
		ret.put("Lakeside SouthPercopo South", new path("Lakeside SouthPercopo South", arr10));
		ret.put("Percopo SouthLakeside South", new path("Percopo SouthLakeside South", arr10));
		
		int[] arr11 = {48,691,6,672,6,672,8,563,8,563,126,455,126,455,148,474};
		ret.put("Lakeside SouthLakeside North", new path("Lakeside SouthLakeside North", arr11));
		ret.put("Lakeside NorthLakeside South", new path("Lakeside NorthLakeside South", arr11));
		
		int[] arr12 = {148,474,172,497,172,497,384,441,384,441,375,410};
		ret.put("ApartmentsLakeside North", new path("ApartmentsLakeside North", arr12));
		ret.put("Lakeside NorthApartments", new path("Lakeside NorthApartments", arr12));
		
		int[] arr13 = {375,410,492,377,492,377,507,412};
		ret.put("ApartmentsBlum", new path("ApartmentsBlum", arr13));
		ret.put("BlumApartments", new path("BlumApartments", arr13));
		
		int[] arr14 = {375,410,492,377,492,377,516,367,516,367,534,371};
		ret.put("ApartmentsScharp", new path("ApartmentsScharp", arr14));
		ret.put("ScharpApartments", new path("ScharpApartments", arr14));
		
		int[] arr15 = {375,410,383,444,383,444,430,474,430,474,451,510,451,510,416,529,416,529,353,581,353,581,279,626};
		ret.put("ApartmentsChapel North", new path("ApartmentsChapel North", arr15));
		ret.put("Chapel NorthApartments", new path("Chapel NorthApartments", arr15));
		
		int[] arr16 = {375,410,384,445,384,445,431,475,431,475,450,510,450,510,514,493,514,493,
				676,504,676,504,743,495,743,495,817,515};
		ret.put("ApartmentsUnion South", new path("ApartmentsUnion South", arr16));
		ret.put("Union SouthApartments", new path("Union SouthApartments", arr16));
		
		int[] arr17 = {512,729,562,728,562,728,642,706,642,706,666,679,666,679,835,597,835,597,
				817,515};
		ret.put("Percopo NorthUnion South", new path("Percopo NorthUnion South", arr17));
		ret.put("Union SouthPercopo North", new path("Union SouthPercopo North", arr17));
		
		int[] arr18 = {512,729,559,726,559,726,645,710,645,710,663,680,663,680,838,597,838,597,
				838,658,838,658,856,728,856,728,834,735};
		ret.put("Percopo NorthSpeed North", new path("Percopo NorthSpeed North", arr18));
		ret.put("Speed NorthPercopo North", new path("Speed NorthPercopo North", arr18));
		
		int[] arr19 = {279,626,354,583,354,583,418,524,418,524,493,496,493,496,512,492,512,492,538,494,
				538,494,563,475,563,475,565,456,565,456,558,438,558,438,530,440,530,440,507,412};
		ret.put("Chapel NorthBlum", new path("Chapel NorthBlum", arr19));
		ret.put("BlumChapel North", new path("BlumChapel North", arr19));
		
		int[] arr20 = {279,626,352,582,352,582,417,525,417,525,493,498,493,498,514,492,514,492,537,494,
				537,494,562,474,562,474,565,454,565,454,574,452,574,452,574,432,574,432,598,390};
		ret.put("Chapel NorthMees", new path("Chapel NorthMees", arr20));
		ret.put("MeesChapel North", new path("MeesChapel North", arr20));
		
		int[] arr21 = {279,626,349,582,349,582,419,528,419,528,494,496,494,496,510,494,510,494,678,503,
				678,503,742,495,742,495,817,515};
		ret.put("Chapel NorthUnion South", new path("Chapel NorthUnion South", arr21));
		ret.put("Chapel NorthUnion South", new path("Chapel NorthUnion South", arr21));
		
		int[] arr22 = {592,778,628,775,628,775,641,707,641,707,667,678,667,678,836,595,836,595,817,515};
		ret.put("Percopo EastUnion South", new path("Chapel NorthUnion South", arr22));
		ret.put("Chapel NorthUnion South", new path("Chapel NorthUnion South", arr22));

		// eric works here
		int[] arr23 = {512,729,559,726,559,726,641,707,641,707,663,679,663,679,836,598,836,598,838,668,838,668,856,729};
        ret.put("Percopo NorthSpeed North", new path("Percopo NorthSpeed North", arr23));
        ret.put("Speed NorthPercopo North", new path("Speed NorthPercopo North", arr23));
        
        int[] arr24 = {599,380,588,346,588,346,533,370};
        ret.put("ScharpMees", new path("ScharpMees", arr24));
        ret.put("MeesScharp", new path("MeesScharp", arr24));
        
        int[] arr25 = {533,370,515,367,515,367,491,376,491,376,507,412};
        ret.put("ScharpBlum", new path("ScharpBlum", arr25));
        ret.put("BlumScharp", new path("BlumScharp", arr25));
        
        int[] arr26 = {592,778,594,812,594,812,653,800,653,800,829,794,829,794,830,765};
        ret.put("Percopo EastSpeed South", new path("Percopo EastSpeed South", arr26));
        ret.put("Speed SouthPercopo East", new path("Speed SouthPercopo East", arr26));
        
        int[] arr27 = {856,729,862,758,862,758,830,765};
        ret.put("Speed NorthSpeed South", new path("Speed NorthSpeed South", arr27));
        ret.put("Speed SouthSpeed North", new path("Speed SouthSpeed North", arr27));
        
        int[] arr28 = {514,810,594,813,594,813,651,800,651,800,828,795,828,795,830,765};
        ret.put("Percopo SouthSpeed South", new path("Percopo SouthSpeed South", arr28));
        ret.put("Speed SouthPercopo South", new path("Speed SouthPercopo South", arr28));
		
        int[] arr29 = {598,390,605,379,605,379,638,373,638,373,654,380,654,380,662,407,662,407,700,394};
        ret.put("MeesUnion West", new path("MeesUnion West", arr29));
        ret.put("Union WestMees", new path("Union WestMees", arr29));
        
        int[] arr30 = {598,390,605,379,605,379,638,373,638,373,654,380,654,380,662,407,
        		662,407,681,427,681,427,690,458,690,458,708,478,708,478,723,487,723,487,741,494,741,494,818,514};
        ret.put("MeesUnion South", new path("MeesUnion South", arr30));
        ret.put("Union SouthMees", new path("Union SouthMees", arr30));
        
        int[] arr31 = {507,412,530,440,530,440,557,437,
        		557,437,565,457,565,457,561,497,561,497,676,504,676,504,743,495,743,495,818,514};
        ret.put("BlumUnion South", new path("BlumUnion South", arr31));
        ret.put("Union SouthBlum", new path("Union SouthBlum", arr31));
        
        int[] arr32 = {830,765,861,757,861,757,928,744,928,744,942,705,942,705,966,693,966,693,981,637};
        ret.put("Speed SouthBSB West", new path("Speed SouthBSB West", arr32));
        ret.put("BSB WestSpeed South", new path("BSB WestSpeed South", arr32));
        
        int[] arr33 = {856,729,861,757,861,757,928,744,928,744,942,705,942,705,966,693,966,693,981,637};
        ret.put("Speed NorthBSB West", new path("Speed NorthBSB West", arr33));
        ret.put("BSB WestSpeed North", new path("BSB WestSpeed North", arr33));
        
        int[] arr34 = {830,765,861,757,861,757,928,744,928,744,962,748,962,748,983,748,983,748,983,730};
        ret.put("Speed SouthBSB South", new path("Speed SouthBSB South", arr34));
        ret.put("BSB SouthSpeed South", new path("BSB SouthSpeed South", arr34));
        
        int[] arr35 = {856,729,861,757,861,757,928,744,928,744,962,748,962,748,983,748,983,748,983,730};
        ret.put("Speed NorthBSB South", new path("Speed NorthBSB South", arr35));
        ret.put("BSB SouthSpeed North", new path("BSB SouthSpeed North", arr35));
        
        int[] arr36 = {983,730,1021,636};
        ret.put("BSB NorthBSB South", new path("BSB NorthBSB South", arr36));
        ret.put("BSB SouthBSB North", new path("BSB SouthBSB North", arr36));
        
        int[] arr37 = {983,487,991,482,991,482,1005,489,1005,489,996,509,996,509,
        		978,498,978,498,943,498,943,498,850,517,850,517,817,515};
        ret.put("DemingUnion South", new path("DemingUnion South", arr37));
        ret.put("Union SouthDeming", new path("Union SouthDeming", arr37));
        
        int[] arr38 = {983,487,964,474,964,474,878,446};
        ret.put("DemingUnion East", new path("DemingUnion East", arr38));
        ret.put("Union EastDeming", new path("Union EastDeming", arr38));
        
        int[] arr39 = {983,487,991,482,991,482,1005,489,1005,489,996,509,996,509,1015,509,
        		1015,509,1045,527,1045,527,1066,529,1066,529,1054,539,1054,539,1047,565};
        ret.put("DemingOlin North", new path("DemingOlin North", arr39));
        ret.put("Olin NorthDeming", new path("Olin NorthDeming", arr39));
        // anand works here
        
        int[] arr40 = {983,730,983,748,983,748,929,745,929,745,941,705,941,705,966,694,966,694,981,637};
        ret.put("BSB SouthBSB West", new path("BSB SouthBSB West", arr40));
        ret.put("BSB WestBSB South", new path("BSB WestBSB South", arr40));
        
        int []arr41 = {698,391,698,331,817,326,909,354,879,389};
        ret.put("Union WestUnion North", new path("Union WestUnion North", arr41));
        ret.put("Union NorthUnion West", new path("Union NorthUnion West", arr41));

        int []arr42 = {699,391,661,405,678,424,690,456,623,485,749,495,817,514};
        ret.put("Union WestUnion South", new path("Union WestUnion South", arr42));
        ret.put("Union SouthUnion West", new path("Union SouthUnion West", arr42));
       
        int []arr43 = {879,390,878,445};
        ret.put("Union NorthUnion East", new path("Union NorthUnion East", arr43));
        ret.put("Union EastUnion North", new path("Union EastUnion North", arr43));
        
        int []arr44 = {1047,565,1010,557,1010,557,1002,567,1002,567,981,637};
        ret.put("Olin NorthBSB West", new path("Olin NorthBSB West", arr44));
        ret.put("BSB WestOlin North", new path("BSB WestOlin North", arr44));
        
        int []arr45 = {1047,565,1050,579,1050,579,1029,581,1029,581,1021,636};
        ret.put("Olin NorthBSB North", new path("Olin NorthBSB North", arr45));
        ret.put("BSB NorthOlin North", new path("BSB NorthOlin North", arr45));
        
        int []arr46 = {817,514,849,518,849,518,943,497,943,497,977,497,977,497,1017,
        		509,1017,509,1044,527,1044,527,1067,529,1067,529,1047,565};
        ret.put("Union SouthOlin North", new path("Union SouthOlin North", arr46));
        ret.put("Olin NorthUnion South", new path("Olin NorthUnion South", arr46));
        
        int []arr47 = {507,412,528,439,528,439,556,438,556,438,572,434,572,434,596,390};
        ret.put("BlumMees", new path("BlumMees", arr47));
        ret.put("MeesBlum", new path("MeesBlum", arr47));
        
        int []arr48 = {817,514,835,596,835,596,837,669,837,669,856,729};
        ret.put("Union SouthSpeed North", new path("Union SouthSpeed North", arr48));
        ret.put("Speed NorthUnion South", new path("Speed NorthUnion South", arr48));
        
        int []arr49 = {1047,565,1054,538,1054,538,1067,530,1067,530,
        		1045,526,1045,526,1016,510,1016,510,996,510,996,510,
        		983,517,983,517,906,543,906,543,834,597,834,597,665,
        		678,665,678,641,707,641,707,562,724,562,724,512,729};
        ret.put("Olin NorthPercopo North", new path("Olin NorthPercopo North", arr49));
        ret.put("Percopo NorthOlin North", new path("Percopo NorthOlin North", arr49));
        
        int []arr50 = {1047,565,1054,538,1054,538,1067,530,1067,530,
        		1045,526,1045,526,1016,510,1016,510,996,510,996,510,
        		983,517,983,517,906,543,906,543,834,597,834,597,665,
        		678,665,678,641,707,641,707,626,772,626,772,592,778};
        ret.put("Olin NorthPercopo East", new path("Olin NorthPercopo East", arr50));
        ret.put("Percopo EastOlin North", new path("Percopo EastOlin North", arr50));
        
        int []arr51 = {817,514,834,597,834,597,665,678,665,678,
        		641,707,641,707,562,724,562,724,512,729};
        ret.put("Union SouthPercopo North", new path("Union SouthPercopo North", arr51));
        ret.put("Percopo NorthUnion South", new path("Percopo NorthUnion South", arr51));
        
        int []arr52 = {817,514,834,597,834,597,665,678,665,678,
        		641,707,641,707,626,772,626,772,592,778};
        ret.put("Union SouthPercopo East", new path("Union SouthPercopo East", arr52));
        ret.put("Percopo EastUnion South", new path("Percopo EastUnion South", arr52));
        
        int []arr53 = {983,487,1005,489,1005,489,996,508,996,508,983,
        		517,983,517,910,540,910,540,834,597,834,597,665,678,665,
        		678,641,707,641,707,562,724,562,724,512,729};
        ret.put("DemingPercopo North", new path("DemingPercopo North", arr53));
        ret.put("Percopo NorthDeming", new path("Percopo NorthDeming", arr53));
        
        int []arr54 = {983,487,1005,489,1005,489,996,508,996,508,983,517,983,
        		517,910,540,910,540,834,597,834,597,665,678,665,678,
        		641,707,641,707,626,772,626,772,592,778};
        ret.put("DemingPercopo East", new path("DemingPercopo East", arr54));
        ret.put("Percopo EastDeming", new path("Percopo EastDeming", arr54));
        
        int []arr55 = {983,487,1005,489,1005,489,996,508,996,508,983,517,983,
        		517,910,540,910,540,834,597,834,597,837,668,837,668,856,729};
        ret.put("DemingSpeed North", new path("DemingSpeed North", arr55));
        ret.put("Speed NorthDeming", new path("Speed NorthDeming", arr55));
        
        int []arr56 = {561,497,614,655,614,655,642,706,
        		642,706,564,726,564,726,512,729};
        ret.put("Lake NorthPercopo North", new path("Lake NorthPercopo North", arr56));
        ret.put("Percopo NorthLake North", new path("Percopo NorthLake North", arr56));
        
        int []arr57 = {561,497,614,655,614,655,642,706,
        		642,706,628,773,628,773,592,778};
        ret.put("Lake NorthPercopo East", new path("Lake NorthPercopo East", arr57));
        ret.put("Percopo EastLake North", new path("Percopo EastLake North", arr57));
        
        int []arr58 = {561,497,563,457,563,457,557,438,
        		557,438,530,442,530,442,507,409};
        ret.put("Lake NorthBlum", new path("Lake NorthBlum", arr58));
        ret.put("BlumLake North", new path("BlumLake North", arr58));
        
        int []arr59 = {561,497,563,457,563,457,574,452,
        		574,452,573,435,573,435,597,389};
        ret.put("Lake NorthMees", new path("Lake NorthMees", arr59));
        ret.put("MeesLake North", new path("MeesLake North", arr59));
        
		return ret;
	}
}
