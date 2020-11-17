import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class client_process implements Runnable {
    private final String Name;
    private final Socket connection;
    private final client_manager MyBoss;
    private final clientStreamSave streamSave;

    client_process(String Name, Socket connection, client_manager MyBoss, clientStreamSave streamSave) {
        this.Name = Name;
        this.connection = connection;
        this.MyBoss = MyBoss;
        this.streamSave=streamSave;
    }

    @Override
    public void run() {
        while (true){
            try {
                //读取客户端序列化后的对象
                Message message = (Message)streamSave.getOis().readObject();

                //判断消息类型
                if(message.getTheType()==0)
                    Group_Send(message);//群聊
                else if(message.getTheType()==1)
                   Private_Send(message); //私发


            } catch (IOException e) {
                //e.printStackTrace();
                System.err.println(Name+" 已丢失连接 ");
                MyBoss.del_client(Name);
                return;
            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
                System.err.println("流中获取对象失败,已丢失连接 名字"+Name);
                MyBoss.del_client(Name);
                return;
            }
        }

    }

    void Group_Send(Message message){
        //加工一下消息
        String finalMessage = process_message(message.getTheMessage(), message.getTheType(),null);

        //设置好sendMessage准备发送
        Message sendMessage=new Message();
        sendMessage.p2pSend(message.getTheFromUser(),message.getTheToUser(),finalMessage);

        //通知BOSS群发
        if(!MyBoss.send_message_group(sendMessage))
            System.err.println("用户 "+Name+"的群发消息发送失败");
    }

    void Private_Send(Message message){
        //加工一下消息
        String finalMessage = process_message(message.getTheMessage(), message.getTheType(),message.getTheToUser());

        //设置好snedMessage准备发送
        Message sendMessage=new Message();
        sendMessage.p2pSend(message.getTheFromUser(),message.getTheToUser(),finalMessage);

        //通知BOSS私发
        if(!MyBoss.send_message_user(sendMessage))
            System.err.println("用户 "+Name+" 私发给 "+message.getTheToUser()+ "的消息发送失败");
    }
    String process_message(String message,int type,String toUser) {
        //给信息拼接上发送人名字和时间,再作发送
        Date t = new Date();
        SimpleDateFormat df = new SimpleDateFormat(" (yyyy-MM-dd HH:mm:ss) ");
        if (type==0)
            //群聊处理
            return Name+df.format(t)+"\n★ "+message+"\n"; //按格式拼接名字和时间
        else
            //私聊处理
            return Name+df.format(t)+"("+"私聊给 " +toUser+")\n💢 "+message+"\n";
    }

}
