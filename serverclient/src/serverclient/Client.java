package serverclient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java Client");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
            Socket socket = new Socket(hostName, portNumber);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String serverMessage;
            String userAnswer;


            serverMessage = in.readLine();
            System.out.println("Server says: " + serverMessage);

            while (true) {

                String currentNumberMessage = in.readLine();
                System.out.println(currentNumberMessage);

                String mathQuestion = in.readLine();
                System.out.println("Server asks: " + mathQuestion);

                System.out.print("Enter your answer: ");
                userAnswer = userInput.readLine();
                out.println(userAnswer);

                serverMessage = in.readLine();
                System.out.println("Server says: " + serverMessage);

                if (serverMessage.toLowerCase().contains("play again")) {

                    System.out.print("Do you want to play again? (yes/no): ");
                    String playAgainResponse = userInput.readLine();

                    out.println(playAgainResponse.toLowerCase());

                    if ("no".equalsIgnoreCase(playAgainResponse)) {
                        break;
                    }
                } else {

                    break;
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }
}
