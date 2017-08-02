package MySocket;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

class Handler implements Runnable{
    Socket socket;
    OutputStream out;
    InputStream in;

    Handler(){
        socket = new Socket();
        socket.connect(new InetSocketAddress(2055));
        out = socket.getOutputStream();
        in = socket.getInputStream();
    }

    public void Run(){
        
    }

}

public class Client {
    public static void main(String [] args){

        try{
            socket = new Socket();
            socket.connect(new InetSocketAddress(2055));
            out = socket.getOutputStream();
            in = socket.getInputStream();
        }

    }
}
