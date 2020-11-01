import java.net.Socket;

public class client_process implements Runnable{
    private String Name;
    private Socket connection;
    private client_manager MyBoss;
    client_process(String Name,Socket connection,client_manager MyBoss){
        this.Name=Name;
        this.connection=connection;
        this.MyBoss=MyBoss;
    }
    @Override
    public void run() {

    }
}
