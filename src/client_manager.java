import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class client_manager {
    public int client_count =0;
    private int Thread_count=1000;
    public HashMap<String, Socket> client_map = new HashMap<String, Socket>();
    ExecutorService pool = Executors.newFixedThreadPool(Thread_count);
    boolean creat_client(Socket connection){
        if(client_count>=1000)
            return false;
        try {
            //刚开始连接读取名字
            InputStream in = connection.getInputStream();
            int len = in.available();
            byte[]Getname=new byte[len];
            int readsucced=in.read(Getname);
            if(readsucced==0)
                return false;

            //新建线程
            Runnable task = new client_process(Getname.toString(),connection,this);
            pool.submit(task);

            //名字和套接字加入哈希表，客户数量+1
            client_map.put(Getname.toString(),connection);
            client_count++;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    void del_client(Socket connection){

    }
    boolean send_message(byte[] message){
        return true;
    }
}
