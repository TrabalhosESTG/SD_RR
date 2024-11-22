package sd_rr.Server;

import sd_rr.PermissaoTypeEnum;
import sd_rr.User;

import java.io.*;
import java.net.*;
import java.util.*;

public class server {
    private static final int PORT = 12345;
    private static final List<User> users = new ArrayList<>(); // Lista de usuários
    private static final List<chat> chats = new ArrayList<>(); // Lista de chats

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado na porta " + PORT);

            // Criar e adicionar 3 chats, um para cada nível de permissão
            chats.add(new chat(1, "chat1", "chat de nível alto", PermissaoTypeEnum.ALTO));
            chats.add(new chat(2, "chat2", "chat de nível médio", PermissaoTypeEnum.MEDIO));
            chats.add(new chat(3, "chat3", "chat de nível baixo", PermissaoTypeEnum.BAIXO));

            while (true) {
                Socket userSocket = serverSocket.accept();
                System.out.println("User conectado: " + userSocket.getInetAddress());
                new UserHandler(userSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class UserHandler extends Thread {
        private final Socket userSocket;

        public UserHandler(Socket socket) {
            this.userSocket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(userSocket.getOutputStream(), true)) {

                String request;
                while ((request = in.readLine()) != null) {
                    String[] parts = request.split(" ");
                    String command = parts[0];

                    switch (command.toUpperCase()) {
                        case "REGISTER": {
                            String username = parts[1];
                            String password = parts[2];
                            PermissaoTypeEnum level = PermissaoTypeEnum.valueOf(parts[3].toUpperCase());
                            String response = registerUser(username, password, level);
                            out.println(response);
                            break;
                        }
                        case "LOGIN": {
                            String username = parts[1];
                            String password = parts[2];
                            String response = authenticateUser(username, password);
                            out.println(response);
                            break;
                        }
                        case "LISTCHATS": {
                            String response = listChats();
                            out.println(response);
                            break;
                        }
                        case "JOINCHAT": {
                            String username = parts[1];
                            int chatId = Integer.parseInt(parts[2]);
                            String response = joinChat(username, chatId);
                            out.println(response);
                            break;
                        }
                        default:
                            out.println("Comando inválido.");
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String registerUser(String username, String password, PermissaoTypeEnum level) {
            synchronized (users) {
                for (User user : users) {
                    if (user.getNome().equals(username)) {
                        return "Erro: Nome de usuário já existe.";
                    }
                }
                users.add(new User(username, password, level));
                return "Usuário registrado com sucesso!";
            }
        }

        private String authenticateUser(String username, String password) {
            synchronized (users) {
                for (User user : users) {
                    if (user.getNome().equals(username) && user.getPassword().equals(password)) {
                        return "Login bem-sucedido! Permissão: " + user.getPermissao();
                    }
                }
                return "Falha na autenticação. Verifique suas credenciais.";
            }
        }

        private String listChats() {
            StringBuilder response = new StringBuilder("Chats disponíveis:\n");
            synchronized (chats) {
                for (chat c : chats) {
                    response.append("ID: ").append(c.getId())
                            .append(", Nome: ").append(c.getNome())
                            .append(", Nível: ").append(c.getPermissao())
                            .append("\n");
                }
            }
            return response.toString();
        }

        private String joinChat(String username, int chatId) {
            User user = null;
            synchronized (users) {
                for (User u : users) {
                    if (u.getNome().equals(username)) {
                        user = u;
                        break;
                    }
                }
            }

            if (user == null) {
                return "Erro: Usuário não encontrado. Faça login antes de entrar no chat.";
            }

            synchronized (chats) {
                for (chat c : chats) {
                    if (c.getId() == chatId) {
                        if (c.getPermissao() == user.getPermissao()) {
                            c.addUser(user);
                            return "Usuário " + username + " entrou no chat " + c.getNome();
                        } else {
                            return "Erro: Você não tem permissão para entrar neste chat.";
                        }
                    }
                }
            }
            return "Erro: Chat não encontrado.";
        }
    }
}
