import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Middleware extends Application {

    private TextArea logTextArea;
    private Label connectionLabel;
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    
    @Override
    public void start(Stage primaryStage) {
        connectionLabel = new Label("Waiting for connections...");
        logTextArea = new TextArea();
        logTextArea.setEditable(false);

        BorderPane root = new BorderPane();
        root.setTop(connectionLabel);
        root.setCenter(logTextArea);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Start the middleware server
        new Thread(() -> startServer()).start();
    }

    private void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    logConnection(clientSocket);
                    executorService.submit(() -> processRequest(clientSocket));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   private void processRequest(Socket clientSocket) {
    try {
        // Connect to local server
        Socket socket = new Socket("127.0.0.2", 5004);
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Send request and log response
        String request = "test";
        writer.println(request);
        writer.flush();
        String response = reader.readLine();
        logToTextArea("Received response from local server: " + response);

        // Close connections
        //reader.close();
        //writer.close();
        //socket.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    private void logConnection(Socket clientSocket) {
        String connectionInfo = "New connection from " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
        logToTextArea(connectionInfo);
        Platform.runLater(() -> connectionLabel.setText(connectionInfo));
    }

    private void logToTextArea(String message) {
        Platform.runLater(() -> {
            logTextArea.appendText(message + "\n");
            logTextArea.setScrollTop(Double.MAX_VALUE);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
