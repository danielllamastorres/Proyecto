import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import javafx.scene.control.Label;

public class Precertificado extends Application {

    private TextField textField1;
    private TextField textField2;
    private TextField textField3;
    private DatePicker datePicker;

    @Override
    public void start(Stage primaryStage) {
        textField1 = new TextField();
        textField2 = new TextField();
        textField3 = new TextField();
        datePicker = new DatePicker();
        
        Label label1 = new Label("Nombre:");
        Label label2 = new Label("Frase de seguridad:");
        Label label3 = new Label("Telefono:");
        Label label4 = new Label("Fecha:");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveTextToFiles();
            }
        });

        VBox root = new VBox(10);
        root.getChildren().addAll(label1,textField1, label2, textField2, label3, textField3,label4, datePicker, saveButton);

        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("Generar precertificado");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void saveTextToFiles() {
       String publickey="38";
            
            int safeword = Integer.parseInt(textField2.getText());
                    
                    String encrypt ="";
                    String decrypt =""; 
                    char[] chars=publickey.toCharArray();
                         for (char c:chars){
                            c+=safeword;
                            encrypt +=c;
                     }
        try (BufferedWriter writer1 = new BufferedWriter(new FileWriter("/Users/danielllamas/Desktop/Proyecto Final TS/User.req"));
             BufferedWriter writer2 = new BufferedWriter(new FileWriter("User.key"));
             BufferedWriter writer3 = new BufferedWriter(new FileWriter("registroARC.txt"))) {
            
            writer1.write(publickey);
            writer1.newLine();
            writer1.write(textField1.getText());
            writer1.newLine();
            writer1.write(textField3.getText());
            writer1.newLine();
            
            writer3.write(safeword);
            
            
            writer2.write(encrypt);
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate != null) {
                writer1.write(selectedDate.toString());
                writer1.newLine();
                writer2.write(selectedDate.toString());
                writer2.newLine();
            }

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
