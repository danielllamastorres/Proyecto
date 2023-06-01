package whatsapp;
 
import java.io.Serializable;
import java.util.function.Consumer;


public class Cliente extends Conexion {
    private String ip;
    private int port;

    public Cliente(String ip, int port,Consumer<Serializable> onReceiveCallback) {
        super(onReceiveCallback);
        this.ip=ip;
        this.port=port;
    }

    @Override
    protected boolean isServer() {
        return false;
    }

    @Override
    protected String getIP() {
        return ip;
    }

    @Override
    protected int getPort() {
        return port;
    }
    
}
