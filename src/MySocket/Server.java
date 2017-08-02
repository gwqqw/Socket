package MySocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Handler implements Runnable {
    private Socket socket;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            if (null != socket) {
                System.out.println(socket.getInetAddress() + ":" + socket.getPort() + " is connected");
                //TODO:
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != socket) {
                    System.out.println("Close connection " + socket.getInetAddress() + ":" + socket.getPort());
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Server {

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {

            serverSocket = new ServerSocket(2055);
            while (true) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    Thread curConnection = new Thread((new Handler(socket)));
                    curConnection.start();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
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
