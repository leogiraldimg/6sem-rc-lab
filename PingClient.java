import java.io.*;
import java.net.*;
import java.util.*;

/*
 * Cliente para enviar as requisições de Ping sobre UDP.
 */

public class PingClient {
    private static final double LOSS_RATE = 0.3;
    private static final int AVERAGE_DELAY = 100; //milliseconds
    private static final int NUM_PACKAGES = 10;
    private static final int TIMEOUT = 1000;

    public static void main(String[]args) throws Exception {
        // Obter o argumento da linha de comando.
        if (args.length !=2) {
            System.out.println("Required arguments: host, port");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        // Criando socket de datagrama para receber e enviar pacotes UDP através da porta especificada na linha de comando.
        DatagramSocket socket = new DatagramSocket();

        int contPackages = 0;
        while (contPackages < 10) {

            //Envio do pacote
            InetAddress clientHost = InetAddress.getByName(host);
            int clientPort = port;
            String bufMessage = "Pacote: " + contPackages;
            byte[]buf = bufMessage.getBytes();
            DatagramPacket message = new DatagramPacket(buf, buf.length, clientHost, clientPort);
            socket.send(message);

            System.out.println("Pacote enviado: " + contPackages);

            //Tratando espera pelo ack
            socket.setSoTimeout(TIMEOUT);
            DatagramPacket getAck = new DatagramPacket(new byte[1024],1024);
            try {
                socket.receive(getAck);
                contPackages = contPackages + 1;
            }
            catch (SocketTimeoutException e) {
                System.out.println("Não recebido ack do pacote: " + contPackages);
                contPackages = contPackages + 1;
                continue;
            }
        }
    }
}