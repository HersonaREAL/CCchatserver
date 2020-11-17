
import java.io.*;
import java.net.Socket;

public class test_client {
    public static void main(String[] args) {
        String hostname = args.length>0?args[0]:"localhost";
        Socket socket=null;
        int i=0;
        while(true){
            try {
                Socket a=new Socket(hostname,23333);
                ObjectOutputStream out=new ObjectOutputStream(a.getOutputStream());
                out.writeUTF(String.valueOf(i++));
                out.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
/*        Socket socket = null;
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
            Message f=new Message();
            f.groupSend("SADKJFHSAKJLFDSHJK","傲天");
            for(int i=0;i<10;i++){
                c.writeObject(f);
                c.flush();
            }

            while (true){
                Message b = (Message) a.readObject();
                System.out.println(b.getTheMessage());
            }*/

        }


}
