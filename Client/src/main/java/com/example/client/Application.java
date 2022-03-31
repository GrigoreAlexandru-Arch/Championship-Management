package com.example.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class Application extends javafx.application.Application {
    static Stage currentStage = null;
    static Socket socket = null;
    static ObjectOutputStream outputStream = null;
    static ObjectInputStream inputStream = null;

    @Override
    public void start(Stage stage) throws IOException {
        socket = new Socket("localhost", 6543);

        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());

        currentStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-stage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        new Thread(() -> {
            try {
                MainController mainController = fxmlLoader.getController();

                while (true) {
                    Object receive = null;

                    receive = inputStream.readObject();
                    if (receive.getClass() == String.class) {
                        Object finalReceive = receive;
                        mainController.setResponse((String) finalReceive);
                    } else {
                        StringBuilder message = new StringBuilder(new String(""));

                        for (Map.Entry<String, Integer> entry : ((Map<String, Integer>) receive).entrySet()) {
                            String s = entry.getKey();
                            Integer integer = entry.getValue();
                            message.append(s).append(" ").append(integer).append('\n');
                        }
                        String finalMessage = message.toString();
                        mainController.setResponse(finalMessage);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();

        stage.setTitle("Championship");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}