import java.net.*;
import java.io.*;
import java.util.*;

class UDPServer {
    public static void main(String[] args) throws Exception {
        try {
            DatagramSocket ds = new DatagramSocket(2334);
            byte[] buff = new byte[1024];
            Scanner scan = new Scanner(System.in);
            while (true) {
                DatagramPacket dpreceive = new DatagramPacket(buff, buff.length);
                ds.receive(dpreceive);
                String str = new String(dpreceive.getData(), 0, dpreceive.getLength());
                System.out.println(str);
                if (str.equals("bye")) {
                    System.out.println("Server Is Exiting .... BYE");
                    break;
                }
                buff = new byte[1024];
                String str1 = scan.nextLine();
                InetAddress ip = InetAddress.getByName("localhost");
                DatagramPacket dp = new DatagramPacket(str1.getBytes(), str1.length(), ip, 6000);
                ds.send(dp);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        }
    }
}