package buffer;

import java.io.Serializable;
import java.util.Queue;

public class Message implements Serializable {
    private int theType;//��Ϣ����: 0Ⱥ�� 1˽�� 2ͬ��

    private String theFromUser; //��Ϣ��������
    private String theToUser;  //��Ϣ������ȥ
    private String theMessage;//��Ϣ����

    private Queue<String>  theClientMap;//ͬ�����ͻ����õ�
    private int theClientCount;                 //�ͻ�������

    public Message() {
        this.theType = -1;
        this.theFromUser = null;
        this.theFromUser = null;
        this.theMessage = null;
        this.theClientMap = null;
        this.theClientCount = -1;
    }

    /**
     * @return ��Ϣ����: 0Ⱥ�� 1˽�� 3ͬ��
     */
    public int getTheType() {
        return theType;
    }

    /**
     * @return fromUser�û���
     */
    public String getTheFromUser() {
        return theFromUser;
    }

    public String getTheToUser() {
        return theToUser;
    }

    /**
     * @return ������Ϣ
     */
    public String getTheMessage() {
        return theMessage;
    }

    /**
     * @return ���������û��б�
     */
    public Queue<String> getTheClientMap() {
        return theClientMap;
    }

    /**
     * @return �������û���
     */
    public int getTheClientCount() {
        return theClientCount;
    }

    /**
     * @param theMessage Ⱥ����Ϣ����
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
     * @param theMessage  P2P��Ϣ����
     * @param theFromUser ���ͷ�
     * @param theToUser   ���շ�
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
     * @param theMessage     ͬ����Ϣ����
     * @param theClientMap   �û���
     * @param theClientCount �û���
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
     * @return Object������Ϣ
     */
    @Override
    public String toString() {
        String a = "Object��������: \ntheType : " + getTheType() +
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
