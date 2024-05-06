import javax.swing.*;
import java.awt.*;
import java.util.*;

public class ImgQuadTreeDisplayer extends JPanel {
	private int imageArray[][];	 	// each cell holds an intensity value between 0 and 255

	public ImgQuadTreeDisplayer(String filename) {

		// Create the quadtree based on the data from the file
		ImgQuadTree q=new ImgQuadTree(filename);
		
		// Output information about the ImgQuadTree
		System.out.println("ImgQuadTree Loaded:");
		System.out.println("Number of nodes: " + q.getNumNodes());
		System.out.println("Number of leaves: " + q.getNumLeaves());

		// Create the original image based on the ImgQuadTree
		imageArray = q.getImageArray();

		// Display the image
		setPreferredSize(new Dimension(256,256));
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(this);
		f.pack();
		f.setVisible(true);
	}

	public void paintComponent(Graphics g){
		for (int row = 0; row < 256; row++){
			for (int col = 0; col < 256; col++){
				int c = imageArray[row][col];
				Color color = new Color(c,c,c);
				g.setColor(color);
				g.fillRect(col,row,1,1);
			}
		}
	}

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Please input the name of the quad tree file to load:");
		String filename = scan.nextLine();
		new ImgQuadTreeDisplayer(filename);
		scan.close();
	}

}

