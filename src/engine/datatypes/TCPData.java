package engine.datatypes;

public class TCPData {

    public static int USER_ICON = 1;
    public static int VIBRATE = 2;
    public int id;
    public int key;
    public String value;
    public static final int CLOSE_CONNECTION = -1;

    public TCPData(int key, String value){
        this.key = key;
        this.value = value;
    }

    public TCPData(int id, int key, String value){
        this.key = key;
        this.value = value;
    }


    public TCPData(){

    }

    public TCPData(int key){
        this.key = key;
    }

}
