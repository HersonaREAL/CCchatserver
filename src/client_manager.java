import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class client_manager {
    public int client_count =0;
    private  final static int Thread_count=1000;
    public HashMap<Socket, String> client_map = new HashMap<>();//存用户信息的哈希表
    ExecutorService pool = Executors.newFixedThreadPool(Thread_count); //线程池

    boolean creat_client(Socket connection){
        if(client_count>=1000) //最多支持1000人聊天
            return false;

        try {
            //刚开始连接读取名字
            DataInputStream in =new DataInputStream(connection.getInputStream());
            String Getname=in.readUTF();


            //新建线程
            Runnable task = new client_process(Getname,connection,this);
            pool.submit(task);

            //名字和套接字加入哈希表，客户数量+1,需要上锁，防止读写不一致
            synchronized (this){
                client_map.put(connection,Getname);
                client_count++;
            }

            //通知所有客户端有人加入
            String enter_notice="\t\t有位👴加入群聊 名字:".concat(Getname).concat("\n");
            System.out.println("用户 "+Getname+" 已加入,套接字"+connection.getInetAddress()+":"+connection.getPort());
            if(!send_message(enter_notice))
                System.err.println("加入通知发送失败");

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    void del_client(Socket connection){
        try {
        String exit_name = client_map.get(connection);
        String exit_notice="\t\t有位👴退出群聊 名字:".concat(exit_name).concat("\n");
        synchronized (this){
            //从哈希表中去除
            client_map.remove(connection);
            client_count--;
        }
        System.out.println("用户 "+exit_name+" 已退出,套接字"+connection.getInetAddress()+":"+connection.getPort());

        connection.close();//关闭套接字释放资源

        if(!send_message(exit_notice))
            System.err.println("退出通知发送失败");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    boolean send_message(String message){
        //遍历哈希表中所有套接字
        for (Socket tmp : client_map.keySet()) {
            try {
                synchronized (this) {
                    //写流需要上锁
                    DataOutputStream out = new DataOutputStream(tmp.getOutputStream());
                    out.writeUTF(message);
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
