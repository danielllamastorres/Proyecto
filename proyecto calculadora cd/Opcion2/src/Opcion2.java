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
    private Socket s = null;
    private Socket s2= null;
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

    private void makeEqualsButton(Button button) {
        
        try{
            String address="127.0.0.1";
            int port = 5000;
            s = new Socket(address, port);
            System.out.println("Conectado");
            
            DataInputStream dis
                = new DataInputStream(s.getInputStream());
            DataOutputStream dos
                = new DataOutputStream(s.getOutputStream());
            
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
                            dos.writeUTF(request); //string de entrada
                            ans = dis.readUTF();
                            
                            
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
