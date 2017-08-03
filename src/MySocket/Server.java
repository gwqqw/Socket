package MySocket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class Handler implements Runnable {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    public Handler(ServerSocket serverSocket) {
        try {
            this.socket = serverSocket.accept();
            in = socket.getInputStream();
            out = socket.getOutputStream();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Handler constructor throws exception.");
        }
    }

    private void DownloadFile()
    {
        DataInputStream din = null;
        FileOutputStream fos = null;
        try{
            System.out.println("Start to download file...");
            din = new DataInputStream(in);
            fos = new FileOutputStream(new File("D:\2_" + din.readUTF()));
            byte[] inputByte = new byte[1024];
            int length = 0;
            while (true){
                length = din.read(inputByte, 0, inputByte.length);
                fos.write(inputByte, 0, length);
                System.out.println(length + "bytes downloaded.");
                if (-1 == length)
                {
                    System.out.println("Finish to download file.");
                    oos.writeByte(2);
                    break;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Exception is thrown during download file.");
        }finally {
            try{
                if (null != din) din.close();
                if (null != fos) fos.close();
            }catch (IOException e){
                e.printStackTrace();
                System.out.println("Exception is thrown during clearing up DataInputStream and FileOutputStream.");
            }
        }
    }

    @Override
    public void run() {
        Socket socket = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            if (null != socket) {
                System.out.println(socket.getInetAddress() + ":" + socket.getPort() + " is connected");
                while (true) {
                    int type = ois.readByte();
                    if (1 == type) {
                        DownloadFile();
                    } else if (2 == type) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != oos) {
                    oos.close();
                }
                if (null != ois) {
                    ois.close();
                }
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
                try {
                    Thread curConnection = new Thread((new Handler(serverSocket)));
                    curConnection.start();

                } catch (Exception e) {
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
