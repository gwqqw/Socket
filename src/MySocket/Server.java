package MySocket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class ServerHandler implements Runnable {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    public ServerHandler(Socket socket) {
        try {
            this.socket = socket;
            in = socket.getInputStream();
            out = socket.getOutputStream();
            oos = new ObjectOutputStream(out);
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
            din = new DataInputStream(in);
            String filename = din.readUTF();
            fos = new FileOutputStream(new File("D:2_" + filename));
            System.out.println("Start to download file " + filename);
            long fileLength = din.readLong();
            long currentReadLength = 0;
            byte[] inputByte = new byte[1024];
            int length = 0;
            while (true){
                length = din.read(inputByte, 0, inputByte.length);
                fos.write(inputByte, 0, length);
                System.out.println(length + "bytes downloaded.");
                currentReadLength += length;
                if (-1 == length || currentReadLength >= fileLength)
                {
                    System.out.println("Finish to download file.");
                    oos.writeByte(2);
                    oos.flush();
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Exception is thrown during download file.");
        }finally {
            try{
                if (null != din) din.close();
                if (null != fos) fos.close();
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Exception is thrown during clearing up DataInputStream and FileOutputStream.");
            }
        }
    }

    @Override
    public void run() {
            DownloadFile();
        }
}

public class Server {

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(30000);
            socket = serverSocket.accept();
            System.out.println(socket.getInetAddress().getHostName() +  " is connected.");
            //while (true) {
            try {
                Thread curConnection = new Thread((new ServerHandler(socket)));
                curConnection.start();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
            //}
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

//    public static void main(String[] args) {
//        ServerSocket serverSocket = null;
//        Socket socket = null;
//        ObjectInputStream ois = null;
//        ObjectOutputStream oos = null;
//        try {
//            serverSocket = new ServerSocket(2055);
//            socket = serverSocket.accept();
//            InputStream in = socket.getInputStream();;
//            OutputStream out = socket.getOutputStream();
//            ois = new ObjectInputStream(in);
//            oos = new ObjectOutputStream(out);
//            byte flag = ois.readByte();
//            if (1 == flag)
//                DownloadFile(in, out, oos);
//            while (true){
//                flag = ois.readByte();
//                if (2 == flag)   break;
//            }
//        }catch (IOException e){
//            e.printStackTrace();
//        }finally {
//            try {
//                if (null != ois) ois.close();
//                if (null != oos) oos.close();
//                if (null != socket) socket.close();
//                if (null != serverSocket) serverSocket.close();
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//   }

    private static void DownloadFile(InputStream in, OutputStream out, ObjectOutputStream oos )
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
                fos.flush();
                System.out.println(length + "bytes downloaded.");
                if (-1 == length)
                {
                    System.out.println("Finish to download file.");
                    oos.writeByte(2);
                    oos.flush();
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
}
