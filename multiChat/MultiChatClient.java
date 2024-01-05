import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MultiChatClient extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private PrintWriter out;
	private JButton sendbutton;
	
    public MultiChatClient() {
        setTitle("Client");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());
	
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        messageField = new JTextField();
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(messageField, BorderLayout.CENTER);
		
		
		sendbutton=new JButton("send");
		
		
		
		sendbutton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e){
				sendMessage();
			}
		});
		bottomPanel.add(sendbutton, BorderLayout.EAST);

        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		
    }

    public void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            Scanner in = new Scanner(socket.getInputStream());
            while (in.hasNextLine()) {
                String message = in.nextLine();
                appendToChatArea(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        appendToChatArea("Me: " + message);
        out.println(message);
        messageField.setText("");
    }

    private void appendToChatArea(String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        MultiChatClient client = new MultiChatClient();
        client.setVisible(true);
        client.connectToServer();
    }
}