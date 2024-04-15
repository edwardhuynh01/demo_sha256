import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private String clientName;
    private String clientHashPwd;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<ClientHandler> clientHandlers;
    public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> clientHandlers) {
        this.clientSocket = clientSocket;
        this.clientHandlers = clientHandlers;
    }
    private static boolean isValid(String receivedHash, String name) throws Exception {
        if (FileHelper.addPasswordHashIfNotExists(receivedHash, name, FileHelper.FILE_PATH)) {
            return true;
        }
        return false;
    }
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            clientName = in.readLine();
            System.out.println("Client name: " + clientName);
            while (true) {
                clientHashPwd = in.readLine();
                System.out.println("Client hash password: " + clientHashPwd);
                if (isValid(clientHashPwd, clientName)) {
                    out.println("VALID");
                    //System.out.println("valid");
                    break;
                } else {
                    out.println("INVALID");
                    //System.out.println("invalid");
                }
            }
            while (true) {
                String message = in.readLine();
                //
                if (message == null) {
                    break;
                }
                if (message.equalsIgnoreCase("exit")) {
                    broadcastMessage(clientName + " has exited the chat.");
                    System.out.println(clientName + " has exited the chat.");
                    break;
                }
                Server.logMessage(clientName, message);
                broadcastMessage(clientName + ":" + message);
            }
            clientHandlers.remove(this);
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void broadcastMessage(String message) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.sendMessage(message);
        }
    }
    public void sendMessage(String message) {
        out.println(message);
    }
}
