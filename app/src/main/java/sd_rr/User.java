package sd_rr;

import java.io.*;
import java.net.*;

public class User {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 12345;
    private String nome;
    private String password; // Armazenar a senha
    private PermissaoTypeEnum permissao;

    public User( String nome, String password, PermissaoTypeEnum permissao) {
        this.nome = nome;
        this.password = password;
        this.permissao = permissao;
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Bem-vindo! Use os comandos:");
            System.out.println("REGISTER <username> <password> <permission_level> - para registrar");
            System.out.println("LOGIN <username> <password> - para fazer login");
            System.out.println("Digite 'sair' para encerrar.");

            String command;
            while (!(command = console.readLine()).equalsIgnoreCase("sair")) {
                out.println(command);
                String response = in.readLine();
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters e Setters

    public String getNome() {
        return nome;
    }

    public String getPassword() {
        return password;
    }

    public PermissaoTypeEnum getPermissao() {
        return permissao;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPermissao(PermissaoTypeEnum permissao) {
        this.permissao = permissao;
    }
}
