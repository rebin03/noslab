import java.io.*;
import java.net.*;

class Server {
    public static void main(String[] args) {
        try {
            // Step 1: Create a server socket on port 1346
            ServerSocket serversocket = new ServerSocket(1346);
            System.out.println("Waiting for request....");

            // Step 2: Wait for a client to connect; accept the connection
            Socket socket = serversocket.accept();
            System.out.println("Request Accepted...");

            // Step 3: Create an output stream to send data to the client
            PrintStream ps = new PrintStream(socket.getOutputStream());

            // Step 4: Create a buffered reader to read data from the server's console
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            // Step 5: Prompt the server operator to input data
            System.out.println("Input the data at the server...");

            // Read a line of input from the server's console and send it to the client
            ps.print(br.readLine());

            // Step 6: Close the socket and server socket once communication is done
            socket.close();
            serversocket.close();
        } catch (IOException e) {
            // Handle IOException, print an error message if an exception occurs
            System.out.println("Error: " + e.getMessage());
        }
    }
}
