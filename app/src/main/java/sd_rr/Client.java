package sd_rr;
import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Bem-vindo! Use os comandos:");
            System.out.println("REGISTER <username> <password> - para registrar");
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
}
