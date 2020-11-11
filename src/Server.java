import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;

    public Server(){
        try {
            System.out.println("Server is a start... ");
            serverSocket = new ServerSocket(8888);

            System.out.println("waiting for the client");
            clientSocket = serverSocket.accept();

            System.out.println("Client connected: " + clientSocket);

            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            AtomicBoolean isDrop = new AtomicBoolean(true);

            new Thread(() -> {
                try {
                    Scanner scanner = new Scanner(System.in);
                    while (true) {
                        if (isDrop.get()) {
                            System.out.println("Please type in a message: ");
                        }
                        out.writeUTF(new SimpleDateFormat("HH:mm:ss").format(new Date())+" ServerMesage: "+scanner.nextLine());
                        isDrop.set(false);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            while (true) {
                String incomingMessage = in.readUTF();
                System.out.println("__________________________");
                System.out.println(incomingMessage);
                if (isDrop.get()) {
                    System.out.println("Please type in a message: ");
                }
                isDrop.set(true);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}