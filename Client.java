import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception{
        try {
            final int PortNumber = 9900;
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter Server IP Address: ");
            String serverIP = inputReader.readLine();
            //String serverIP = "192.168.214.1";
            Socket clientSocket = new Socket(serverIP, PortNumber);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            System.out.print("Enter your name: ");
            String clientName = inputReader.readLine();
            out.println(clientName);
            while (true) {
                System.out.print("Enter your password: ");
                String passwordHash = SHA256Hasher.hashPassword(inputReader.readLine().trim() + clientName);
                //String passwordHash = inputReader.readLine().trim();
                out.println(passwordHash);
                String response = in.readLine();
                if ("VALID".equals(response)) {
                    break;
                } else {
                    System.out.println("Invalid password, please try again.");
                }
            }
            System.out.println("===================================");
            Thread readThread = new Thread(() -> {
                try {
                    while (true) {
                        String response = in.readLine();
                        String[] parts = response.split(":");
                        String hashed = SHA256Hasher.hashPassword(parts[2].trim());
                        String p = parts[1].trim();
                        if (hashed.equals(p)) {
                            System.out.println("Hashed recieve..:" + p);
                            System.out.println("Hashed encrypted:" + hashed);
                            System.out.println(parts[2]);
                        } else {
                            System.out.println("Hashed recieve..: " + p);
                            System.out.println("Hashed encrypted:" + hashed);
                            System.out.println("Message error!!!");
                        }
                        if (response == null) break;
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            readThread.start();
            while (true) {
                String message = inputReader.readLine();
                String originalMessage = message;
                String tamperedMessage = MessageIntercepter.tamperwithMessage(originalMessage);
                out.println(SHA256Hasher.hashPassword(message) + ":" + tamperedMessage);
                //out.println(SHA256Hasher.hashPassword(message) + ":" + originalMessage);
                //System.out.println(SHA256Hasher.hashPassword(message) + ":" + tamperedMessage);
                //out.println(SHA256Hasher.hashPassword(message) + ":" + message);
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
            }
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

