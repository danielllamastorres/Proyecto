import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.scene.control.Button;

import javafx.stage.Stage;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Opcion2 extends Application {
    private static final String[][] template = {
            {"7", "8", "9", "/"},
            {"4", "5", "6", "*"},
            {"1", "2", "3", "-"},
            {"0", "c", "=", "+"}
    };

    private final Map<String, Button> accelerators = new HashMap<>();

    private final DoubleProperty stackValue = new SimpleDoubleProperty();
    private final DoubleProperty value = new SimpleDoubleProperty();

    private enum Op {NOOP, ADD, SUBTRACT, MULTIPLY, DIVIDE}

    private Op curOp = Op.NOOP;
    private Op stackOp = Op.NOOP;
    
    public String sfinal="";
    public String operando;
    public String num1;
    public String num2;
    public static String request;
    public Socket s = null;
    public Socket s2= null;
    public Socket s3= null;
    public Socket s4= null;
    public Socket s5= null;
    public Socket s6= null;
    public Socket s7= null;
    public Socket s8= null;
    
    public String ans;

    public static void main(String[] args) {
        
        launch(args);
        

        
    }
 

    @Override
    public void start(Stage stage) {
        final TextField screen = createScreen();
        final TilePane buttons = createButtons();

        stage.setTitle("Calculadora");
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        stage.setScene(new Scene(createLayout(screen, buttons)));
        stage.show();
    }

    private VBox createLayout(TextField screen, TilePane buttons) {
        final VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: black; -fx-padding: 20; -fx-font-size: 20;");
        layout.getChildren().setAll(screen, buttons);
        handleAccelerators(layout);
        screen.prefWidthProperty().bind(buttons.widthProperty());
        return layout;
    }

    private void handleAccelerators(VBox layout) {
        layout.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            Button activated = accelerators.get(keyEvent.getText());
            if (activated != null) {
                activated.fire();
            }
        });
    }

    private TextField createScreen() {
        final TextField screen = new TextField();
        screen.setStyle("-fx-background-color: aquamarine;");
        screen.setAlignment(Pos.CENTER_RIGHT);
        screen.setEditable(false);
        screen.textProperty().bind(Bindings.format("%.0f", value));
        return screen;
    }

    private TilePane createButtons() {
        TilePane buttons = new TilePane();
        buttons.setVgap(7);
        buttons.setHgap(7);
        buttons.setPrefColumns(template[0].length);
        for (String[] r : template) {
            for (String s : r) {
                buttons.getChildren().add(createButton(s));
                
            }
            
        }
        return buttons;
    }

    private Button createButton(final String s) {
        Button button = makeStandardButton(s);

        if (s.matches("[0-9]")) {
            makeNumericButton(s, button);
        } else {
            final ObjectProperty<Op> triggerOp = determineOperand(s);
            if (triggerOp.get() != Op.NOOP) {
                makeOperandButton(button, triggerOp);
            } else if ("c".equals(s)) {
                makeClearButton(button);
            } else if ("=".equals(s)) {
                makeEqualsButton(button);
            }
        }

        return button;
    }

    private ObjectProperty<Op> determineOperand(String s) {
        final ObjectProperty<Op> triggerOp = new SimpleObjectProperty<>(Op.NOOP);
        switch (s) {
            case "+" -> triggerOp.set(Op.ADD) ; 
            case "-" -> triggerOp.set(Op.SUBTRACT);
            case "*" -> triggerOp.set(Op.MULTIPLY);
            case "/" -> triggerOp.set(Op.DIVIDE);
        }
        operando = s;
         //System.out.println(s);
        return triggerOp;
         
        
    }

    private void makeOperandButton(Button button, final ObjectProperty<Op> triggerOp) {
        button.setStyle("-fx-base: lightgray;");
        button.setOnAction(actionEvent -> curOp = triggerOp.get());
        
    }

    private Button makeStandardButton(String s) {
        Button button = new Button(s);
        button.setStyle("-fx-base: beige;");
        accelerators.put(s, button);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return button;
    }

    private void makeNumericButton(final String s, Button button) {
        button.setOnAction(actionEvent -> {
            if (curOp == Op.NOOP) {
                value.set(value.get() * 10 + Double.parseDouble(s));
                //sfinal= "hola";
                System.out.println(sfinal);
            } else {
                stackValue.set(value.get());
                value.set(Double.parseDouble(s));
                stackOp = curOp;
                curOp = Op.NOOP;
            }
        });
    }

    private void makeClearButton(Button button) {
        button.setStyle("-fx-base: mistyrose;");
        button.setOnAction(actionEvent -> value.set(0));
    }

    public void makeEqualsButton(Button button) {
        
        try{
            String address="127.0.0.1";
            String address1="127.0.0.2";
            String address2="127.0.0.3";
            int port = 5003;
            int port2= 5001;
            int port3=5002;
            int port4=5004;
            int port5=5005;
            int port6=5006;
            int port7=5007;
            int port8=5008;
            s = new Socket(address, port);
            s2 = new Socket(address, port2);
            s3 = new Socket(address, port3);
            s4 = new Socket(address, 5009);
//            s5 = new Socket(address, port5);
//            s6 = new Socket(address, port6);
//            s7 = new Socket(address, port7);
//            s8 = new Socket(address, port8);
            
            
            System.out.println("Conectado");
            
            DataInputStream dis
                = new DataInputStream(s2.getInputStream());
            DataOutputStream dos
                = new DataOutputStream(s.getOutputStream());
            DataInputStream dis2
                = new DataInputStream(s.getInputStream());
            
            DataOutputStream dos2
                = new DataOutputStream(s2.getOutputStream());
            DataInputStream dis3
                = new DataInputStream(s3.getInputStream());
            DataOutputStream dos3
                = new DataOutputStream(s3.getOutputStream());
            DataInputStream dis4
                = new DataInputStream(s4.getInputStream());
            DataOutputStream dos4
                = new DataOutputStream(s4.getOutputStream());
//            DataOutputStream dos5
//                = new DataOutputStream(s5.getOutputStream());
//            DataOutputStream dos6
//                = new DataOutputStream(s6.getOutputStream());
//            DataOutputStream dos7
//                = new DataOutputStream(s7.getOutputStream());
//            DataOutputStream dos8
//                = new DataOutputStream(s8.getOutputStream());
            
            button.setStyle("-fx-base: ghostwhite;");
        button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    int a1=(int)stackValue.get();
                    int a2=(int)value.get();
                    num1=Integer.toString(a1);
                    num2=Integer.toString(a2);
                    switch (stackOp) {
                        case ADD -> {operando = "+"; }
                        case SUBTRACT -> {operando = "-";}
                        case MULTIPLY -> {operando = "*";}
                        case DIVIDE -> { operando = "/";}
                    }
                    request=(num1+" "+operando+" "+num2);

                        try {
                           // if (request==null)
                             //   break;
                           // dos.writeUTF(request);
                           dos.writeUTF(request);
                           dos4.writeUTF(request);
                           dos2.writeUTF(request);
                            dos3.writeUTF(request);
//                            dos5.writeUTF(request);
//                            dos6.writeUTF(request);
//                            dos7.writeUTF(request);
//                            dos8.writeUTF(request);
                            if (operando=="+"){
                            ans = dis.readUTF();
                            }
                            else if (operando=="*"){
                                ans = dis2.readUTF();                        
                            }
                            else if (operando=="-"){
                                ans = dis3.readUTF();                        
                            }
                            else if (operando=="/"){
                                ans = dis4.readUTF();                        
                            }
                            
                            
                        } catch (IOException ex) {
                            Logger.getLogger(Opcion2.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.out.println("respuesta = " + ans); 
                        value.set(Double.parseDouble(ans)); 
                }
            });
            
        }
        
        
        catch (Exception e){
            System.out.println("Error en la conexion");
        }
        
    }
    
}
