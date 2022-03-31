import networking.ServerThread;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(6543);
            ConcurrentHashMap<Long, ObjectOutputStream> outputMap = new ConcurrentHashMap<>();

            while (true) {
                new ServerThread(serverSocket.accept(),outputMap).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


