import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleChatServerGUI extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private PrintWriter out;

    public SingleChatServerGUI() {
        // Set up the GUI window
        setTitle("Server");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create and configure the chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Create the bottom panel with message field and send button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        bottomPanel.add(messageField, BorderLayout.CENTER);

        JButton sendButton = new JButton("Send");
        // Add ActionListener to the send button
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        bottomPanel.add(sendButton, BorderLayout.EAST);

        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);

        try {
            // Set up the server socket
            ServerSocket serverSocket = new ServerSocket(12345);
            Socket clientSocket = serverSocket.accept();
            chatArea.append("Client connected: " + clientSocket + "\n");

            // Set up PrintWriter for sending messages to the client
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Read messages from the client and display them in the chat area
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                chatArea.append("Client: " + inputLine + "\n");
            }

            // Close resources
            in.close();
            clientSocket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to send a message to the client
    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            out.println(message);
            chatArea.append("Server: " + message + "\n");
            messageField.setText("");
        }
    }

    // Main method to launch the server GUI
    public static void main(String[] args) {
        new SingleChatServerGUI();
    }
}
