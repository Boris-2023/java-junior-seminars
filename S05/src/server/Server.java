package server;
//0. Разобрать код, написанный на уроке. Прийти к осознанию, что все написанное - понятно.
//1. Сформулировать вопросы, которые остались без ответа, и прислать их в форму сдачи дз.
//2. Досдать долги по курсу (т.е. работы, которые еще не сданы).
//3.** Дореализовать проект:
//3.1 Доработать "отключение клиента" - отсылать всем уведомление о том, что клиент отключился
//3.2 Разобраться с префиксами сообщений: навести порядок в консоли (чтобы все было аккуратно и понятно) - на усмотрение студент
//4.**** Реализовать "системные" вызовы со стороны клиента:
//4.1 /all - получить список всех текущих пользователей
//4.2 /exit - отключиться (старый exit удалить)

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Server {
    public static final int PORT = 8181;

    public static void main(String[] args) {

        final Map<String, ClientThread> clients = new HashMap<>();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port: " + PORT);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();

                    PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
                    Scanner clientIn = new Scanner(clientSocket.getInputStream());

                    // gets client's id
                    String clientId = clientIn.nextLine();
                    clientOut.println("You are connected to the Server with Id = " + clientId);

                    // .getLocalPort() = port on the Server (PORT), .getPort() - port of the Client (random)
                    System.out.println("New client linked to the Server: " + clientSocket.getPort() + ", Id = " + clientId);

                    // creating new thread for particular client socket
                    ClientThread clientThread = new ClientThread(clientSocket, clients, clientId);

                    //  announcing all
                    for (ClientThread client : clients.values()) {
                        client.send("New client "
                                + clientSocket.getPort() + " has linked in, id: "
                                + clientId);
                    }

                    // list of all clients available
                    if (clients.size() > 0) {
                        clientOut.println("List of all clients available: \n" + clientThread.makeAllClientsToSend(clientId));
                    } else {
                        clientOut.println("");
                    }

                    // saving new client into clients map
                    clients.put(clientId, clientThread);

                    // starts the thread of the client
                    clientThread.start();


                } catch (IOException e) { // try(Socket ..)
                    System.out.println("Error during accepting connection: " + e.getMessage());
                    break;
                }
            } // while(true)

        } catch (IOException e) {
            throw new RuntimeException("Failed to start Server on port " + PORT, e);
        }
    }

}

// class to describe a link for any particular client
class ClientThread extends Thread {

    private final Socket clientSocket;
    private final PrintWriter out;
    private final Map<String, ClientThread> clients;
    private final String clientId;

    public ClientThread(Socket clientSocket, Map<String, ClientThread> clients, String clientId) throws IOException {
        this.clientSocket = clientSocket;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.clients = clients;
        this.clientId = clientId;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        try (Scanner in = new Scanner(clientSocket.getInputStream())) {

            while (true) {
                if (clientSocket.isClosed()) {
                    System.out.println("Client " + clientId + " disconnected!");
                    clients.remove(clientId);
                    break;
                }

                String input = in.nextLine();

                // process exit request by Client
                if (input.replace(" ", "").equalsIgnoreCase("/exit")) {
                    System.out.println("Client " + clientId + " disconnected!");
                    clients.remove(clientId);
                    if (clients.size() > 0) {
                        clients.entrySet()
                                .forEach(x -> x.getValue().send("Server: Client Id = " + clientId + " has disconnected!"));
                    }
                    break;

                } else {
                    System.out.println("New message from Client " + clientId + ": " + input);
                    out.println("Server: message [" + input + "] received!"); // server acknowledgement

                    // to particular client - addressee id starts with @
                    String toClientId = null;

                    if (input.startsWith("@")) {
                        String[] parts = input.split("\\s+"); // '\\s+' = space
                        if (parts.length > 0) {
                            toClientId = parts[0].substring(1);
                        }
                    }

                    if (toClientId == null) { //to all clients
                        clients.entrySet().stream()
                                .filter(x -> !Objects.equals(x.getKey(), clientId))
                                .forEach(x -> x.getValue().send(clientId + " -> all: " + input));
                    } else {
                        ClientThread toClient = clients.get(toClientId);
                        if (toClient != null) {
                            toClient.send(clientId + " -> you: " + input.replace("@" + toClientId + " ", ""));
                        } else {
                            out.println("Error! No such client, id = " + toClientId);
                        }
                    }

                } // else - not /exit

            } // while (true)

        } catch (IOException e) {
            System.err.println("Error during interaction with client " + clientId + ": " + e.getMessage());
        }

        try {
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error when disconnecting client " + clientId + ": " + e.getMessage());
        }
    }

    void send(String message) {
        out.println(message);
    }

    String makeAllClientsToSend(String fromClientId) {
        return clients.entrySet().stream()
                .filter(x -> (!Objects.equals(x.getKey(), fromClientId)))
                .map(x -> "id = " + x.getKey() + ", client = " + x.getValue().getClientSocket().getPort())
                .collect(Collectors.joining("\n")) + "%%";
    }

}

