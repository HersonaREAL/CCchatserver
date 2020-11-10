package buffer;

import operation.Message;
import operation.clientStreamSave;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class client_manager {
    private int client_count =0;
    private  final static int Thread_count=1000;
    private HashMap<String, Socket> client_map = new HashMap<>();//存用户信息的哈希表
    private HashMap<String, clientStreamSave>io_map=new HashMap<>();//存储用户Io流
    private Message notice = new Message();//通知
    ExecutorService pool = Executors.newFixedThreadPool(Thread_count); //线程池

    public boolean creat_client(Socket connection){
        if(client_count>=1000) //最多支持1000人聊天
            return false;

        try {
            //刚开始连接读取名字
            DataInputStream in =new DataInputStream(connection.getInputStream());
            String getName=in.readUTF();
            in.close();

            if(client_map.get(getName)!=null||getName.equals("GM")){
                NameErr(connection);//重名直接踢了
                return false;
            }


            //名字和套接字加入哈希表，客户数量+1,需要上锁，防止读写不一致
            synchronized (this){
                client_map.put(getName,connection);
                io_map.put(getName,new clientStreamSave(new ObjectInputStream(connection.getInputStream()),new ObjectOutputStream(connection.getOutputStream())));
                client_count++;
            }

            //新建线程
            Runnable task = new client_process(getName,connection,this,io_map.get(getName));
            pool.submit(task);

            //通知所有客户端有人加入
            String noticeMessage="\t\t有位👴加入群聊 名字:"+getName+"\n";
            System.out.println("用户 "+getName+" 已加入,套接字"+connection.getInetAddress()+":"+connection.getPort());
            sys_for_client(noticeMessage);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    void del_client(String Name){
        try {
        Socket exit_socket = client_map.get(Name);
        String exit_notice="\t\t有位👴退出群聊 名字:"+Name+"\n";
        synchronized (this){
            //从哈希表中去除
            client_map.remove(Name);
            io_map.remove(Name);
            client_count--;
        }

        System.out.println("用户 "+Name+" 已退出,套接字"+exit_socket.getInetAddress()+":"+exit_socket.getPort());

        exit_socket.close();//关闭套接字释放资源

        if(!sys_for_client(exit_notice))
            System.err.println("退出通知发送失败");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    boolean send_message_group(Message theMessage){
        //遍历哈希表中所有套接字
        for (String tmp : client_map.keySet()) {
            ObjectOutputStream sendStream = io_map.get(tmp).getOos();
            try {
                synchronized (this) {
                    //写流需要上锁
                    sendStream.writeObject(theMessage);
                    sendStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    boolean send_message_user(Message theMessage){
        //获取接受用户的流
        ObjectOutputStream sendStream = io_map.get(theMessage.getTheToUser()).getOos();
        try {
            synchronized (this){
            sendStream.writeObject(theMessage);
            sendStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(theMessage.getTheFromUser()+" 发送给 "+theMessage.getTheToUser()+" 的消息发送失败");
            return false;
        }
        return true;
    }

    boolean sys_for_client(String theMessage){
        //同步参数给客户端
        synchronized (this){
            //设置同步消息类
        notice.syncSend(theMessage,client_map,client_count);
        }
        return send_message_group(notice);
    }

    void NameErr(Socket connection){
        //名字错误处理
        try {
            ObjectOutputStream out=new ObjectOutputStream(connection.getOutputStream());
            synchronized (this){
            notice.p2pSend("GM",null,"\t\t名字已经被占用了！！！\n");
            out.writeObject(notice);
            out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
