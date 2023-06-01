import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.util.Random;

import java.io.*;

public class PKI extends Application {

    private TextField textField;
    private Label resultLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Registro de Llaves Públicas");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        textField = new TextField();
        Button registerButton = new Button("Registrar");
        registerButton.setOnAction(e -> register());

        resultLabel = new Label();

        vbox.getChildren().addAll(textField, registerButton, resultLabel);

        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void register() {
    String inputFilePath = "/Users/danielllamas/Desktop/Proyecto Final TS/ARCRegistro.txt";   // Replace with the path to your input text file
    String outputFilePath = "/Users/danielllamas/Desktop/Proyecto Final TS/ARCregistro.txt";
    String outputFilePath2 = "ARregistro.txt";// Replace with the path where the output file will be created
    String searchString = textField.getText(); // Use the input from the text field

    try {
        boolean found = searchInFile(inputFilePath, searchString);
        if (found) {
            resultLabel.setText("ARC: Ya está registrado este usuario.");
        } else {
            Random random = new Random();
            int randomNumber = random.nextInt(90000) + 10000;
            resultLabel.setText("ARC: Llave pública no encontrada, registro exitoso. Certificado creado con el número: "+ randomNumber +" .");
            
            writeToFile(outputFilePath, searchString);
            
            String certificado = "ID: " + randomNumber + "\nExpedido por AC1.\n";
            writeToFile("/Users/danielllamas/Desktop/Proyecto Final TS/Daniel_cer.txt", certificado);

            String sourceFilePath = "/Users/danielllamas/Desktop/Proyecto Final TS/User.req";
            String destinationFilePath = "/Users/danielllamas/Desktop/Proyecto Final TS/Daniel_cer.txt";

            try {
                BufferedReader reader = new BufferedReader(new FileReader(sourceFilePath));
                FileWriter writer = new FileWriter(destinationFilePath, true); // Append to the existing file

                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.write("\n"); // Add a new line after each line (optional)
                }

                writer.close();
                reader.close();

                System.out.println("Text file copied successfully.");
            } catch (IOException e) {
                System.out.println("An error occurred while copying the text file.");
                e.printStackTrace();
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    private boolean searchInFile(String filePath, String searchString) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(searchString)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void writeToFile(String filePath, String content) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
        writer.write(content);
        writer.newLine(); // Add a new line after the content if needed
    }
}
}
