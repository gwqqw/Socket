package MySocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {

            serverSocket = new ServerSocket(2055);
            while (true) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    System.out.println(socket.getInetAddress() + ":" + socket.getPort() + " is connected");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (null == socket) socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != serverSocket) serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
