package whatsapp;

/**
 *
 * @author danielllamas
 */
import java.io.UnsupportedEncodingException;
import javafx.application.Application;
import javafx.application.Platform;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.VBox;
import javafx.stage.*;
import java.util.Arrays;

import java.lang.reflect.Array;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Whatsapp extends Application{
    int key=1;
    int modo=2; //1: texto plano    2: simetrico    3: asimetrico
    String decrypt="";
    String decrypt2="";
    private final boolean isServer=false; //true = servidor
    private final TextArea messages = new TextArea();
    private final Conexion connection = isServer ? crearServidor(): crearCliente();
    private Parent createContent(){
        messages.setPrefHeight(550);
        TextField input = new TextField();
        input.setOnAction(event -> {
            String message = isServer ? "Daniel: " : "María: ";
            
            message += input.getText();
            
            switch (modo){
                case 1: {
                    input.clear();
                    messages.appendText(message + "\n");
                    try {
                      connection.enviar(message);
                    }
                    catch (Exception e){
                    messages.appendText("Failed to send" + "\n");
                    }
                   break; 
                }
                case 2: {
                    String encrypt ="";
                    String decrypt =""; 
                    char[] chars=message.toCharArray();
                         for (char c:chars){
                            c+=key;
                            encrypt +=c;
                     }
                    input.clear();
                    messages.appendText(message + "\n");
                    message=encrypt;
                    try {
                      connection.enviar(message);
                    }
                    catch (Exception e){
                    messages.appendText("Failed to send" + "\n");
                }
                   break; 
                }
                case 3: {
                    int n = 143; //33
                    int en = 13;
                    int d = 37;
                    int hola;
                    
                    byte[] bytes;
                    String resultado="";
                    try {
                        bytes = message.getBytes("US-ASCII");
                        byte[] bytes2 = new byte[bytes.length];
                        int[] cifrado = new int[bytes.length];
                        int[] descifrado = new int[bytes.length];
                        
                        for (int i = 0; i < bytes.length; i++) {
 
            // Array.getByte method
                        byte k = Array.getByte(bytes, i);
                        hola = (int)k;
                        cifrado[i]=(int) ((Math.pow(hola,en)) % n); //Math.pow(numero, 3)
                        resultado+=Integer.toString(cifrado[i]);
                        
 
         }
        
                    } catch (java.io.UnsupportedEncodingException e) {
                    e.printStackTrace();}
                    
                    input.clear();
                    messages.appendText(message + "\n");
                    try {
                      connection.enviar(resultado);
                    }
                    catch (Exception e){
                    messages.appendText("Failed to send" + "\n");
                    }
                }

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
        int keyser=1;
        
        
        return new Server(55550, data -> {
            
            Platform.runLater(()-> {
                switch (modo){
                    case 1: {
                        messages.appendText(data.toString() + "\n");
                        break;
                    }
                    case 2: {
                        char[] chars2=data.toString().toCharArray();
                        for (char c:chars2){
                            c-=keyser;
                            decrypt+=c;
                        }
                        messages.appendText(decrypt + "\n"); //messages.appendText(data.toString() + "\n");
                        break;
                    }
                    case 3: {
                        char[] chars2=data.toString().toCharArray();
                        int[] descifrado= new int[chars2.length];
                        int n = 143; //33
                        int en = 13;
                        int d = 37;
                        
                        for (char c:chars2){
                            int a=Character.getNumericValue(c);
                            descifrado[c]=(int) ((Math.pow(a,d)) % n);
                            decrypt2+=c;
                        }
                        messages.appendText(decrypt2 + "\n"); //messages.appendText(data.toString() + "\n");
                        break;
                    }
                }
            });
        });
        
    }
    
    private Cliente crearCliente(){
        int keyclient=1;
        return new Cliente("127.0.0.1", 55550, data -> {
            Platform.runLater(()-> {
                switch (modo){
                    case 1: {
                        messages.appendText(data.toString() + "\n");
                        break;
                    }
                    case 2: {
                        char[] chars2=data.toString().toCharArray();
                        for (char c:chars2){
                            c-=keyclient;
                            decrypt+=c;
                        }
                        messages.appendText(decrypt + "\n"); //messages.appendText(data.toString() + "\n");
                        break;
                    }
                    case 3: {
                        char[] chars2=data.toString().toCharArray();
                        int[] descifrado= new int[chars2.length];
                        int n = 143; //33
                        int en = 13;
                        int d = 37;
                        
                        for (char c:chars2){
                            int a=Character.getNumericValue(c);
                            descifrado[c]=(int) ((Math.pow(a,d)) % n);
                            decrypt2+=c;
                        }
                        messages.appendText(decrypt2 + "\n"); //messages.appendText(data.toString() + "\n");
                        break;
                        
                    }
                }
                
            });
        });
        
    }
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        launch(args);
       
    }

   

    
}
