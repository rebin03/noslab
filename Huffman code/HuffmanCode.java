import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

class Node {
    Character ch;
    Integer freq;
    Node left = null;
    Node right = null;

    Node(Character ch, Integer freq) {
        this.ch = ch;
        this.freq = freq;
    }

    // Constructor for internal nodes
    public Node(Character ch, Integer freq, Node left, Node right) {
        this.ch = ch;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }
}

public class HuffmanCode {
    // Build the Huffman tree and display encoded/decoded strings
    public static void createHuffmanTree(String text) {
        // Check for empty or null input
        if (text == null || text.length() == 0) {
            return;
        }

        // Calculate character frequencies
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : text.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }

        // Priority queue to store nodes based on frequency
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(l -> l.freq));

        // Populate priority queue with leaf nodes
        for (var entry : freq.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }

        // Build the Huffman tree by merging nodes
        while (pq.size() != 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            int sum = left.freq + right.freq;
            pq.add(new Node(null, sum, left, right));
        }

        // Get the root of the Huffman tree
        Node root = pq.peek();

        // Create a map to store Huffman codes for each character
        Map<Character, String> huffmanCode = new HashMap<>();

        // Generate Huffman codes for each character
        encodeData(root, "", huffmanCode);

        // Display Huffman codes
        System.out.println("Huffman Codes of the characters are: " + huffmanCode);
        System.out.println("The initial string is: " + text);

        // Encode the input string using Huffman codes
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append(huffmanCode.get(c));
        }
        System.out.println("The encoded string is: " + sb);

        // Decode the encoded string and display the result
        System.out.print("The decoded string is: ");
        if (isLeaf(root)) {
            while (root.freq-- > 0) {
                System.out.print(root.ch);
            }
        } else {
            int index = -1;
            while (index < sb.length() - 1) {
                index = decodeData(root, index, sb);
            }
        }
    }

    // Recursive method to generate Huffman codes for each character
    public static void encodeData(Node root, String str, Map<Character, String> huffmanCode) {
        if (root == null) {
            return;
        }
        if (isLeaf(root)) {
            // Store Huffman code for leaf node character
            huffmanCode.put(root.ch, str.length() > 0 ? str : "1");
        }
        // Recursively traverse left and right branches
        encodeData(root.left, str + '0', huffmanCode);
        encodeData(root.right, str + '1', huffmanCode);
    }

    // Recursive method to decode the encoded string
    public static int decodeData(Node root, int index, StringBuilder sb) {
        if (root == null) {
            return index;
        }
        if (isLeaf(root)) {
            // Print the character for leaf node
            System.out.print(root.ch);
            return index;
        }
        // Move to the left or right branch based on the bit value
        index++;
        root = (sb.charAt(index) == '0') ? root.left : root.right;
        index = decodeData(root, index, sb);
        return index;
    }

    // Check if a node is a leaf in the Huffman tree
    public static boolean isLeaf(Node root) {
        return root.left == null && root.right == null;
    }

    // Main method to take input and initiate Huffman coding
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the string to perform Huffman coding: ");
        String text = scanner.nextLine();
        scanner.close();
        createHuffmanTree(text);
    }
}
