import java.net.*;
import java.io.*;

public class UDPServer {
    public static void main(String args[]) {
        // Section 1: Initialize DatagramSocket and check for command-line argument
        DatagramSocket ds = null;
        if (args.length < 1) {
            System.out.println("Usage: java UDPServer ");
            System.exit(1);
        }

        try {
            // Section 2: Create DatagramSocket and buffer for data
            int socket_no = Integer.valueOf(args[0]).intValue();
            ds = new DatagramSocket(socket_no);
            byte[] buffer = new byte[2000];

            // Section 3: Continuous loop for receiving and sending UDP packets
            while (true) {
                // Section 4: Receive UDP packet from client
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                ds.receive(request);

                // Section 5: Print the received message from client
                System.out.println("Received: " + new String(request.getData()));

                // Section 6: Prepare and send a reply to the client
                DatagramPacket reply = new DatagramPacket(
                        request.getData(),
                        request.getLength(),
                        request.getAddress(),
                        request.getPort());
                ds.send(reply);
            }
        } catch (SocketException e) {
            // Section 7: Handle SocketException
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            // Section 8: Handle IOException
            System.out.println("IO:" + e.getMessage());
        } finally {
            // Section 9: Close the DatagramSocket in the finally block
            if (ds != null)
                ds.close();
        }
    }
}
