import java.io.*;
import java.net.*;

class Client {
    public static void main(String[] args) {
        try {
            // Step 1: Create a socket and attempt to connect to the server on localhost and
            // port 1346
            Socket socket = new Socket("localhost", 1346);
            System.out.println("Connected Successfully.....");

            // Step 2: Create a buffered reader to read data from the server
            BufferedReader bs = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Response from Server.....");

            // Step 3: Read the response from the server and print it on the client side
            System.out.println("Client Side : " + bs.readLine());

            // Step 4: Close the socket once communication is done
            socket.close();

        } catch (UnknownHostException e) {
            // Handle UnknownHostException, print an error message if the host is not found
            System.out.println("Error: IP not found for " + e);

        } catch (IOException e) {
            // Handle IOException, print an error message if an exception occurs
            System.out.println("Error: " + e.getMessage());
        }
    }
}
