import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SingleChatClientGUI extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private PrintWriter out;

    public SingleChatClientGUI() {
        // Set up the GUI window
        setTitle("Client");
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
            // Connect to the server
            Socket socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Read messages from the server and display them in the chat area
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                chatArea.append("Server: " + inputLine + "\n");
            }

            // Close resources
            in.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to send a message to the server
    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            out.println(message);
            chatArea.append("Client: " + message + "\n");
            messageField.setText("");
        }
    }

    // Main method to launch the client GUI
    public static void main(String[] args) {
        new SingleChatClientGUI();
    }
}
