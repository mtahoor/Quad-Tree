import java.io.*;

public class ImgQuadTree {
    private QTNode root;

    public ImgQuadTree(String filename) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
            if (inputStream == null) {
                throw new FileNotFoundException("File not found: " + filename);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                root = buildQuadTree(reader);
            }
        } catch (IOException e) {
            System.err.println("Error reading quadtree file: " + filename);
            e.printStackTrace();
            System.exit(1);
        }
    }

    private QTNode buildQuadTree(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        int value = Integer.parseInt(line.trim());
        QTNode node = new QTNode(value);

        if (value == -1) {
            node.children = new QTNode[4];
            for (int i = 0; i < 4; i++) {
                node.children[i] = buildQuadTree(reader);
            }
        }

        return node;
    }

    public int getNumNodes() {
        return countNodes(root);
    }

    private int countNodes(QTNode node) {
        if (node == null) {
            return 0;
        }

        int count = 1;
        if (node.children != null) {
            for (QTNode child : node.children) {
                count += countNodes(child);
            }
        }

        return count;
    }

    public int getNumLeaves() {
        return countLeaves(root);
    }

    private int countLeaves(QTNode node) {
        if (node == null) {
            return 0;
        }

        if (node.intensity != -1) {
            return 1;
        }

        int leafCount = 0;
        if (node.children != null) {
            for (QTNode child : node.children) {
                leafCount += countLeaves(child);
            }
        }

        return leafCount;
    }

    public int[][] getImageArray() {
        int[][] image = new int[256][256];
        fillImageArray(root, 0, 0, 256, image);
        return image;
    }

    private void fillImageArray(QTNode node, int x, int y, int size, int[][] image) {
        if (node == null) {
            return;
        }

        if (node.intensity != -1) {
            for (int i = x; i < x + size; i++) {
                for (int j = y; j < y + size; j++) {
                    image[i][j] = node.intensity;
                }
            }
        } else {
            int halfSize = size / 2;
            fillImageArray(node.children[0], x, y, halfSize, image);
            fillImageArray(node.children[1], x, y + halfSize, halfSize, image);
            fillImageArray(node.children[2], x + halfSize, y, halfSize, image);
            fillImageArray(node.children[3], x + halfSize, y + halfSize, halfSize, image);
        }
    }

    private class QTNode {
        int intensity;
        QTNode[] children;

        public QTNode(int intensity) {
            this.intensity = intensity;
        }
    }
}