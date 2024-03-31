package client;

import server.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

public class Client {
    public static void main(String[] args) {

        try {
            Socket serverSocket = new Socket("localhost", Server.PORT);
            System.out.println("Connected to the Server: tcp://localhost:" + Server.PORT);

            // send id to the server
            new PrintWriter(serverSocket.getOutputStream(), true).println(UUID.randomUUID());


            Scanner serverIn = new Scanner(serverSocket.getInputStream());

            // read welcome message from the server
            String input = serverIn.nextLine();
            System.out.println(input);

            // read list of all clients
            input = serverIn.nextLine();
            if (!input.isEmpty()) {
                System.out.println(input);
                boolean isEndOfString = false;
                while (!isEndOfString) {
                    input = serverIn.nextLine();
                    if (input.contains("%%")) {
                        isEndOfString = true;
                        input = input.substring(0, input.length() - 2);
                    }
                    System.out.println(input);
                }
            }

            new Thread(new ServerReader(serverSocket)).start();
            new Thread(new ServerWriter(serverSocket)).start();

        } catch (IOException e) {
            throw new RuntimeException("Failed to connect to Server: " + e.getMessage(), e);
        }

    }

}


class ServerWriter implements Runnable {
    private final Socket serverSocket;
    private long id;

    public ServerWriter(Socket serverSocket) {
        this.serverSocket = serverSocket;


    }

    @Override
    public void run() {
        Scanner consoleReader = new Scanner(System.in); // not necessary to close manually as it's a standard input stream => Java will close it itself
        try (PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true)) {
            while (true) {
                String msgFromConsole = consoleReader.nextLine();
                out.println(msgFromConsole);

                if (msgFromConsole.replace(" ", "").equalsIgnoreCase("/exit")) {
                    System.out.println("Disconnecting ...");
                    break;
                }

            }
        } catch (IOException e) {
            System.err.println("Error when writing to the Server: " + e.getMessage());
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error when disconnecting from the Server: " + e.getMessage());
        }

    }

}

class ServerReader implements Runnable {
    private final Socket serverSocket;

    public ServerReader(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try (Scanner in = new Scanner(serverSocket.getInputStream())) {
            while (in.hasNext()) {
                String input = in.nextLine();
                System.out.println(input);
            }
        } catch (IOException e) {
            System.err.println("Error when reading from the Server: " + e.getMessage());
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error when disconnecting from the Server: " + e.getMessage());
        }
    }
}