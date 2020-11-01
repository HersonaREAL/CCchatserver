import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class client_process implements Runnable {
    private String Name;
    private Socket connection;
    private client_manager MyBoss;

    client_process(String Name, Socket connection, client_manager MyBoss) {
        this.Name = Name;
        this.connection = connection;
        this.MyBoss = MyBoss;
    }

    @Override
    public void run() {
        try {
//            InputStream in = connection.getInputStream();
//            StringBuilder message = new StringBuilder();
//            InputStreamReader reader = new InputStreamReader(in,"utf-8");
            DataInputStream in =new DataInputStream(connection.getInputStream());
            String message;
            while (true) {
                message = in.readUTF();
                MyBoss.send_message(process_message(message.toString()));
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(Name+" 已丢失连接");
            MyBoss.del_client(connection);
        }
    }

    String process_message(String message) {
        Date t = new Date();
        SimpleDateFormat df = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss\n");
        String Name_Date=Name.concat(df.format(t));
        String real_message=Name_Date.concat(message);
        return  real_message;

    }

}
