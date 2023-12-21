package server;

import java.net.*;
import java.io.*;

public class MathServer {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		if(args.length != 1)
		{
			System.err.println("Usage: java MathServer <port number>");
			System.exit(1);
		}
		
		int portNumber = Integer.parseInt(args[0]);
		try(
				ServerSocket serverSocket = new ServerSocket(portNumber);
				Socket clientSocket = serverSocket.accept();
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		){
			
			String inputLine, outputLine;
			
			MathProtocol mp = new MathProtocol();
			outputLine = mp.processInput(null);
			out.println(outputLine);
			
			while((inputLine = in.readLine()) != null)
			{
				outputLine = mp.processInput(inputLine);
				out.println(outputLine);
				if(outputLine.equals("Server terminate"))
				{
					break;
				}
			}
			
		} catch (IOException e) 
		{ 
			System.out.println("Exception caught when trying to listen on port + portNumber + or listening for a connection");
			System.out.println(e.getMessage());
		}
	}

}
