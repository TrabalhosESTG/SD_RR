package sd_rr;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final Map<String, String> users; // Mapa compartilhado para armazenar usuários

    public ClientHandler(Socket socket, Map<String, String> users) {
        this.clientSocket = socket;
        this.users = users;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String request;
            while ((request = in.readLine()) != null) {
                String[] parts = request.split(" ");
                String command = parts[0].toUpperCase();

                switch (command) {
                    case "REGISTER":
                        if (parts.length == 3) {
                            String username = parts[1];
                            String password = parts[2];
                            String response = registerUser(username, password);
                            out.println(response);
                        } else {
                            out.println("Erro: Comando REGISTER requer 2 argumentos (username e password).");
                        }
                        break;

                    case "LOGIN":
                        if (parts.length == 3) {
                            String username = parts[1];
                            String password = parts[2];
                            String response = authenticateUser(username, password);
                            out.println(response);
                        } else {
                            out.println("Erro: Comando LOGIN requer 2 argumentos (username e password).");
                        }
                        break;

                    default:
                        out.println("Erro: Comando inválido. Use REGISTER ou LOGIN.");
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao lidar com o cliente: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Erro ao fechar a conexão com o cliente: " + e.getMessage());
            }
        }
    }

    private synchronized String registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return "Erro: Nome de usuário já registrado.";
        }
        users.put(username, password);
        return "Usuário registrado com sucesso!";
    }

    private synchronized String authenticateUser(String username, String password) {
        if (users.containsKey(username) && users.get(username).equals(password)) {
            return "Login bem-sucedido! Bem-vindo, " + username + "!";
        }
        return "Erro: Nome de usuário ou senha inválidos.";
    }
}
