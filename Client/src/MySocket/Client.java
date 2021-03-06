package MySocket;


import javax.swing.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

class ClientHandler implements Runnable{
    Socket socket;
    OutputStream out;
    InputStream in;

    ClientHandler(){
        try {
//            FileSelection fileSelection = new FileSelection();
//            fileSelection.pack();
//            fileSelection.setVisible(true);
//            String filePath = fileSelection.
            socket = new Socket();
            socket.connect(new InetSocketAddress(2055));
            out = socket.getOutputStream();
            in = socket.getInputStream();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void run(){

        JFileChooser fileChooser = null;
        DataOutputStream dout = null;
        DataInputStream din = null;
        FileInputStream fin = null;
        File file = null;
        byte[] sendByte;
        int length = 0;
        try {
            fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(true);
            fileChooser.setVisible(true);
            fileChooser.showDialog(null, "Selection a file to upload");
            String strFilePath = fileChooser.getSelectedFile().getAbsolutePath();

            dout = new DataOutputStream(socket.getOutputStream());
            din = new DataInputStream(socket.getInputStream());
            file = new File(strFilePath);
            fin = new FileInputStream(file);
            sendByte = new byte[1024];
            System.out.println("Start to upload file " + strFilePath);
            dout.writeUTF(strFilePath);
            while ((length = fin.read(sendByte, 0, sendByte.length)) > 0){
                dout.write(sendByte,0, length);
                dout.flush();
            }
            dout.writeByte(2);
            while (true){
                byte receive = din.readByte();
                if (2 == receive)
                {
                    break;
                }
            }
            System.out.println("Finish uploading file.");

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (null != dout) dout.close();
                if (null != fin) fin.close();
                if (null != socket) socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}

public class Client {
    public static void main(String [] args){
        Socket socket = null;
        OutputStream out = null;
        InputStream in = null;

        JFileChooser fileChooser = null;
        DataOutputStream dout = null;
        DataInputStream din = null;
        FileInputStream fin = null;
        File file = null;
        byte[] sendByte;
        int length = 0;
        try{
            fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(true);
            fileChooser.setVisible(true);
            fileChooser.showDialog(null, "Selection a file to upload");
            String strFilePath = fileChooser.getSelectedFile().getAbsolutePath();
            String strFileName = fileChooser.getSelectedFile().getName();

            socket = new Socket();
            socket.connect(new InetSocketAddress(30000));

            System.out.println(socket.getInetAddress().getHostName() + " connects to server...");
            out = socket.getOutputStream();

            dout = new DataOutputStream(socket.getOutputStream());
            file = new File(strFilePath);
            fin = new FileInputStream(file);
            sendByte = new byte[1024];
            System.out.println("Start to upload file " + strFilePath);
            dout.writeUTF(strFileName);
            dout.flush();
            dout.writeLong(file.length());
            dout.flush();
            System.out.println("Send file length " + file.length());
            while ((length = fin.read(sendByte, 0, sendByte.length)) > 0){
                dout.write(sendByte,0, length);
                dout.flush();
                System.out.println("Send file length " + length);
            }
            System.out.println("Send file finished.");
            din = new DataInputStream(socket.getInputStream());
            while (true){
                byte receive = din.readByte();
                if (2 == receive)
                {
                    System.out.println("Get complish message from server.");
                    break;
                }
            }
            System.out.println("Finish uploading file.");

        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }

    }
}
