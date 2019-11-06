import java.io.*; 
import java.net.*;
import java.util.*;
	
class ChatClient { 
	public static void main(String args[]) throws Exception {
		Socket clientSocket = new Socket("177.81.46.238", 81);

		ThreadSent ts = new ThreadSent(clientSocket);
		ThreadReceive tr = new ThreadReceive(clientSocket);

		ts.start();
		tr.start();
	}
}  

class ThreadSent extends Thread {

	Socket clientSocket;

	public ThreadSent(Socket s) {
		clientSocket = s;
	}

	public void run() {

		try {
			String sentence;
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

			while (true) {
				sentence = inFromUser.readLine();

				if (sentence.equals("close")) {
					clientSocket.close();
					break;
				}

				outToServer.writeBytes(sentence + '\n');
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}

class ThreadReceive extends Thread {

	Socket clientSocket;

	public ThreadReceive(Socket s) {
		clientSocket = s;
	}

	public void run() {

		try {
			String modifiedSentence;
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			while (true) {
				modifiedSentence = inFromServer.readLine(); ;
				System.out.println("FROM SERVER: " + modifiedSentence); 
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
