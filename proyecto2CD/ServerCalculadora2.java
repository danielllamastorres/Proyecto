package servercalculadora2;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerCalculadora2 {

    public static void main(String[] args) {
        ServerCalculadora2 server = new ServerCalculadora2(5001);
    }

    public ServerCalculadora2(int port) {
        try {
            ServerSocket ss = new ServerSocket(port);

            while (true) {
                Socket clientSocket = ss.accept();
                ServerThread thread = new ServerThread(clientSocket);
                thread.start();
                System.out.println(thread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ServerThread extends Thread {
        private Socket socket;

        public ServerThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                while (true) {
                    String input = dis.readUTF();
                    if (input.equals("bye"))
                        break;
                    System.out.println("Petición recibida: " + input);
                    double result = 0;

                    StringTokenizer st = new StringTokenizer(input);
                    double oprnd1 = Integer.parseInt(st.nextToken());
                    String operation = st.nextToken();
                    double oprnd2 = Integer.parseInt(st.nextToken());

                    // Calculator Operation Perform By Server
                    if (operation.equals("+")) {
                        result = oprnd1 + oprnd2;
                    } else if (operation.equals("-")) {
                        result = oprnd1 - oprnd2;
                    } else if (operation.equals("/")) {
                        result = oprnd1 / oprnd2;
                    } else if (operation.equals("*")) {
                        result = oprnd1 * oprnd2;
                    }
                    System.out.println("Enviando el resultado: " + result);

                    String stringresult = Double.toString(result);
                    dos.writeUTF(stringresult);
                }

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


 
}