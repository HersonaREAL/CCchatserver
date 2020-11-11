import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class clientStreamSave {
    private final ObjectInputStream ois;
    private final ObjectOutputStream oos;

    public clientStreamSave(ObjectInputStream ois, ObjectOutputStream oos){
        this.ois=ois;
        this.oos=oos;
    }

    public ObjectInputStream getOis(){
        return ois;
    }
    public ObjectOutputStream getOos(){
        return oos;
    }
}
