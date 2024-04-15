import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PortNumber = 9900;
    private static final String LOG_FILE = "./logs/chat_log.txt";

    public static void main(String[] args) {
        try {
            InetAddress localAddress = InetAddress.getLocalHost();
            System.out.println("Server is running on IP: " + localAddress.getHostAddress());
            ServerSocket serverSocket = new ServerSocket(PortNumber);
            System.out.println("Server is listening...");
            ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
            ExecutorService executor = Executors.newCachedThreadPool();
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientHandlers);
                clientHandlers.add(clientHandler);
                executor.execute(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void logMessage(String senderName, String message) {
        try (PrintWriter logWriter = new PrintWriter(new BufferedWriter(new FileWriter(LOG_FILE, true)))) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = sdf.format(new Date());
            logWriter.println(timestamp + " - " + senderName + ": " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
