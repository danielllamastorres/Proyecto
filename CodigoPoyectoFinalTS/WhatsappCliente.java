package whatsapp;

/**
 *
 * @author danielllamas
 */
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.*;

public class WhatsappCliente extends Application{
    private final boolean isServer=false; //true = servidor
    private final TextArea messages = new TextArea();
    private final Conexion connection = isServer ? crearServidor(): crearCliente();
    private Parent createContent(){
        messages.setPrefHeight(550);
        TextField input = new TextField();
        input.setOnAction(event -> {
            String message = isServer ? "Server: " : "Client: ";
            message += input.getText();
            input.clear();
            
            messages.appendText(message + "\n");
            
            try {
                connection.enviar(message);
            }
            catch (Exception e){
                messages.appendText("Failed to send" + "\n");
            }
            
        });
        
        VBox root=new VBox(20, messages, input);
        root.setPrefSize(600,600);
        return root;
    }
    
    @Override
    public void init() throws Exception {
        connection.iniciarConexion();
        
    }
    
    @Override
    public void start (Stage primaryStage) throws Exception{
        primaryStage.setScene(new Scene (createContent()));
        primaryStage.show();
    }
    
    public void stop() throws Exception {
        
    }
    
    private Server crearServidor(){
        return new Server(55550, data -> {
            Platform.runLater(()-> {
                messages.appendText(data.toString() + "\n");
            });
        });
        
    }
    
    private Cliente crearCliente(){
        return new Cliente("127.0.0.1", 55550, data -> {
            Platform.runLater(()-> {
                messages.appendText(data.toString() + "\n");
            });
        });
        
    }
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        launch(args);
    }

   

    
}
