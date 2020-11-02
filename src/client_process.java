import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class client_process implements Runnable {
    private final String Name;
    private final Socket connection;
    private final client_manager MyBoss;

    client_process(String Name, Socket connection, client_manager MyBoss) {
        this.Name = Name;
        this.connection = connection;
        this.MyBoss = MyBoss;
    }

    @Override
    public void run() {
        try(DataInputStream in =new DataInputStream(connection.getInputStream())) {

            while (true) {
                //轮询接受信息

                String message = in.readUTF();
                if(!MyBoss.send_message(process_message(message)))
                    System.err.println(Name+"的信息发送失败！");
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.err.println(Name+" 已丢失连接");
            MyBoss.del_client(connection);
        }

    }

    String process_message(String message) {
        //给信息拼接上发送人名字和时间,再作发送
        Date t = new Date();
        SimpleDateFormat df = new SimpleDateFormat(" (yyyy-MM-dd HH:mm:ss)\n○ ");
        String Name_Date=Name.concat(df.format(t)); //按格式拼接名字和时间
        return  Name_Date.concat(message).concat("\n"); //拼接信息
    }

}
