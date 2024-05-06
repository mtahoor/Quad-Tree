import java.io.*;

public class ImgQuadTreeFileCreator {
    public static void main(String[] args) {
        // Create a BufferedReader to read input from the console
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            // Prompt the user to enter the input image file name
            System.out.println("Enter the name of the input image file:");
            String inputFile = reader.readLine().trim();

            // Prompt the user to enter the output quadtree file name
            System.out.println("Enter the name of the output quadtree file:");
            String outputFile = reader.readLine().trim();

            // Read the uncompressed image file
            try (BufferedReader inputFileReader = new BufferedReader(new FileReader(inputFile))) {
                // Create a 2D array to store the image data
                int[][] image = new int[256][256];
                // Populate the image array with intensity values from the input file
                for (int row = 0; row < 256; row++) {
                    for (int col = 0; col < 256; col++) {
                        String line = inputFileReader.readLine();
                        if (line == null) {
                            throw new IOException("Unexpected end of file.");
                        }
                        int intensity = Integer.parseInt(line.trim());
                        image[row][col] = intensity;
                    }
                }

                // Write quadtree data to the output file
                try (BufferedWriter outputFileWriter = new BufferedWriter(new FileWriter(outputFile))) {
                    writeQuadTree(outputFileWriter, image, 0, 0, 256);
                }
            }

            System.out.println("Quadtree data file created successfully: " + outputFile);
        } catch (IOException e) {
            // Handle any IO exceptions that may occur
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Method to write quadtree data recursively to the output file
    private static void writeQuadTree(BufferedWriter writer, int[][] image, int x, int y, int size) throws IOException {
        if (size == 1) {
            // If the current block is a single pixel, write its intensity to the output file
            writer.write(image[x][y] + "\n");
            return;
        }

        // Check if the current block is a leaf (all pixels have the same intensity)
        boolean isLeaf = true;
        int intensity = image[x][y];
        for (int i = x; i < x + size; i++) {
            for (int j = y; j < y + size; j++) {
                if (image[i][j] != intensity) {
                    isLeaf = false;
                    break;
                }
            }
            if (!isLeaf) {
                break;
            }
        }

        if (isLeaf) {
            // If the block is a leaf, write its intensity to the output file
            writer.write(intensity + "\n");
        } else {
            // If the block is not a leaf, write -1 to indicate a non-leaf node
            writer.write("-1\n");
            // Recursively write quadtree data for each quadrant of the current block
            int halfSize = size / 2;
            writeQuadTree(writer, image, x, y, halfSize);
            writeQuadTree(writer, image, x, y + halfSize, halfSize);
            writeQuadTree(writer, image, x + halfSize, y, halfSize);
            writeQuadTree(writer, image, x + halfSize, y + halfSize, halfSize);
        }
    }
}
