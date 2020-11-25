
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class test_client  {


    public static void main(String[] args) {
        //String hostname = args.length>0?args[0]:"localhost";
        String hostname = "zj_laptop";
        Socket socket = null;
        int i = 0;
        try {
            Socket a = new Socket(hostname, 20015);
            DataOutputStream out = new DataOutputStream(a.getOutputStream());
            Scanner ins = new Scanner(System.in);
            System.out.println("input name");
            if (ins.hasNextLine())
                out.writeUTF(ins.nextLine());
            out.flush();

            Runnable sb = new readN(a);
            new Thread(sb).start();

            System.err.println("connect,PLZ input");

            while (ins.hasNextLine()) {
                out.writeUTF(ins.nextLine());
                out.flush();
            }
        } catch (
                Exception ioException) {
            ioException.printStackTrace();
        }
    }


}

class readN implements Runnable{
    Socket a;
    readN(Socket a){
        this.a=a;
    }
    @Override
    public void run() {
        System.err.println("in read thread");
        try {
            DataInputStream in1 = new DataInputStream(a.getInputStream());
            while (true) {
                String a = in1.readUTF();
                System.err.println(a);
            }
        } catch (IOException e) {
            System.err.println("err");
            e.printStackTrace();
        }
    }
}

