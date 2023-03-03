// A Java program for a Server

import java.io.*;
import java.net.*;
import java.util.*;
 
public class ServerCalculadora {

    private Socket socket = null;
 

    public ServerCalculadora(int port)
    {
        try {

            ServerSocket ss = new ServerSocket(port);
            Socket s = ss.accept();
 

            DataInputStream dis
                = new DataInputStream(s.getInputStream());
            DataOutputStream dos
                = new DataOutputStream(s.getOutputStream());
 
            while (true) {
                String input = dis.readUTF();
                if (input.equals("bye"))
                    break;
                System.out.println("Petición recibida: "+ input);
                double result = 0;
 
                StringTokenizer st
                    = new StringTokenizer(input);
                double oprnd1
                    = Integer.parseInt(st.nextToken());
                String operation = st.nextToken();
                double oprnd2
                    = Integer.parseInt(st.nextToken());
 
                // Calculator Operation Perform By Server
                if (operation.equals("+")) {
                    result = oprnd1 + oprnd2;
                }
                else if (operation.equals("-")) {
                    result = oprnd1 - oprnd2;
                }
                else if (operation.equals("/")) {
                    result = oprnd1 / oprnd2;
                }
                else if (operation.equals("*")) {
                    result = oprnd1 * oprnd2;
                }
                System.out.println("Enviando el resultado: " + result);
                dos.writeUTF(Double.toString(result));
            }
        }
        catch (Exception e) {
            System.out.println("Error");
        }
    }
 
    public static void main(String args[])
    {

        ServerCalculadora server = new ServerCalculadora(5000);
    }
}