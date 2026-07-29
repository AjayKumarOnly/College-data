import java.io.*;
import java.net.*;

public class ChatClient {

    public static void main(String[] args) throws Exception {

        BufferedReader keyboard =
                new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter Your Name : ");
        String name = keyboard.readLine();

        // Same PC
        Socket socket = new Socket("localhost", 5000);

        // Different PCs
        // Socket socket = new Socket("192.168.1.100",5000);

        BufferedReader in =
                new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

        PrintWriter out =
                new PrintWriter(socket.getOutputStream(), true);

        // Receive Messages
        Thread receive = new Thread(() -> {

            try {

                String msg;

                while ((msg = in.readLine()) != null) {

                    System.out.println(msg);

                }

            } catch (Exception e) {
            }

        });

        receive.start();

        // Send Messages
        String message;

        while ((message = keyboard.readLine()) != null) {

            out.println(name + " : " + message);

        }

    }

}


import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    static Vector<ClientHandler> clients = new Vector<>();

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server Started...");

        while (true) {

            Socket socket = serverSocket.accept();
            System.out.println("New Client Connected");

            ClientHandler client = new ClientHandler(socket);
            clients.add(client);

            client.start();
        }
    }

    static class ClientHandler extends Thread {

        Socket socket;
        BufferedReader in;
        PrintWriter out;

        ClientHandler(Socket socket) throws Exception {
            this.socket = socket;

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream(), true);
        }

        public void run() {

            try {

                String message;

                while ((message = in.readLine()) != null) {

                    System.out.println(message);

                    for (ClientHandler client : clients) {
                        client.out.println(message);
                    }

                }

            } catch (Exception e) {

                System.out.println("Client Disconnected");
                clients.remove(this);

            }

        }

    }

}
