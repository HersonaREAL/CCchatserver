package operation;

import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.Queue;

public class Message implements Serializable {
    private int theType;//消息类型: 0群发 1私聊 2同步

    private String theFromUser; //消息从哪里来
    private String theToUser;  //消息到哪里去
    private String theMessage;//消息本体

    private Queue<String>  theClientMap;//同步给客户端用的
    private int theClientCount;                 //客户端数量

    public Message() {
        this.theType = -1;
        this.theFromUser = null;
        this.theFromUser = null;
        this.theMessage = null;
        this.theClientMap = null;
        this.theClientCount = -1;
    }

    /**
     * @return 消息类型: 0群发 1私聊 3同步
     */
    public int getTheType() {
        return theType;
    }

    /**
     * @return fromUser用户名
     */
    public String getTheFromUser() {
        return theFromUser;
    }

    public String getTheToUser() {
        return theToUser;
    }

    /**
     * @return 所发消息
     */
    public String getTheMessage() {
        return theMessage;
    }

    /**
     * @return 服务器的用户列表
     */
    public Queue<String> getTheClientMap() {
        return theClientMap;
    }

    /**
     * @return 服务器用户数
     */
    public int getTheClientCount() {
        return theClientCount;
    }

    /**
     * @param theMessage 群发消息内容
     */
    public void groupSend(String theMessage, String theFromUser) {
        this.theType = 0;
        this.theMessage = theMessage;

        this.theFromUser = theFromUser;
        this.theToUser = null;
        this.theClientMap = null;
        this.theClientCount = -1;
    }

    /**
     * @param theMessage  P2P消息内容
     * @param theFromUser 发送方
     * @param theToUser   接收方
     */
    public void p2pSend(String theFromUser, String theToUser, String theMessage) {
        this.theType = 1;
        this.theFromUser = theFromUser;
        this.theToUser = theToUser;
        this.theMessage = theMessage;

        this.theClientMap = null;
        this.theClientCount = -1;
    }

    /**
     * @param theMessage     同步消息内容
     * @param theClientMap   用户表
     * @param theClientCount 用户数
     */
    public void syncSend(String theMessage, Queue<String> theClientMap, int theClientCount) {
        this.theType = 2;
        this.theMessage = theMessage;
        this.theClientMap = theClientMap;
        this.theClientCount = theClientCount;

        this.theToUser = null;
        this.theFromUser = null;
    }

    /**
     * @return Object具体信息
     */
    @Override
    public String toString() {
        String a = "Object内容如下: \ntheType : " + getTheType() +
                "\ntheMessage : " + getTheMessage() +
                "\ntheFromUser : " + getTheFromUser() +
                "\ntheToUser : " + getTheToUser() +
                "\ntheNumberOfClient : " + getTheClientCount();
        if (getTheClientMap() != null)
            a.concat("\nHashMap:\n" + getTheClientMap().toString());
        return a;
    }

    public static void main(String[] args) {
        Message a = new Message();
        System.out.println(a.toString());
    }
}
