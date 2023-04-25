package whatsapp;

/**
 *
 * @author danielllamas
 */
import java.util.Scanner;
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
import javafx.scene.layout.TilePane;

public class Whatsapp extends Application{
    int key=1;
    int modo; //1: texto plano    2: simetrico    3: asimetrico
    String decrypt="";
    String decrypt2="";
    private final boolean isServer=false; //true = servidor
    private final TextArea messages = new TextArea();
    private final Conexion connection = isServer ? crearServidor(): crearCliente();
    
    
    private Parent createContent(){
        Button button1 = new Button("Texto Plano");
        Button button2 = new Button("Simetrico");
        Button button3 = new Button("Asimetrico");
        Button button4 = new Button("Sobre Digital");
        messages.setPrefHeight(550);
        createButton();
        TextField input = new TextField();
        
        button1.setOnAction(e -> {
            modo=1;

            String message = isServer ? "daniel: " : "maria: ";
                    message += input.getText();
                    
                    input.clear();
                    messages.appendText(message + "\n");
                    try {
                      connection.enviar(message);
                    }
                    catch (Exception ex){
                    messages.appendText("Failed to send" + "\n");
                    }
      
        });
        button2.setOnAction(e -> {
            modo=2;
            String message = isServer ? "daniel: " : "maria: ";
                    message += input.getText();
                    
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
                    System.out.println(message);
                    try {
                      connection.enviar(message);
                    }
                    catch (Exception ex){
                    messages.appendText("Failed to send" + "\n");
                }
        });
        
        button3.setOnAction(e-> {
        modo=3;
        String message = isServer ? "Daniel: " : "María: ";
                    message += input.getText();
                    
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
        
                    } catch (java.io.UnsupportedEncodingException ex) {
                    ex.printStackTrace();}
                    
                    input.clear();
                    messages.appendText(message + "\n");
                    try {
                      connection.enviar(resultado);
                    }
                    catch (Exception ex){
                    messages.appendText("Failed to send" + "\n");
                    }
        
        
        });
        
        button4.setOnAction(e->{
        modo=4;
        String message = isServer ? "daniel: " : "maria: ";
        message += input.getText();
        String display=message;
        
        String abecedario= "abcdefghijklmnñopqrstuvwxyz0123456789";
        
        int keyser; //lave privada de daniel
        if (isServer==false){
        keyser=1;
        }else{
        keyser=2;
        }
        
        
        int hash = 0;
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            hash += c;
        }
        String decrypt="";
        int newindex=0;

        char[] chars1 = ("" + hash).toCharArray();
                        for (char c:chars1){
                            newindex=abecedario.indexOf(c)+keyser;
                            if(newindex>37){
                                newindex=newindex-37-1;
                            }
                            c=abecedario.charAt(newindex);
                            decrypt+=c;
                        }

        String docfirmado=message+","+decrypt;

        
        int llavealeatoria =5;
        String docfirmadocif="";
        int llavepubMaria = 39;
        int llaveRandCif=0;
        char[] chars2 = docfirmado.toCharArray();
                        for (char c:chars2){
                            if (c==' '||c==','||c==':'){
                                docfirmadocif+=c;
                            }else {
                            newindex=abecedario.indexOf(c)+llavealeatoria-1;
                            if(newindex>37){
                                newindex=newindex-37-1;
                            }
                            c=abecedario.charAt(newindex);
                            docfirmadocif+=c;
                        } 
                        }
                        
       
        llaveRandCif=llavealeatoria+llavepubMaria-37-1;
        docfirmadocif=docfirmadocif+","+llaveRandCif;
        
        message=docfirmadocif;
        input.clear();
        
                    messages.appendText(display + "\n");
                    try {
                      connection.enviar(message);
                    }
                    catch (Exception ex){
                    messages.appendText("Failed to send" + "\n");
                }
        
        });
 
 
        VBox root=new VBox(20, messages, input, button1, button2, button3, button4);
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
    private Button createButton() {
    Button button = new Button("Click Me");
    return button;
    }   
    private Server crearServidor(){
        int keyser=3;
        
        
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
                    case 4: {
                   
                    String docfirmadocif=data.toString();
                    String abecedario= "abcdefghijklmnñopqrstuvwxyz0123456789";
                    
                    String[] stringarray = docfirmadocif.split(",");
                    int newindex=0; 
                    String MensajeCifrado = stringarray[0];
                    String ResumenCifrado = stringarray[1];
                    String LlaveAleatoriaCifrada = stringarray[2];
                  
                    System.out.println("Inserta la llave privada: ");
                    Scanner stringScanner = new Scanner(System.in);
                    String age = stringScanner.next();
                    
                    int llaveusuario=Integer.parseInt(age);
                    int LlavePrivadaMaria=2;
                    int LlavePrivadaDaniel=1;
                    int llaveDescifradora;
                    
                    if (isServer == false) {
                        llaveDescifradora=Integer.parseInt(LlaveAleatoriaCifrada)-llaveusuario;
                    }else {
                        llaveDescifradora=Integer.parseInt(LlaveAleatoriaCifrada)-llaveusuario-1;
                    }
        
                    char[] chars3 = docfirmadocif.toCharArray();
                    String docfirmadodescif="";
                        for (char c:chars3){
                            if (c==' '||c==','||c==':'){
                                docfirmadodescif+=c;
                            }else {
                            newindex=abecedario.indexOf(c)-llaveDescifradora;
                            if(newindex<0){
                                newindex=newindex+37-1;
                            }
                            c=abecedario.charAt(newindex);
                            docfirmadodescif+=c;
                        } 
                        }
                        
    
                    messages.appendText(docfirmadodescif + "\n"); //messages.appendText(data.toString() + "\n");
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
                    case 4: {
                    String docfirmadocif=data.toString();
                   
                    String abecedario= "abcdefghijklmnñopqrstuvwxyz0123456789";
                    
                    String[] stringarray = docfirmadocif.split(",");
                    int newindex=0; 
                    String MensajeCifrado = stringarray[0];
                    String ResumenCifrado = stringarray[1];
                    String LlaveAleatoriaCifrada = stringarray[2];

                    System.out.println("Inserta la llave privada: ");
                    Scanner stringScanner = new Scanner(System.in);
                    String age = stringScanner.next();
                    
                    int llaveusuario=Integer.parseInt(age);
        
                    int LlavePrivadaMaria=2;
                    int LlavePrivadaDaniel=1;
                    
                    int llaveDescifradora;
                    
                    if (isServer == false) {
                        llaveDescifradora=Integer.parseInt(LlaveAleatoriaCifrada)-llaveusuario;
                    }else {
                        llaveDescifradora=Integer.parseInt(LlaveAleatoriaCifrada)-llaveusuario-1;
                    }
                    

        
                    char[] chars3 = docfirmadocif.toCharArray();
                    String docfirmadodescif="";
                        for (char c:chars3){
                            if (c==' '||c==','||c==':'){
                                docfirmadodescif+=c;
                            }else {
                            newindex=abecedario.indexOf(c)-llaveDescifradora;
                            if(newindex<0){
                                newindex=newindex+37-1;
                            }
                            c=abecedario.charAt(newindex);
                            docfirmadodescif+=c;
                        } 
                        }
                        

                        messages.appendText(docfirmadodescif + "\n"); //messages.appendText(data.toString() + "\n");
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
