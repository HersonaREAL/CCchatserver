import buffer.Message;

import java.io.*;
import java.net.Socket;

public class test_client {
    public static void main(String[] args) {
        String hostname = args.length>0?args[0]:"localhost";
        Socket socket = null;
        try {
            socket = new Socket(hostname,23333);

            ObjectOutputStream c = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream a =new ObjectInputStream(socket.getInputStream());
            c.writeUTF("傲天");
            c.flush();

            Message k = new Message();
            k.groupSend("TEST","傲天");
            for(int i=0;i<10;i++){
                c.writeObject(k);
                c.flush();
            }

            while (true){
                Message b = (Message) a.readObject();
                System.out.println(b.getTheMessage());
            }

        } catch (Exception e) {
            System.err.println("EEEEEE");
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
