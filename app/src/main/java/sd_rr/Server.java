package sd_rr;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private static final Map<String, String> users = new HashMap<>(); // Armazena username -> password

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado na porta " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String request;
                while ((request = in.readLine()) != null) {
                    String[] parts = request.split(" ");
                    String command = parts[0];

                    if ("REGISTER".equalsIgnoreCase(command)) {
                        String username = parts[1];
                        String password = parts[2];
                        String response = registerUser(username, password);
                        out.println(response);
                    } else if ("LOGIN".equalsIgnoreCase(command)) {
                        String username = parts[1];
                        String password = parts[2];
                        String response = authenticateUser(username, password);
                        out.println(response);
                    } else {
                        out.println("Comando inválido.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String registerUser(String username, String password) {
            synchronized (users) {
                if (users.containsKey(username)) {
                    return "Erro: Nome de usuário já existe.";
                }
                users.put(username, password);
                return "Usuário registrado com sucesso!";
            }
        }

        private String authenticateUser(String username, String password) {
            synchronized (users) {
                if (users.containsKey(username) && users.get(username).equals(password)) {
                    return "Login bem-sucedido!";
                }
                return "Falha na autenticação. Verifique suas credenciais.";
            }
        }
    }
}
