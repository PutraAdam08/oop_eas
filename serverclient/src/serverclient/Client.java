package serverclient;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8085);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            String serverMessage = in.readLine();
            System.out.println("Server says: " + serverMessage);

            while (true) {

                String currentNumberMessage = in.readLine();
                System.out.println(currentNumberMessage);

                String mathQuestion = in.readLine();
                System.out.println("Server asks: " + mathQuestion);

                System.out.print("Enter your answer: ");
                int answer = Integer.parseInt(userInput.readLine()); // For simplicity, assume the answer is always an integer.
                out.println(answer);


                String serverResponse = in.readLine();
                System.out.println("Server says: " + serverResponse);

                if (serverResponse.toLowerCase().contains("play again")) {

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

            userInput.close();
            in.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
