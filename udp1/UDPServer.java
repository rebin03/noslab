import java.net.*;
import java.io.*;

public class UDPServer {
    public static void main(String args[]) {

        DatagramSocket ds = null;
        if (args.length < 1) {
            System.out.println("Usage: java UDPServer ");
            System.exit(1);
        }

        try {
            int socket_no = Integer.valueOf(args[0]).intValue();
            ds = new DatagramSocket(socket_no);
            byte[] buffer = new byte[2000];

            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                ds.receive(request);

                System.out.println("Received: " + new String(request.getData()));

                DatagramPacket reply = new DatagramPacket(
                        request.getData(),
                        request.getLength(),
                        request.getAddress(),
                        request.getPort());
                ds.send(reply);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
            if (ds != null)
                ds.close();
        }
    }
}
