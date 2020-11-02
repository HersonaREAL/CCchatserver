import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class test_client {
    public static void main(String[] args) {
        String hostname = args.length>0?args[0]:"localhost";
        Socket socket = null;
        try {
            while(true){
            socket = new Socket(hostname,23333);
            //socket.setSoTimeout(15000);

           // Writer out = new OutputStreamWriter(socket.getOutputStream(),"utf-8");
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("👴傲天");
            out.flush();
            }
            //out.close();
//            int i=0;
//            while (i<1000){
//                out.writeUTF("测试聊天"+i);
//                out.flush();
//                i++;
//            }
//            DataInputStream in =new DataInputStream(socket.getInputStream());
//            while(true){
//                String message = in.readUTF();
//                System.out.print(message);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
