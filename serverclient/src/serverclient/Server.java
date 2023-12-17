package serverclient;
import java.io.*;
import java.net.*;
import java.util.Random;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8085);
            System.out.println("Server is listening...");

            while (true) {
               
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                out.println("Do you want to play? (yes/no)");
                String playResponse = in.readLine();

                if ("no".equalsIgnoreCase(playResponse)) {

                    System.out.println("Client chose not to play. Server terminated.");
                    break;
                } else if ("yes".equalsIgnoreCase(playResponse)) {

                    Random random = new Random();
                    int randomNumber = random.nextInt(10) + 1;

                    while (randomNumber > 0) {
                        out.println("Your current number: " + randomNumber);
                        out.println("What is " + randomNumber + " - 1?");
                        String clientAnswer = in.readLine();

                        if (Integer.toString(randomNumber - 1).equals(clientAnswer.trim())) {

                            randomNumber--;


                            out.println("Correct! Do you want to play again? (yes/no)");
                            String playAgainResponse = in.readLine();

                            if ("no".equalsIgnoreCase(playAgainResponse)) {

                                System.out.println("Client chose not to play again. Server terminated.");
                                break;
                            }
                        } else {
 
                            randomNumber--;

                          
                            out.println("Wrong answer! Try again.");
                        }
                    }

                    if (randomNumber == 0) {
                        out.println("Congratulations! You reached zero. Game over.");
                    }
                }


                in.close();
                out.close();
                clientSocket.close();
            }

            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
