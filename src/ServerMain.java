

import operation.client_manager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    public final static int PORT=23333;
    private static final client_manager manager = new client_manager();
    public static void main(String[] args) {
        try(ServerSocket server = new ServerSocket(PORT)){
            while (true) {
                Socket connection = server.accept();
                if(!manager.creat_client(connection)){
                    System.err.println("客户端连接失败");
                    connection.close();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
