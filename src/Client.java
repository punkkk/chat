import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by dart on 01.09.15.
 * Клиент к серверу, подключается по определенному порту, есть возможность отправлять сообщения на сервер
 * @author punkkk
 */
class Client {
    private int _serverPort;
    private InetAddress _serverAddress;
    private Socket _socket;
    private DataOutputStream _out;
    private BufferedReader _keyboard;
    private String _name;
    public Client( int port, String address){
        this._serverPort = port;
        this.setAddress(address);
    }
    public void setName(String name){
        this._name = name;
    }
    public void setAddress(String address) {
        try {
            this._serverAddress = InetAddress.getByName(address);
        }catch (IOException err){
            System.out.println(err.getMessage());
        }
    }

    public String getAddress() {
        return this._serverAddress.toString();
    }

    public void setPort(int port){
        this._serverPort = port;
    }

    public void setSocket() {
        try {
            this._socket = new Socket(this._serverAddress, this._serverPort);
        }catch(IOException err){
            System.out.println(err.getMessage());
        }
    }

    public void setOutputStream(){
        try {
            this._out = new DataOutputStream(this._socket.getOutputStream());
        }catch(IOException err){
            System.out.println(err.getMessage());
        }
    }
    public void setKeyboard() {
        this._keyboard = new BufferedReader(new InputStreamReader(System.in));
    }
    public void startClient(){
        this.setSocket();
        this.setOutputStream();
        this.setKeyboard();
        System.out.println("Server address:" + this.getAddress() + " port: " + this._serverPort);
        String line;
        try {
            System.out.println("Type something");
            new ClientThread(_socket);
            while (true) {
                line = _keyboard.readLine();
                if(line.equalsIgnoreCase("exit")) {
                    System.exit(0);
                }
                this._out.writeUTF(this._name + ": " + line);
                this._out.flush();
            }
        }catch (IOException err){
            System.out.println(err.getMessage());
        }
    }
    public void argsAggregation(String[] args){
        if(args.length >= 1){
            this._name = args[0];
        }else{
            this._name = "ANONIMUS";
        }
    }
    public static void main(String[] args){
        Client client = new Client(1734, "127.0.0.1");
        client.argsAggregation(args);
        client.startClient();
    }
}