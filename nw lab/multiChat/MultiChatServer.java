import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class MultiChatServer extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private Map<Integer, PrintWriter> clientWriters = new ConcurrentHashMap<>();
    private int clientIdCounter = 1;

    public MultiChatServer() {
        setTitle("Multi-Chat Server");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        bottomPanel.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessageToClient();
            }
        });
        bottomPanel.add(sendButton, BorderLayout.EAST);

        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            appendToChatArea("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                appendToChatArea("New client connected: " + clientSocket);

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                clientWriters.put(clientIdCounter, out);

                Thread clientThread = new Thread(new ClientHandler(clientSocket, out, clientIdCounter));
                clientThread.start();

                clientIdCounter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendToChatArea(String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter writer;
        private Scanner in;
        private int clientId;

        public ClientHandler(Socket socket, PrintWriter writer, int clientId) {
            this.socket = socket;
            this.writer = writer;
            this.clientId = clientId;
        }

        @Override
        public void run() {
            try {
                in = new Scanner(socket.getInputStream());

                while (in.hasNextLine()) {
                    String message = in.nextLine();
                    appendToChatArea("Client " + clientId + ": " + message);

                    // Handle messages from clients here
                    handleClientMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    socket.close();
                    clientWriters.remove(clientId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleClientMessage(String message) {
            // Handle messages from clients here (e.g., store in a database, perform
            // actions, etc.)
            // For example, you can append the message to a file or save it in a database.
            // This is where you can implement custom logic based on the client's message.
        }
    }

    private void sendMessageToClient() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            int selectedClientId = Integer.parseInt(JOptionPane.showInputDialog("Enter Client ID to send the message:"));
            PrintWriter clientWriter = clientWriters.get(selectedClientId);
            if (clientWriter != null) {
                clientWriter.println("Server: " + message);
                appendToChatArea("Server: " + message);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Client ID!");
            }
            messageField.setText("");
        }
    }

    public static void main(String[] args) {
        MultiChatServer server = new MultiChatServer();
        server.setVisible(true);
        server.startServer();
    }
}