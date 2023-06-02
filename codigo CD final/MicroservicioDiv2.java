
import java.io.*;
import java.net.*;
import java.util.*;

public class MicroservicioDiv2 {


    public static void main(String[] args) {
        MicroservicioDiv2 server = new MicroservicioDiv2(5008);
    }

    public MicroservicioDiv2(int port) {
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
                    
                    double result = 0;

                    StringTokenizer st = new StringTokenizer(input);
                    double oprnd1 = Integer.parseInt(st.nextToken());
                    String operation = st.nextToken();
                    double oprnd2 = Integer.parseInt(st.nextToken());

                    // Calculator Operation Perform By Server
                    if (operation.equals("/")) {
                        System.out.println("Petición de division recibida: " + input);
                        result = oprnd1 / oprnd2;
                    } else{
                        
                        System.out.println("No recibo esto: " + input);
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