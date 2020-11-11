import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public Client() {
        try {
            System.out.println("Socket is starting up...");
            socket = new Socket("localhost", 8888);
            System.out.println(socket);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            AtomicBoolean isDrop = new AtomicBoolean(true);

            new Thread(() -> {
                try {
                    Scanner scanner = new Scanner(System.in);
                    while (true) {
                        if (isDrop.get()) {
                            System.out.println("Please type in a message: ");
                        }
                        out.writeUTF(new SimpleDateFormat("HH:mm:ss").format(new Date())+" ClientMesage: "+scanner.nextLine());
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
        Client socket = new Client();
    }
}