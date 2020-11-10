package buffer;

import operation.Message;
import operation.clientStreamSave;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class client_process implements Runnable {
    private final String Name;
    private final Socket connection;
    private final client_manager MyBoss;
    private final clientStreamSave streamSave;

    client_process(String Name, Socket connection, client_manager MyBoss,clientStreamSave streamSave) {
        this.Name = Name;
        this.connection = connection;
        this.MyBoss = MyBoss;
        this.streamSave=streamSave;
    }

    @Override
    public void run() {
        while (true){
            try {
                Message message = (Message)streamSave.getOis().readObject();
                if(message.getTheType()==0){
                    //群聊
                    String finalMessage = process_message(message.getTheMessage(), message.getTheType());
                    message.groupSend(finalMessage,message.getTheFromUser());
                    MyBoss.send_message_group(message);//通知BOSS群发
                }else if(message.getTheType()==1){
                    //私发
                    String finalMessage = process_message(message.getTheMessage(), message.getTheType());
                    message.p2pSend(message.getTheFromUser(),message.getTheToUser(),finalMessage);
                    MyBoss.send_message_user(message); //通知BOSS私发
                }

            } catch (IOException e) {
                e.printStackTrace();
                MyBoss.del_client(Name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("流中获取对象失败");
                MyBoss.del_client(Name);
            }
        }

    }

    String process_message(String message,int type) {
        //给信息拼接上发送人名字和时间,再作发送
        Date t = new Date();
        SimpleDateFormat df = new SimpleDateFormat(" (yyyy-MM-dd HH:mm:ss)\n○ ");
        if (type==0)
            //群聊处理
            return Name+df.format(t)+message+"\n"; //按格式拼接名字和时间
        else
            //私聊处理
            return Name+df.format(t)+"(私聊)"+message+"\n";
    }

}
